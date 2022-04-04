package com.demo.todo.controller;

import com.demo.todo.domain.Status;
import com.demo.todo.resource.WorkResource;
import com.demo.todo.service.WorkService;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WorkControllerGetWorkResourceTest
{
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

    @ParameterizedTest(name = "status = 200, Test status code 200, id = {0}")
    @MethodSource("parameter_test_200")
    public void test_200(String id) throws Exception {
        testStatus200(id);
    }

    @ParameterizedTest(name = "status = 400, Check Parameter Validation, id = {0}")
    @MethodSource("parameter_test_400")
    public void test_400(String id) throws Exception {
        testStatus400(id);
    }

    /**
     * Test script for status code 200 case
     *
     * @param id Work ID
     * @throws Exception Exception
     */
    private void testStatus200(String id) throws Exception {
        Optional<WorkResource> data = getDataMock();

        doReturn(data)
                .when(workService)
                .getWorkResource(any());

        MockHttpServletRequestBuilder request = get(COLLECTION_RESOURCE_URI, id);

        ResultActions actual = mockMvc.perform(request);

        actual.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", equalTo(1)))
                .andExpect(jsonPath("workName", equalTo("work name")))
                .andExpect(jsonPath("startingDate", equalTo("2022-01-01")))
                .andExpect(jsonPath("endingDate", equalTo("2022-01-02")))
                .andExpect(jsonPath("status", equalTo(0)));
    }

    /**
     * Test script for status code 400 case
     *
     * @param id Work ID
     * @throws Exception exception
     */
    private void testStatus400(String id) throws Exception {
        MockHttpServletRequestBuilder request = get(COLLECTION_RESOURCE_URI, id);

        ResultActions actual = mockMvc.perform(request);

        actual.andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * Create data for mock service
     *
     * @return data
     */
    private Optional<WorkResource> getDataMock() {
        WorkResource workResource = WorkResource.builder()
                .id(1L)
                .workName("work name")
                .startingDate(LocalDate.of(2022, 1, 1))
                .endingDate(LocalDate.of(2022, 1, 2))
                .status(Status.PLANNING)
                .build();

        return Optional.of(workResource);
    }

    /**
     * The data to test the case of status code 200
     *
     * @return data
     */
    private static Stream<Arguments> parameter_test_200() {
        return Stream.of(
                Arguments.of("1"));
    }

    /**
     * The data to test the case of status code 400
     *
     * @return data
     */
    private static Stream<Arguments> parameter_test_400() {
        return Stream.of(
                Arguments.of("@"),
                Arguments.of("a"));
    }
}
