package com.codekoi.koi;

public enum KoiType {
    FISHBOWL("어항", 0),
    STREAM("시냇물", 3000),
    RIVER("강물", 4000),
    SEA("바다", 5000)
    ;

    public String name;
    public int price;

    KoiType(String name, int price) {
        this.name = name;
        this.price = price;
    }
}