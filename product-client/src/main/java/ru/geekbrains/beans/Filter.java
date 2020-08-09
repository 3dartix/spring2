package ru.geekbrains.beans;

import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Data
@CommonsLog
public class Filter {
    private String category;
    private Optional<Integer> minPrice;
    private Optional<Integer> maxPrice;
    private List<String> brands;
    private Optional<String> name;

    @PostConstruct
    public void init() {
        minPrice = Optional.of(10);
        maxPrice = Optional.of(5000);
        brands = new ArrayList<>();
        name = Optional.of("");
        log.info("filter init");
    }

    public boolean containsBrand(String brand){
        if(this.brands != null){
            return this.brands.equals(brand);
        }
        return false;
    }

    public boolean containsCategory(String category){
        if(this.category == category){
            return true;
        }
        return false;
    }



}
