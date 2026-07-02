package com.chrisnkl.function.domain;

import lombok.Getter;

@Getter
public enum Headers {

    FILE_NAME("file-name"),
    CONTENT_TYPE("content-type"),
    CONTENT_DISPOSITION("content-disposition");

    private final String name;

    Headers(String name) {
        this.name = name;
    }

}
