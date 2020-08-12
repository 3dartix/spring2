package ru.geekbrains.representation.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.geekbrains.entity.Product;
import ru.geekbrains.representation.ProductRepr;

import java.util.List;

@Mapper (uses = {BrandMapper.class, PictureMapper.class})
public interface ProductMapper {
    //ссылка на этот маппер
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    Product toProduct (ProductRepr productRepr);

    ProductRepr fromProduct (Product product);

    List<Product> toProductList(List<ProductRepr> productReprList);

    List<ProductRepr> FromProductList(List<Product> productList);
}
