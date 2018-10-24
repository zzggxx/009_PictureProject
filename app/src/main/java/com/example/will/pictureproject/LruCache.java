package com.example.will.pictureproject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * 讲解类
 *
 * @param <K>
 * @param <V>
 */
public class LruCache<K, V> {
    /**
     * LinkedHashMap 提供特殊的构造方法来创建链接哈希映射，该哈希映射的迭代顺序就是最后访问其条目的顺序，
     * 从近期访问最少到近期访问最多的顺序（访问顺序）。 这种映射很适合构建 LRU 缓存。
     */
    private final LinkedHashMap<K, V> map;

    /**
     * 当前缓存的个数
     */
    private int size;
    /**
     * 缓存的最大个数
     */
    private int maxSize;
    /**
     * 添加到缓存中的个数
     */
    private int putCount;
    /**
     * 创建的个数
     */
    private int createCount;
    /**
     * 被移除的个数
     */
    private int evictionCount;
    /**
     * 命中个数
     */
    private int hitCount;
    /**
     * 丢失个数
     */
    private int missCount;

    /**
     * 最大缓存的大小，一般定缓存的大小
     */
    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        // 定义一个LinkedHashMa,后边增加值才是排序算法的要点.
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }

    /**
     * Returns the value for {@code key} if it exists in the cache or can be
     * created by {@code #create}. If a value was returned, it is moved to the
     * head of the queue. This returns null if a value is not cached and cannot
     * be created. 根据key，获取value值，并将本次返回的value放置到对列的最上层，来表示访问的顺序（最近访问）
     */
    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V mapValue;
        synchronized (this) {
            // 从集合中获取value
            mapValue = map.get(key);
            if (mapValue != null) {
                // 如果不为空，对hitCount执行自增操作
                hitCount++;
                return mapValue;
            }
            // 如果为空，就对missCount执行自增操作
            missCount++;
        }

        /*
         * 尝试根据key去创建这样一个value
         */

        V createdValue = create(key);
        // 创建value失败，就返回一个null
        if (createdValue == null) {
            return null;
        }
        // 如果成功
        synchronized (this) {
            // 将createCount进行自增操作
            createCount++;
            // 并将创建的这个值存储到集合中去，并去获取该key之前所映射的值（如果之前没有映射，返回null）
            mapValue = map.put(key, createdValue);
            // 如果之前该key有对应的value值
            if (mapValue != null) {
                // There was a conflict so undo that last put
                // 为了避免冲突，就重新将之前的值再存进去来覆盖我们自己创造的值（这里是考虑到了create方法是在其他线程中操作）
                map.put(key, mapValue);
            } else {
                // 如果之前确实没有一个value（这回终于放心了，可以使用我们自己创造的值了，就将对缓存大小做操作）
                // 缓存的大小改变
                size += safeSizeOf(key, createdValue);
            }
        }
        // 这里没有移除，只是改变了位置
        if (mapValue != null) {
            entryRemoved(false, key, createdValue, mapValue);
            return mapValue;
        } else {
            // 在最后判断缓存是否超过了设定的最大值
            trimToSize(maxSize);
            return createdValue;
        }
    }

    /**
     * Caches {@code value} for {@code key}. The value is moved to the head of
     * the queue.
     *
     * @return the previous value mapped by {@code key}.
     */
    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        V previous;
        synchronized (this) {
            // 将添加的次数进行自增
            putCount++;
            // 改变缓存大小
            size += safeSizeOf(key, value);
            // 将本次添加，并获取之前的和key映射的value（也就是previous的value）
            previous = map.put(key, value);
            // 如果之前key是对应着value的，那就将之前value所占的一个缓存大小移掉（相当于一个车上某一个位子上原先有120斤重的人，现在上来一个150斤的，就应该+150
            // -120）
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }
        // 看一下当前大小是否超过总的大小
        trimToSize(maxSize);
        return previous;
    }

    /**
     * Remove the eldest entries until the total of remaining entries is at or
     * below the requested size. 如果我们定义的size>maxSize 就移除一个最不常用的数据（或者是
     * 最老的数据），直到我们定义的size<maxSize
     *
     * @param maxSize the maximum size of the cache before returning. May be -1 to
     *                evict even 0-sized elements.
     */
    public void trimToSize(int maxSize) {
        /**
         * 这里是一个无限循环的操作，如果我们的size一直大于maxSize，就一直执行这个方法 比如车的载重是400斤，之前有四个人，分别是
         * 100斤，99斤，98斤，97斤，现在上来一个人是300斤，那就得执行移除三个人的方法，才能满足不超重
         */
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }
                // 如果本来没超过，就中断循环
                if (size <= maxSize || map.isEmpty()) {
                    break;
                }
                // 移除最少使用的缓存
//              Set<Entry<K,V>> entrySet = map.entrySet();
                Map.Entry<K, V> toEvict = map.entrySet().iterator().next();
                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                // 将当前的size减掉所移除的size
                size -= safeSizeOf(key, value);
                evictionCount++;
            }

            entryRemoved(true, key, value, null);
        }
    }

    /**
     * Removes the entry for {@code key} if it exists.
     * 用户手动移除
     *
     * @return the previous value mapped by {@code key}.
     */
    public final V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V previous;
        synchronized (this) {
            previous = map.remove(key);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }

        return previous;
    }

    /**
     * //这里用户可以重写它，实现数据和内存回收操作，默认是没有任何实现
     */
    protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
    }

    /**
     * Returns the size of the entry for {@code key} and {@code value} in
     * user-defined units. The default implementation returns 1 so that size is
     * the number of entries and max size is the maximum number of entries.
     * //这里跟我们实例化 LruCache 的 maxSize 要对应起来，如果 maxSize在初始定义的时候是定义的个数 这里就是 return
     * 1，如果是内存的大小，如果5M，这是应该是每个缓存 value 的 size 大小，如果是 Bitmap，这应该是
     * bitmap.getByteCount(); 所以这个方法一般会由用户进行重写
     */
    protected int sizeOf(K key, V value) {
        return 1;
    }

    /**
     * Clear the cache
     * 清除所有缓存
     */
    public final void evictAll() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }


    /**
     * Returns a copy of the current contents of the cache, ordered from least
     * recently accessed to most recently accessed. 返回缓存的一个备份，从最不常用到最常用进行排序
     */
    public synchronized final Map<K, V> snapshot() {
        return new LinkedHashMap<K, V>(map);
    }
}
