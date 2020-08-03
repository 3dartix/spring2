package ru.geekbrains.entity;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCsv {

    @CsvBindByPosition(position = 0)
    private String name;

    @CsvBindByPosition(position = 2)
    private BigDecimal price;

    @CsvBindByPosition(position = 1)
    private String description;

//    @CsvCustomBindByPosition(position = 3, converter = TextToListCategories.class)
    @CsvBindByPosition(position = 3)
    private String categories;

    @CsvBindByPosition(position = 4)
    private String brand;

    @Override
    public String toString() {
        return "ProductCsv{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", categories=" + categories +
                ", brand='" + brand + '\'' +
                '}';
    }
}
