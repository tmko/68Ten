package com.ten68.marketing.webfront.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RESTController.class)
@TestPropertySource(properties = {
    "spring.forward.url=http://localhost",
    "spring.forward.port=1"
})

class RESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void echoRequest_ShouldReturnEchoedBody() throws Exception {
        mockMvc.perform(
                post("/v1/api/echo")
                .contentType("application/json")
                .content("Hello")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Echo").value("Hello"));

    }

    //@Test
    void forwardRequest_WhenDownstreamUnreachable_ShouldReturn500() throws Exception {
        mockMvc.perform(
                post("/v1/api/chat")
                .contentType("application/json")
                .content("test")
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //        .andExpect(content().string(eq("{'Echo':'test'}")))
        ;
    }
}
