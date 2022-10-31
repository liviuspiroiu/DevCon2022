package com.example.devcon;


import com.example.devcon.model.domain.enumeration.OrderStatus;
import com.example.devcon.model.repository.OrderRepository;
import com.example.devcon.model.service.OrderService;
import com.example.devcon.web.dto.AddressDto;
import com.example.devcon.web.dto.OrderDto;
import com.example.devcon.web.dto.OrderItemDto;
import com.example.devcon.web.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Collections;

import static com.example.devcon.model.domain.enumeration.ProductStatus.AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest(classes = DevconApplication.class)
@AutoConfigureMockMvc
@Sql({"/data/users.sql", "/data/categories.sql", "/data/products.sql", "/data/orders.sql"})
@Transactional
public class OrderControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private OrderRepository orderRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @WithUserDetails("liviu.spiroiu@cognyte.com")
    public void list() throws Exception {

        MvcResult result = this.mockMvc
                .perform(get("/orders/"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertEquals("/orders/list", modelAndView.getViewName());

        assertEquals(Collections.singletonList(new OrderDto(
                1L,
                BigDecimal.valueOf(60000, 2),
                OrderStatus.SENT.name(),
                null,
                null,
                new AddressDto(
                        "Dionisie Lupu 66",
                        "Sector 1",
                        "Bucuresti",
                        "030167",
                        "RO"
                ),
                Collections.singleton(new OrderItemDto(
                        1L,
                        1L,
                        new ProductDto(
                                1L,
                                "iPhone 13",
                                "iPhone 13 Description",
                                BigDecimal.valueOf(60000, 2),
                                AVAILABLE.name(),
                                0,
                                1L
                        ),
                        1L
                )),
                "Liviu Spiroiu"
        )), modelAndView.getModel().get("orders"));
    }

    @Test
    @WithUserDetails("liviu.spiroiu@cognyte.com")
    public void cart() throws Exception {

        MvcResult result = this.mockMvc
                .perform(get("/orders/cart"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertEquals("/orders/cart", modelAndView.getViewName());
        assertEquals(new OrderDto(
                2L,
                BigDecimal.valueOf(180000, 2),
                OrderStatus.NEW.name(),
                null,
                null,
                new AddressDto(
                        "Dionisie Lupu 66",
                        "Sector 1",
                        "Bucuresti",
                        "030167",
                        "RO"
                ),
                Collections.singleton(new OrderItemDto(
                        2L,
                        3L,
                        new ProductDto(
                                1L,
                                "iPhone 13",
                                "iPhone 13 Description",
                                BigDecimal.valueOf(60000, 2),
                                AVAILABLE.name(),
                                0,
                                1L
                        ),
                        2L
                )),
                "Liviu Spiroiu"
        ), modelAndView.getModel().get("order"));
    }

    @Test
    @WithUserDetails("liviu.spiroiu@cognyte.com")
    public void checkout() throws Exception {

        MvcResult result = this.mockMvc
                .perform(get("/orders/checkout/2"))
                .andExpect(status().isFound())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertEquals("redirect:/orders/", modelAndView.getViewName());

        final OrderDto order = OrderService.mapToDto(orderRepository.findById(2L).orElseThrow());

        assertEquals(new OrderDto(
                2L,
                BigDecimal.valueOf(180000, 2),
                OrderStatus.SENT.name(),
                null,
                1L,
                new AddressDto(
                        "Dionisie Lupu 66",
                        "Sector 1",
                        "Bucuresti",
                        "030167",
                        "RO"
                ),
                Collections.singleton(new OrderItemDto(
                        2L,
                        3L,
                        new ProductDto(
                                1L,
                                "iPhone 13",
                                "iPhone 13 Description",
                                BigDecimal.valueOf(60000, 2),
                                AVAILABLE.name(),
                                0,
                                1L
                        ),
                        2L
                )),
                "Liviu Spiroiu"
        ), order);
    }

    @Test
    @WithUserDetails("liviu.spiroiu@cognyte.com")
    public void increaseQuantity() throws Exception {

        MvcResult result = this.mockMvc
                .perform(get("/orders/increaseQuantity/2/2"))
                .andExpect(status().isFound())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertEquals("redirect:/orders/cart", modelAndView.getViewName());

        final OrderDto order = OrderService.mapToDto(orderRepository.findById(2L).orElseThrow());

        assertEquals(new OrderDto(
                2L,
                BigDecimal.valueOf(240000, 2),
                OrderStatus.NEW.name(),
                null,
                null,
                new AddressDto(
                        "Dionisie Lupu 66",
                        "Sector 1",
                        "Bucuresti",
                        "030167",
                        "RO"
                ),
                Collections.singleton(new OrderItemDto(
                        2L,
                        4L,
                        new ProductDto(
                                1L,
                                "iPhone 13",
                                "iPhone 13 Description",
                                BigDecimal.valueOf(60000, 2),
                                AVAILABLE.name(),
                                0,
                                1L
                        ),
                        2L
                )),
                "Liviu Spiroiu"
        ), order);
    }

    @Test
    @WithUserDetails("liviu.spiroiu@cognyte.com")
    public void decreaseQuantity() throws Exception {

        MvcResult result = this.mockMvc
                .perform(get("/orders/decreaseQuantity/2/2"))
                .andExpect(status().isFound())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertEquals("redirect:/orders/cart", modelAndView.getViewName());

        final OrderDto order = OrderService.mapToDto(orderRepository.findById(2L).orElseThrow());

        assertEquals(new OrderDto(
                2L,
                BigDecimal.valueOf(120000, 2),
                OrderStatus.NEW.name(),
                null,
                null,
                new AddressDto(
                        "Dionisie Lupu 66",
                        "Sector 1",
                        "Bucuresti",
                        "030167",
                        "RO"
                ),
                Collections.singleton(new OrderItemDto(
                        2L,
                        2L,
                        new ProductDto(
                                1L,
                                "iPhone 13",
                                "iPhone 13 Description",
                                BigDecimal.valueOf(60000, 2),
                                AVAILABLE.name(),
                                0,
                                1L
                        ),
                        2L
                )),
                "Liviu Spiroiu"
        ), order);
    }

    @Test
    @WithUserDetails("liviu.spiroiu@cognyte.com")
    public void deleteItem() throws Exception {

        MvcResult result = this.mockMvc
                .perform(get("/orders/deleteItem/2"))
                .andExpect(status().isFound())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertEquals("redirect:/orders/cart", modelAndView.getViewName());

        final OrderDto order = OrderService.mapToDto(orderRepository.findById(2L).orElseThrow());

        assertEquals(new OrderDto(
                2L,
                BigDecimal.valueOf(0, 2),
                OrderStatus.NEW.name(),
                null,
                null,
                new AddressDto(
                        "Dionisie Lupu 66",
                        "Sector 1",
                        "Bucuresti",
                        "030167",
                        "RO"
                ),
                Collections.emptySet(),
                "Liviu Spiroiu"
        ), order);
    }

}