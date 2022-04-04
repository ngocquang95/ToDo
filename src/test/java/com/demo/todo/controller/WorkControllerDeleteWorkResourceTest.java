package com.demo.todo.controller;

import com.demo.todo.service.WorkService;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WorkControllerDeleteWorkResourceTest {

    /**
     * Collection Resource URI
     */
    private static final String COLLECTION_RESOURCE_URI = "/works/{id}";

    /**
     * MockMvc
     */
    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void test(){
    }

    /**
     * Work service
     */
    @MockBean
    WorkService workService;

    @ParameterizedTest(name = "status = 204, Check Parameter Validation, id={0}")
    @MethodSource("parameter_test_204")
    public void test_204(String id) throws Exception {
        testStatus204(id);
    }

    @ParameterizedTest(name = "status = 400 => id={0}")
    @MethodSource("parameter_test_400")
    public void test_400(String id) throws Exception {
        testStatus400(id);
    }

    private void testStatus204(String id) throws Exception {
        doNothing()
                .when(workService)
                .deleteWorkResource(Mockito.any());

        MockHttpServletRequestBuilder request = delete(COLLECTION_RESOURCE_URI, id);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    private void testStatus400(String id) throws Exception {
        MockHttpServletRequestBuilder request = delete(COLLECTION_RESOURCE_URI, id);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> parameter_test_204() {
        return Stream.of(
                Arguments.of("1"),
                Arguments.of("2"));
    }

    private static Stream<Arguments> parameter_test_400() {
        return Stream.of(
                Arguments.of("try"));
    }
}
