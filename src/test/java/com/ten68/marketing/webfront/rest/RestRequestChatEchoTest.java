package com.ten68.marketing.webfront.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestRequestChatEcho.class)
class RestRequestChatEchoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogicChatEcho logicChatEcho;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void echo_returnsHttp200() throws Exception {
        mockMvc.perform(post("/v1/api/echo")
                        .content("hello")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

    @Test
    void echo_returnsApplicationJsonContentType() throws Exception {
        mockMvc.perform(post("/v1/api/echo")
                        .content("hello")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void echo_returnsValidJsonContainingInput() throws Exception {
        String input = "hello world";
        MvcResult result = mockMvc.perform(post("/v1/api/echo")
                        .content(input)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        String encodedInput = URLEncoder.encode(input, StandardCharsets.UTF_8);

        assertThat(responseBody).contains(encodedInput);
    }
}
