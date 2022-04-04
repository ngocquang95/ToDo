package com.demo.todo.controller;

import com.demo.todo.domain.Status;
import com.demo.todo.resource.WorkResource;
import com.demo.todo.service.WorkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WorkControllerCreateWorkResourceTest
{
    private static final String RESOURCE_ID_HEADER = "Resource-Id";

    /**
     * Collection Resource URI
     */
    private static final String COLLECTION_RESOURCE_URI = "/works";

    @Test
    public void test(){
    }

    /**
     * MockMvc
     */
    @Autowired
    protected MockMvc mockMvc;

    /**
     * Work service
     */
    @MockBean
    WorkService workService;

    @ParameterizedTest(name = "status = 201 => id={0} workName={1}")
    @MethodSource("parameter_test_201")
    public void test_201(String id, String workName) throws Exception {
        testStatus201(id, workName);
    }

    @ParameterizedTest(name = "status = 400 => id={0} workName={1}")
    @MethodSource("parameter_test_400")
    public void test_400(String id, String workName) throws Exception {
        testStatus400(id, workName);
    }

    private void testStatus201(String id, String workName) throws Exception {
        WorkResource data = getDataMock(id, workName);
        Map<String, Object> postData = getPostData(id, workName);

        doReturn(data)
                .when(workService)
                .createWorkResource(Mockito.any());

        doReturn(Optional.of(data))
                .when(workService)
                .getWorkResource(Mockito.any());

        MockHttpServletRequestBuilder request = post(COLLECTION_RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(toJson(postData));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(RESOURCE_ID_HEADER, id))
                .andExpect(content().string(""));
    }

    private void testStatus400(String id, String workName) throws Exception {
        Map<String, Object> postData = getPostData(id, workName);

        MockHttpServletRequestBuilder request = post(COLLECTION_RESOURCE_URI)
                .contentType(APPLICATION_JSON)
                .content(toJson(postData));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private WorkResource getDataMock(String id, String workName) {
        return WorkResource.builder()
                .id(Long.valueOf(id))
                .workName(workName)
                .status(Status.PLANNING)
                .isDeleted(false)
                .build();
    }

    private Map<String, Object> getPostData(String id, String workName) {
        return new HashMap<>() {
            {
                put("id", id);
                put("workName", workName);
            }
        };
    }

    private static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    static Stream<Arguments> parameter_test_201() {
        return Stream.of(
                Arguments.of("1", "work 1"),
                Arguments.of("2", "work 2"),
                Arguments.of("3", "work 3"));
    }

    private static Stream<Arguments> parameter_test_400() {
        return Stream.of(
                Arguments.of("1", null),
                Arguments.of("1", ""));
    }
}
