//define kv storage engine
//This version does not support data persistence operations
//The next version will do consistent synchronization based on raft algorithm on data persistence
type StorageEngine struct {
	// Package sync provides basic synchronization primitives such as mutual
	// exclusion locks. Other than the Once and WaitGroup types, most are intended
	// for use by low-level library routines. Higher-level synchronization is
	// better done via channels and communication.
	//
	// Values containing the types defined in this package should not be copied.
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
