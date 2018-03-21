package cc.corly.springboot.demo;

import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author mengshuang
 * @date 2018/3/16
 */
public class TestMain {
    @Test
    public void testHashSet() {
        Set<String> linkedHashSet = new LinkedHashSet<>();
        Set<String> hashSet = new HashSet<>();
        for (int i = 0; i < 10000000; i++) {
            String a = String.valueOf(i);
            linkedHashSet.add(a);
            hashSet.add(a);
        }
        String b = "500000";
        String c = "800000";
        String d = "1000000000";
        long l = 0;
        long h = 0;
        long start = System.currentTimeMillis();
        linkedHashSet.contains(b);
        l = System.currentTimeMillis()-start;
        start = System.currentTimeMillis();
        hashSet.contains(b);
        h = System.currentTimeMillis()-start;
        System.out.println(b+",linkedHashSet:"+l+", hashSet:"+h);

        b = c;
        start = System.currentTimeMillis();
        linkedHashSet.contains(b);
        l = System.currentTimeMillis()-start;
        start = System.currentTimeMillis();
        hashSet.contains(b);
        h = System.currentTimeMillis()-start;
        System.out.println(b+",linkedHashSet:"+l+", hashSet:"+h);

        b = d;
        start = System.currentTimeMillis();
        linkedHashSet.contains(b);
        l = System.currentTimeMillis()-start;
        start = System.currentTimeMillis();
        hashSet.contains(b);
        h = System.currentTimeMillis()-start;
        System.out.println(b+",linkedHashSet:"+l+", hashSet:"+h);

    }
}
