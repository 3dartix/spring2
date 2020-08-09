package ru.geekbrains.helpers;

import lombok.Data;
import ru.geekbrains.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExpectedEntity {
    private Product expectedProduct;
    private Comment expectedComment;
    private Category expectedCategory;
    private Brand expectedBrand;
    private User expectedUser;
    private Role expectedRole;


    public ExpectedEntity(){
        expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setName("New product");
        expectedProduct.setPrice(new BigDecimal(10));
        expectedProduct.setDescription("any description");
        // создаем категорию
        expectedCategory = new Category();
        expectedCategory.setId(1L);
        expectedCategory.setName("New category");

        List<Product> pList = new ArrayList<>();
        pList.add(expectedProduct);
        expectedCategory.setProducts(pList);

        List<Category> catList = new ArrayList<>();
        catList.add(expectedCategory);
        expectedProduct.setCategories(catList);
        // создаем брэнд
        expectedBrand = new Brand();
        expectedBrand.setId(1L);
        expectedBrand.setName("new Brand");
        expectedBrand.setProducts(pList);
        // создаем картинку

        //создаем юзера
        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail("test@mail.ru");
        expectedUser.setFirstName("first name");
        expectedUser.setLastName("last name");
        expectedUser.setPhone("+000000000");
        // создаем роли
        expectedRole = new Role();
        expectedRole.setId(1L);
        expectedRole.setName("role");

        List<Role> roleList = new ArrayList<>();
        roleList.add(expectedRole);

        expectedUser.setRoles(roleList);

        List<User> userList = new ArrayList<>();
        userList.add(expectedUser);

        expectedRole.setUsers(userList);

        // создаем комментарий
        List<Comment> comList = new ArrayList<>();
        LocalDateTime a = LocalDateTime.of(2017, 2, 13, 15, 56);
        expectedComment = new Comment(1L, a, expectedUser, expectedProduct, "any comment", 2);
        comList.add(expectedComment);

        expectedProduct.setComments(comList);
    }


}
