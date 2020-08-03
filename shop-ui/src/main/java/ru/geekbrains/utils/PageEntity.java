package ru.geekbrains.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import ru.geekbrains.entity.Product;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageEntity {
    private Page<Product> page;
}
