package com.qg.exclusiveplug.map;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelHolder {
    private static final Map<Integer, List<ChannelHandlerContext>> map = new ConcurrentHashMap<>(16);

    public static void put(Integer deviceIndex, List<ChannelHandlerContext> channelHandlerContextList) {
        map.put(deviceIndex, channelHandlerContextList);
    }

    public static List<ChannelHandlerContext> get(Integer deviceIndex) {
        return map.get(deviceIndex);
    }

    public static Map<Integer, List<ChannelHandlerContext>> getMAP() {
        return map;
    }

    public static void removeKey(Integer deviceIndex) {
       map.remove(deviceIndex);
    }

    public static void removeChannel(ChannelHandlerContext channelHandlerContextList) {
        map.entrySet().stream().filter(entry -> entry.getValue().iterator().next() == channelHandlerContextList)
                .forEach(entry -> map.remove(entry.getKey()));
    }

    public static boolean containsKey(Integer deviceIndex) {
        return map.containsKey(deviceIndex);
    }

    public static void replace(Integer deviceIndex, List<ChannelHandlerContext> channelHandlerContextList){
        map.replace(deviceIndex, channelHandlerContextList);
    }

}
