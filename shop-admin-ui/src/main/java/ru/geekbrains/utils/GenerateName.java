package ru.geekbrains.utils;

import org.apache.commons.lang.RandomStringUtils;

public  class GenerateName {
    public static String generateName(String originalName){
        int index = originalName.lastIndexOf('.');
        originalName = originalName.substring(index);
        return RandomStringUtils.randomAlphanumeric(30).toUpperCase() + originalName;
    }
}
