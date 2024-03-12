package com.drawer.utils;

public class CustomPair<K, V> {
    private K key;
    private V value;

    public CustomPair(K key, V value) {
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

    public static <K, V> CustomPair<K, V> of(K key, V value) {
        return new CustomPair<>(key, value);
    }
}
