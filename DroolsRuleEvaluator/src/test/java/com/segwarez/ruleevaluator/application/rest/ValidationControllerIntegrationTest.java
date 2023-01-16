package com.segwarez.ruleevaluator.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segwarez.ruleevaluator.domain.model.Item;
import com.segwarez.ruleevaluator.domain.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ValidationControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void validateOrder_whenEmptyRequest_thenBadRequest400() throws Exception {
        //when
        this.mockMvc.perform(post("/validate/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void validateOrder_whenItemsEmpty_thenValidationErrorsEmpty() throws Exception {
        //when
        this.mockMvc.perform(post("/validate/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Order())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andReturn();
    }

    @Test
    void validateOrder_whenValidItem_thenValidationErrorsEmpty() throws Exception {
        //given
        List<Item> validItem = List.of(Item.builder().name("C").capacity(5).build());
        //when
        this.mockMvc.perform(post("/validate/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Order(validItem))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andReturn();
    }

    @Test
    void validateOrder_whenCapacitySumOver10_thenCapacitySumError() throws Exception {
        //given
        List<Item> validItem = List.of(
                Item.builder().name("A").capacity(4).build(),
                Item.builder().name("B").capacity(2).build(),
                Item.builder().name("C").capacity(5).build()
        );
        //when
        this.mockMvc.perform(post("/validate/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Order(validItem))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationErrors")
                        .value("BREACHED - capacitySum"))
                .andReturn();
    }

    @Test
    void validateOrder_whenNameCAndCapacityOver5_thenNameAndCapacityError() throws Exception {
        //given
        List<Item> validItem = List.of(
                Item.builder().name("A").capacity(2).build(),
                Item.builder().name("B").capacity(2).build(),
                Item.builder().name("C").capacity(6).build()
        );
        //when
        this.mockMvc.perform(post("/validate/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Order(validItem))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationErrors")
                        .value("BREACHED - nameAndCapacity")).
                andReturn();
    }
    @Test
    void validateOrder_whenOverallCapacityAndName_thenBothErrors() throws Exception {
        //given
        List<Item> validItem = List.of(
                Item.builder().name("A").capacity(5).build(),
                Item.builder().name("B").capacity(2).build(),
                Item.builder().name("C").capacity(6).build()
        );
        //when
        this.mockMvc.perform(post("/validate/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Order(validItem))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationErrors")
                        .value(containsInAnyOrder("BREACHED - capacitySum", "BREACHED - nameAndCapacity")))
                .andReturn();
    }
}