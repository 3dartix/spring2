package ru.geekbrains.entity;

import com.opencsv.bean.AbstractCsvConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.Data;

@Data
public class BrandCsv {
//    @CsvBindByName
    @CsvBindByPosition(position = 0)
    private String name;

//    @CsvBindByPosition(position = 1)
//    private String description;
//
////    @CsvBindByPosition(position = 2)
//    @CsvCustomBindByPosition(position = 2, converter = StringToFloatConverter.class)
//    private Float price;

    @Override
    public String toString() {
        return "ProductCsv{" +
                "name='" + name + '\'' +
                '}';
    }

}
