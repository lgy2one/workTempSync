package com.qg.exclusiveplug.web;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuavaTest {
    public static void main(String[] args) {
        Multimap<String, Integer> multimap = ArrayListMultimap.create();
        multimap.put("hua", 1);
        multimap.put("hua", 2);
        System.out.println(multimap.toString());

        Multiset<Integer> multiset = HashMultiset.create();
        multiset.add(1);
        multiset.add(1);
        System.out.println(multiset.toString());

        BiMap<String, Integer> biMap = HashBiMap.create();
        biMap.put("hua", 1);
        biMap.put("chen", 2);
        System.out.println(biMap);

        Table<String, String, Integer> table = HashBasedTable.create();
        table.put("hua", "chen", 1);
        System.out.println(table.toString());

        ImmutableList<Integer> immutableList = ImmutableList.of(1, 2, 3);
        ImmutableMap<String, Integer> immutableMap = ImmutableMap.of("1", 2, "3", 4);
        ImmutableSet<String> immutableSet = ImmutableSet.of("a", "b");
        System.out.println(immutableList);
        System.out.println(immutableMap);
        System.out.println(immutableSet);

        List<String> stringList = new ArrayList<>();
        stringList.add("a");
        stringList.add("b");
        stringList.add("c");
        System.out.println(stringList);
        System.out.println(Joiner.on("-").join(stringList));

        Map<String, Integer> map = Maps.newHashMap();
        map.put("xiaoming", 12);
        map.put("xiaohong", 13);
        System.out.println(map);
        System.out.println(Joiner.on(",").withKeyValueSeparator("=").join(map));

        String str = "1-2-3-4-5-6";
        System.out.println(Splitter.on("-").splitToList(str));
        str = "1-2-3-4-  5-  6   ";
        System.out.println(Splitter.on("-").omitEmptyStrings().trimResults().splitToList(str));
    }
}
