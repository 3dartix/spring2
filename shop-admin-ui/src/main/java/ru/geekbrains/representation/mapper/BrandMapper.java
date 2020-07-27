package ru.geekbrains.representation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.geekbrains.entity.Brand;
import ru.geekbrains.representation.BrandRepr;

import java.util.List;

@Mapper
public interface BrandMapper {
    //ссылка на этот маппер
    BrandMapper MAPPER = Mappers.getMapper(BrandMapper.class);

    Brand toBrand (BrandRepr brandRepr);

    BrandRepr fromBrand (Brand brand);

    List<Brand> toBrandList(List<BrandRepr> brandReprList);

    List<BrandRepr> FromBrandList(List<Brand> brandList);
}
