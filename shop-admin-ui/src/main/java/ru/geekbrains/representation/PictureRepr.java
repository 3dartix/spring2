package ru.geekbrains.representation;


import lombok.Data;
import ru.geekbrains.entity.Picture;
import ru.geekbrains.entity.PictureData;

import java.io.Serializable;

@Data
public class PictureRepr implements Serializable {

    private Long id;

    private String name;

    private String contentType;

    private String localPath;

    private PictureData pictureData;

    public PictureRepr(Picture picture) {
        this.id = picture.getId();
        this.name = picture.getName();
        this.contentType = picture.getContentType();
        this.localPath = picture.getLocalPath();
    }

    public PictureRepr(Long id, String name, String contentType, String localPath) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
        this.localPath = localPath;
    }

    public PictureRepr() {
    }
}
