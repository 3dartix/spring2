package ru.geekbrains.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "pictures")
public class Picture implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "local_path")
    private String localPath;

    @OneToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name="picture_data_id")
    private PictureData pictureData;

    @ManyToOne
    private Product product;

    public Picture(String name, String contentType, PictureData pictureData) {
        this.name = name;
        this.contentType = contentType;
        this.pictureData = pictureData;
    }

    public Picture(String name, String contentType, String localPath) {
        this.name = name;
        this.contentType = contentType;
        this.localPath = localPath;
    }
}
