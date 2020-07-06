package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.geekbrains.entity.Picture;
import ru.geekbrains.repo.PictureRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@CommonsLog
public class PictureController {

    private final PictureRepository pictureRepository;

    @Value("${data.folder}")
    private String dataFolder;

    @Value("${data.save-to-database}")
    private boolean saveToDatabase;

    public PictureController(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @GetMapping("/picture/{pictureId}")
    public void adminDownloadProductPicture(@PathVariable("pictureId") Long pictureId,
                                            HttpServletResponse response) throws IOException {
        log.info("Picture " + pictureId);
        Optional<Picture> picture = pictureRepository.findById(pictureId);
        if (picture.isPresent()) {

            if(!saveToDatabase) {
                log.info("picture.get().getContentType() " + picture.get().getContentType() +
                        "picture.get().getLocalPath()" + picture.get().getLocalPath());
                response.setContentType(picture.get().getContentType());
                response.getOutputStream().write(FileUtils.readFileToByteArray(new File(picture.get().getLocalPath())));

            } else {
                response.setContentType(picture.get().getContentType());
                response.getOutputStream().write(picture.get().getPictureData().getData());
            }
        }
    }
}
