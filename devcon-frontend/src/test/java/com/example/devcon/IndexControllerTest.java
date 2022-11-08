//package com.example.devcon;
//
//import com.example.devcon.common.dto.CategoryDto;
//import com.example.devcon.common.dto.ProductDto;
//import com.example.devcon.common.enums.ProductStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@WebAppConfiguration
//@SpringBootTest(classes = FrontendApplication.class)
//@Sql({"/data/categories.sql", "/data/products.sql"})
//@Transactional
//public class IndexControllerTest {
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
//
//    @Test
//    public void list() throws Exception {
//
//        MvcResult result = this.mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        ModelAndView modelAndView = result.getModelAndView();
//        assertEquals("index", modelAndView.getViewName());
//        assertEquals(new ProductDto(), modelAndView.getModel().get("product"));
//
//        assertEquals(Collections.singletonList(
//                new ProductDto(
//                        1L,
//                        "iPhone 13",
//                        "iPhone 13 Description",
//                        BigDecimal.valueOf(60000, 2),
//                        ProductStatus.AVAILABLE.name(),
//                        0,
//                        1L
//                )
//        ), modelAndView.getModel().get("products"));
//
//        assertEquals(Collections.singletonList(
//                new CategoryDto(
//                        1L,
//                        "Phones",
//                        "Phones",
//                        1)
//        ), modelAndView.getModel().get("categories"));
//
//    }
//
//
//}