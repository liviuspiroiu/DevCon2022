package com.example.devcon;

import com.example.devcon.common.dto.CategoryDto;
import com.example.devcon.common.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest(classes = FrontendApplication.class)
public class IndexControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private String accessToken;


    @BeforeEach
    public void before() throws Exception {
        accessToken = Oauth2Utils.obtainAccessToken("liviu.spiroiu@cognyte.com", "pass");

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", 123);
        OidcIdToken idToken = new OidcIdToken(accessToken, Instant.now(),
                Instant.now().plusSeconds(60), claims);
        SecurityContextHolder.getContext().setAuthentication(Oauth2Utils.getAuthentication(idToken));
        SecurityContextHolderAwareRequestFilter authInjector = new SecurityContextHolderAwareRequestFilter();
        authInjector.afterPropertiesSet();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void list() throws Exception {

        MvcResult result = this.mockMvc.perform(get("/")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertEquals("index", modelAndView.getViewName());
        assertEquals(new ProductDto(), modelAndView.getModel().get("product"));

        assertEquals(Arrays.asList(
                new ProductDto(
                        2L,
                        "iPhone 12",
                        "iPhone 12 Description",
                        BigDecimal.valueOf(60000, 2),
                        1L
                ),
                new ProductDto(
                        3L,
                        "iPhone 13",
                        "iPhone 13 Description",
                        BigDecimal.valueOf(70000, 2),
                        1L
                ),
                new ProductDto(
                        202L,
                        "iPhone 11",
                        "iPhone 11 Description",
                        BigDecimal.valueOf(45000, 2),
                        1L
                )
        ), modelAndView.getModel().get("products"));

        assertEquals(Arrays.asList(
                new CategoryDto(
                        1L,
                        "Phones",
                        "Phones",
                        3),
                new CategoryDto(
                        2L,
                        "Tablets",
                        "Tablets",
                        0)
        ), modelAndView.getModel().get("categories"));

    }


}