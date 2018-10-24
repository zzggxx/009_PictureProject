package com.example.will.pictureproject;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /**
     * 排序验证
     */
    @Test
    public void textLinkHashMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(0, 0.75f, true);
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        map.put("d", "4");
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> stringMap : entries) {
            System.out.println(stringMap.getKey() + "..." + stringMap.getValue());
        }
        System.out.println("---------------------------------");

        map.put("b", "44");
        for (Map.Entry<String, String> stringMap : entries) {
            System.out.println(stringMap.getKey() + "..." + stringMap.getValue());
        }
        System.out.println("---------------------------------");

        map.get("a");
        for (Map.Entry<String, String> stringMap : entries) {
            System.out.println(stringMap.getKey() + "..." + stringMap.getValue());
        }
    }
}