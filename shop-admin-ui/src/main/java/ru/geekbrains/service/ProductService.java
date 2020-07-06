package ru.geekbrains.service;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.entity.*;
import ru.geekbrains.exceptions.NotFoundException;
import ru.geekbrains.repo.ProductRepository;
import ru.geekbrains.representation.ProductRepr;
import ru.geekbrains.specification.ProductSpecification;
import ru.geekbrains.utils.GenerateName;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CommonsLog
public class ProductService {

    private ProductRepository productRepository;

    @Value("${data.folder}")
    private String dataFolder;

    @Value("${data.save-to-database}")
    private boolean saveToDatabase;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.productRepository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ProductRepr> filterByPrice(BigDecimal minPrice, BigDecimal maxPrice, String productName, Pageable pageable) {
        Specification<Product> specification = ProductSpecification.trueLiteral();

        if(minPrice != null) {
            specification = specification.and(ProductSpecification.priceGreaterThanEqual(minPrice));
        }

        if(maxPrice != null){
            specification = specification.and(ProductSpecification.priceLessThanEqual(maxPrice));
        }

        if(productName != null && !productName.isEmpty()){
            log.info("productName:" + productName);
            specification = specification.and(ProductSpecification.productName(productName));
        }

        return productRepository.findAll(specification, pageable).map(ProductRepr::new);
    }

    @Transactional(readOnly = true)
    public List<ProductRepr> findAll (){
        return productRepository.findAll().stream().map(ProductRepr::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProductRepr> findProductById(Long id){
        return productRepository.findProductsById(id).map(ProductRepr::new);
    }

    @Transactional
    public void save(@Valid ProductRepr productRepr) throws IOException {

        createDirectory();

        Product product = (productRepr.getId() != null) ? productRepository.findById(productRepr.getId())
                .orElseThrow(() -> new NotFoundException()) : new Product();

        product.setId(productRepr.getId());
        product.setName(productRepr.getName());
        product.setDescription(productRepr.getDescription());
        product.setPrice(productRepr.getPrice());
        product.setBrand(
                new Brand(
                        productRepr.getBrand().getId(),
                        productRepr.getBrand().getName(),
                        productRepr.getBrand().getProducts()
                )
        );
        product.setCategories(productRepr.getCategories());

        if (productRepr.getNewPictures() != null) { //если есть картинки

            log.info("Срабатывает даже тогда когда нет картинки");

            for (MultipartFile newPicture : productRepr.getNewPictures()) {
                log.info("Product "+ product.getId() +" file "+newPicture.getOriginalFilename()+" size "+newPicture.getSize());

                if (product.getPictures() == null) { //если картинок в сущности нет, создаем массив
                    product.setPictures(new ArrayList<>());
                }

                if(!newPicture.isEmpty()){
                    if(!saveToDatabase){ //если в настройках "сохнять на диск" false
                        String fileName = GenerateName.generateName(newPicture.getOriginalFilename());
                        // написать проверку на уникальность имен картинок
                        File file = new File(dataFolder, fileName);
                        FileUtils.writeByteArrayToFile(file, newPicture.getBytes());
                        product.getPictures().add(
                                new Picture(fileName, newPicture.getContentType(), file.getPath())
                        );
                    } else {
                        //заполняем массив
                        product.getPictures().add(new Picture(newPicture.getOriginalFilename(),
                                newPicture.getContentType(), new PictureData(newPicture.getBytes())));

                    }
                }
            }
        }

        log.info("save -> repo :" + product.toString());
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Optional<ProductRepr> findById(long id) {
        return productRepository.findById(id).map(ProductRepr::new);
    }

    @Transactional
    public void deleteProductByIe(Long id){
        if (!saveToDatabase) {
            deleteImageFromLocalDisk(id);
        }
        productRepository.deleteProductById(id);
    }

    public void deleteCategory(Category category){
        productRepository.findAll().forEach(c -> {
            c.getCategories().remove(category);
        });
    }

    public void createDirectory(){
        log.info("create folder: " +dataFolder);
        //проверяем существует ли каталог
        Path path = Paths.get(dataFolder);
        log.info("absolute path: " + new File(dataFolder).getAbsolutePath());
        try {
            if (!Files.exists(path)) {
                //создаем каталог
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteImageFromLocalDisk(Long id){
        productRepository.findProductsById(id).get()
                .getPictures().forEach(picture -> {
                    File file = new File(picture.getLocalPath());
                    if (file.delete()){
                        log.info(picture.getName() + " was removed");
                    } else {
                        log.info(picture.getName() + " not found in directory");
                    }
                });
    }
}
