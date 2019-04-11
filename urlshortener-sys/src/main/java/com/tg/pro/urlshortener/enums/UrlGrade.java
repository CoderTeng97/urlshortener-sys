package com.tg.pro.urlshortener.enums;

public enum UrlGrade {
    LOW(Long.valueOf(10)),
    MIDDLE(Long.valueOf(15)),
    HIGH(Long.valueOf(20));

    private Long value;

    UrlGrade(Long value) {
        this.value = value;
    }

    public Long value(){
        return this.value = value;
    }
}
