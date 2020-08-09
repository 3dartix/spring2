package ru.geekbrains.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.apachecommons.CommonsLog;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.converter.BrandConverter;
import ru.geekbrains.entity.Brand;
import ru.geekbrains.helpers.ExpectedEntity;
import ru.geekbrains.helpers.MapperJson;
import ru.geekbrains.repo.BrandRepository;
import ru.geekbrains.representation.BrandRepr;
import ru.geekbrains.representation.mapper.BrandMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.geekbrains.helpers.MapperJson.*;

@CommonsLog
@TestPropertySource(locations = "classpath:application-test.properties")
//включаем тестовую версия спринг mvc
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrandControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BrandRepository brandRepository;

    private ExpectedEntity expectedEntity = new ExpectedEntity();

    @Test
    @Order(1)
    public void saveBrandTest() throws Exception {
        //perform выполнить
        mvc.perform(post("/brand")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // для заполнения формы
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(BrandMapper.MAPPER.fromBrand(expectedEntity.getExpectedBrand())))
//                .param("id", "" + expectedEntity.getExpectedBrand().getId())
//                .param("name", "" + expectedEntity.getExpectedBrand().getName())
                .with(csrf()))
                .andExpect(status().is3xxRedirection()) //ожидаем статус от 300 -399 (редирект)
                .andExpect(view().name("redirect:/brand")); // редирект на такой адрес

        Optional<Brand> brand = brandRepository.findOne(Example.of(expectedEntity.getExpectedBrand()));
        assertTrue(brand.isPresent());
        assertEquals(expectedEntity.getExpectedBrand().getName(), brand.get().getName());
    }

    @Test
    @Order(2)
    @Transactional
    public void findAllTest() throws Exception {
        mvc.perform(get("/brand"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("brands", BrandMapper.MAPPER.fromBrandList(brandRepository.findAll())))
                .andExpect(view().name("all-brands")); // редирект на такой адрес
    }

    @Test
    @Order(3)
    public void createBrandTest() throws Exception {
        mvc.perform(get("/brand/new"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("brand")); // редирект на такой адрес
    }

    @Test
    @Order(4)
    @Transactional
    public void editBrandTest() throws Exception {
        mvc.perform(get("/brand/edit")
                .param("id", "1"))
                .andExpect(status().is2xxSuccessful())
//                "emailInfo", Matchers.hasProperty("email", Matchers.equalTo("currentMember@example.com")
                .andExpect(model().attribute("brand", Optional.of(new BrandRepr(1L,expectedEntity.getExpectedBrand().getName(), new ArrayList<>()))))
                .andExpect(view().name("brand")); // редирект на такой адрес
    }

//    @WithMockUser(value = "admin", password = "")
    @Test
    @Order(5)
    @Transactional
    public void deleteBrandTest() throws Exception {
        mvc.perform(delete("/brand")
                .param("id", "1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/brand")); // редирект на такой адрес

        assertNull(brandRepository.findById(1L).orElse(null));
    }

}
