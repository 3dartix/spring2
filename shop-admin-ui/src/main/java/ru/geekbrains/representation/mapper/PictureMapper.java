package ru.geekbrains.representation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.geekbrains.entity.Picture;
import ru.geekbrains.representation.PictureRepr;

import java.util.List;

@Mapper
public interface PictureMapper {
    //ссылка на этот маппер
    PictureMapper MAPPER = Mappers.getMapper(PictureMapper.class);

    Picture toPicture (PictureRepr pictureRepr);

    PictureRepr fromPicture (Picture picture);

    List<Picture> toPictureList(List<PictureRepr> pictureReprList);

    List<PictureRepr> FromPictureList(List<Picture> pictureList);
}
