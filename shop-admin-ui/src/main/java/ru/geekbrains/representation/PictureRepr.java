package ru.geekbrains.representation;


import lombok.Data;
import ru.geekbrains.entity.Picture;
import java.io.Serializable;

@Data
public class PictureRepr implements Serializable {

    private Long id;

    private String name;

    private String contentType;

    public PictureRepr(Picture picture) {
        this.id = picture.getId();
        this.name = picture.getName();
        this.contentType = picture.getContentType();
    }
}
