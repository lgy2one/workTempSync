package com.qg.exclusiveplug.map;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHolder {
    private static final Map<Integer, List<WebSocketSession>> map = new ConcurrentHashMap<>(16);

    public static void put(Integer deviceIndex, List<WebSocketSession> webSocketSessionList) {
        map.put(deviceIndex, webSocketSessionList);
    }

    public static List<WebSocketSession> getWebsocketSessionList(Integer deviceIndex) {
        return map.get(deviceIndex);
    }

    public static Map<Integer, List<WebSocketSession>> getMap() {
        return map;
    }

    public static Integer getInteger(List<WebSocketSession> webSocketSessionList) {
        for(Integer deviceIndex:map.keySet()){
            if (webSocketSessionList == map.get(deviceIndex)) {
                return deviceIndex;
            }
        }
        return null;
    }

    public static boolean containsKey(Integer deviceIndex) {
        return map.containsKey(deviceIndex);
    }

    public static void removeList(List<WebSocketSession> webSocketSessionList) {
        map.entrySet().stream().filter(entry -> entry.getValue() == webSocketSessionList)
                .forEach(entry -> map.remove(entry.getKey()));
    }

}
