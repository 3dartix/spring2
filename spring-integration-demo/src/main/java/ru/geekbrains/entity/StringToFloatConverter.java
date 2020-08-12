package ru.geekbrains.entity;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class StringToFloatConverter extends AbstractBeanField<Float> {
    @Override
    protected Float convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            //System.out.printf("############ " + s);
            return Float.valueOf(s);
        } catch (Exception e){
            return 0f;
        }
    }
}
