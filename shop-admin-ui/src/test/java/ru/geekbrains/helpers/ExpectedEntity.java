package ru.geekbrains.helpers;

import lombok.Data;
import ru.geekbrains.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpectedEntity {
    private Product expectedProduct;
    private Comment expectedComment;
    private Category expectedCategory;
    private Brand expectedBrand;
    private User expectedUser;
    private Role expectedRole;


    public ExpectedEntity(){
        createBrand();
        createCategory();
        createComment();
        createProduct();
        createRole();
        createUser();
    }

    private void createBrand(){
        expectedBrand = new Brand();
        expectedBrand.setName("new Brand");
    }

    private void createProduct(){
        expectedProduct = new Product();
        expectedProduct.setName("New product");
        expectedProduct.setPrice(new BigDecimal(10));
        expectedProduct.setDescription("any description");
    }

    private void createCategory(){
        expectedCategory = new Category();
        expectedCategory.setName("New category");
    }

    private void createUser(){
        expectedUser = new User();
        expectedUser.setId(-1L);
        expectedUser.setEmail("test@mail.ru");
        expectedUser.setFirstName("first name");
        expectedUser.setLastName("last name");
        expectedUser.setPhone("+000000000");
    }

    private void createRole(){
        expectedRole = new Role();
        expectedRole.setName("role");
    }

    private void createComment(){
        LocalDateTime a = LocalDateTime.of(2017, 2, 13, 15, 56);
        expectedComment = new Comment(null, a, expectedUser, expectedProduct, "any comment", 2);
    }
}
