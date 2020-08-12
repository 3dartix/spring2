package ru.geekbrains.conroller;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.entity.Picture;
import ru.geekbrains.repo.PictureRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@CommonsLog
@Controller
public class PictureController {

    private PictureRepository pictureRepository;

    @Value("${data.save-to-database}")
    private boolean saveToDatabase;

    @Autowired
    public PictureController(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @GetMapping("/picture/{pictureId}")
    public HttpServletResponse adminDownloadProductPicture(@PathVariable("pictureId") Long pictureId, HttpServletResponse response) throws IOException {
        log.info("Picture " + pictureId);
        Optional<Picture> picture = pictureRepository.findById(pictureId);
        log.info(picture);
        if (picture.isPresent()) {

            if(!saveToDatabase) {
                log.info("Load image from disk: " + "picture.get().getContentType() " + picture.get().getContentType() +
                        "picture.get().getLocalPath()" + picture.get().getLocalPath());
                response.setContentType(picture.get().getContentType());
                response.getOutputStream().write(FileUtils.readFileToByteArray(new File(picture.get().getLocalPath())));
                log.info(new File(picture.get().getLocalPath()).length());
            } else {
                log.info("Load image from database: " + picture.get().getLocalPath());
                response.setContentType(picture.get().getContentType());
                response.getOutputStream().write(picture.get().getPictureData().getData());
            }
        }
        return response;
    }


}
