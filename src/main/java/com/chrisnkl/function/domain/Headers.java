package com.chrisnkl.function.domain;

import lombok.Getter;

@Getter
public enum Headers {

    FILE_NAME("File-Name"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_DISPOSITION("Content-Disposition");

    private final String name;

    Headers(String name) {
        this.name = name;
    }

}
