package main

import (
	"errors"
	"fmt"
	"net/http"
	"os"
	"sync"
)

/**
* @student no  3117004523
* @author liguoyan
* @date 2020/6/26
 */

//the global KV storage
var KV map[string]interface{}

//define kv storage engine
//This version does not support data persistence operations
//The next version will do consistent synchronization based on raft algorithm on data persistence
type StorageEngine struct {
	sync.Mutex                                         //Ensure thread safety
	Get        func(key string) (interface{}, error)   //get operation
	Put        func(key string, val interface{}) error //put operation
	Del        func(key string) bool                   //delete operating
}

//Use cow thinking to keep safe
//Read and write will not conflict
//Best read performance
var CopyOnWriteEngine StorageEngine

//Storage engine using synchronizer
//Balanced performance
var SyncEngine StorageEngine

//config
var Config ClusterConfig

//init
func init() {
	KV = make(map[string]interface{})
	//init cow engine
	CopyOnWriteEngine = StorageEngine{
		Mutex: sync.Mutex{},
		//The get operation only needs to be unlocked
		Get: func(key string) (interface{}, error) {
			if v, ok := KV[key]; ok {
				return v, nil
			}
			return nil, errors.New("fail")
		},
		Del: func(key string) bool {
			delete(KV, key)
			return true
		},
	}
	CopyOnWriteEngine.Put = func(key string, val interface{}) error {
		CopyOnWriteEngine.Lock()
		defer CopyOnWriteEngine.Unlock()
		KV = Copy(KV)
		KV[key] = val
		return nil
	}
	//sync engine
	SyncEngine = StorageEngine{
		Mutex: sync.Mutex{},
	}
	SyncEngine.Get = func(key string) (interface{}, error) {
		SyncEngine.Lock()
		defer SyncEngine.Unlock()
		if v, ok := KV[key]; ok {
			return v, nil
		}
		return nil, errors.New("fail")
	}
	SyncEngine.Put = func(key string, val interface{}) error {
		SyncEngine.Lock()
		defer SyncEngine.Unlock()
		KV[key] = val
		return nil
	}
	SyncEngine.Del = func(key string) bool {
		SyncEngine.Lock()
		defer SyncEngine.Unlock()
		delete(KV, key)
		return true
	}
}

//main function
func main() {
	if len(os.Args) < 3 {
		fmt.Println("Usage : go run xx   <master>{:port} <slave>{:port}")
		return
	}
	//config by args
	Config = ClusterConfig{
		master: os.Args[1],
		slave:  os.Args[2],
	}
	//Register route
	mux := http.DefaultServeMux
	mux.HandleFunc("/get", HandlerGet)
	mux.HandleFunc("/put", HandlerPut)
	mux.HandleFunc("/del", HandlerDel)
	//Open service
	_ = http.ListenAndServe(Config.master, mux)
}

//Get without Disaster recovery
func HandlerGet(w http.ResponseWriter, r *http.Request) {
	key := r.FormValue("k")
	v, err := SyncEngine.Get(key)
	if err != nil {
		w.Write([]byte("key not exist"))
		return
	}
	//autonomy
	_, _ = w.Write([]byte(v.(string)))
}

//KV request processor with master-slave disaster recovery
func HandlerPut(w http.ResponseWriter, r *http.Request) {
	key := r.FormValue("k")
	val := r.FormValue("v")
//replica
	_ = SyncEngine.Put(key, val)
	//»›‘÷
	_, _ = http.Get(Config.slave + "/put?k=" + key + "&v=" + val)
	_, _ = w.Write([]byte("ok"))
}

//KV request processor with master-slave disaster recovery
func HandlerDel(w http.ResponseWriter, r *http.Request) {
	key := r.FormValue("k")
	SyncEngine.Del(key)
	http.Get(Config.slave + "/del?k=" + key)
	_, _ = w.Write([]byte("ok"))
}

//Shallow clone
func Copy(value map[string]interface{}) map[string]interface{} {
	newMap := make(map[string]interface{})
	for k, v := range value {
		newMap[k] = v
	}
	return newMap
}

//Cluster configuration
type ClusterConfig struct {
	master string
	slave  string
	//Now only support a master with a slave.
	//Both are masters themselves, the other is slave
