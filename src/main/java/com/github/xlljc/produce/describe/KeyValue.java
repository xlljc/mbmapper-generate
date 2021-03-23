package com.github.xlljc.produce.describe;

import java.util.Objects;

/**
 * 键值对映射
 */
public class KeyValue<K,V> {

    private K key;
    private V value;

    public KeyValue() {
    }

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyValue<?, ?> keyValue = (KeyValue<?, ?>) o;
        return Objects.equals(key, keyValue.key) &&
                Objects.equals(value, keyValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    /**
     * 将字符串装成键值对格式, 用 : 分开, 使用 \: 转义
     */
    public static KeyValue<String,String> parseKeyValue(String str) {
        String str2 = str.replaceAll("^\\{", "").replaceAll("}$", "");
        String[] arr = str2.split("(?<!\\\\) *: *");
        if (arr.length != 2) {
            //throw new
        }
        return new KeyValue<>(arr[0], arr[1]);
    }
}
