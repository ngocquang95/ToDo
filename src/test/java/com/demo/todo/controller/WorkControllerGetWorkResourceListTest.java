package com.demo.todo.controller;

import com.demo.todo.common.domain.PaginationParam;
import com.demo.todo.common.domain.ResultSet;
import com.demo.todo.common.domain.SortParam;
import com.demo.todo.domain.Status;
import com.demo.todo.resource.WorkResource;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WorkControllerGetWorkResourceListTest
{

    /**
     * Collection Resource URI
     */
    private static final String COLLECTION_RESOURCE_URI = "/works";

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

    @Test
    public void test(){
    }

    @ParameterizedTest(
            name = "status = 200, Check Parameter Validation, limit={0}, page={1}, sortBy={2}, direction={3}")
    @MethodSource("parameter_test_200")
    public void test_200(String page, String limit, String sortBy, String direction) throws Exception {
        testStatus200(page, limit, sortBy, direction);
    }

    private void testStatus200(String page, String limit, String sortBy, String direction) throws Exception {
        ResultSet<WorkResource> data;
        data = getDataMock();

        Mockito.doReturn(data)
                .when(workService)
                .getWorkResourceList(Mockito.any(PaginationParam.class), Mockito.any(SortParam.class));

        MockHttpServletRequestBuilder request = get(COLLECTION_RESOURCE_URI);

        if (page != null) {
            request.queryParam("page",page);
        }
        if (limit != null) {
            request.queryParam("limit",limit);
        }
        if (sortBy != null) {
            request.queryParam("sortBy", sortBy);
        }
        if (direction != null) {
            request.queryParam("direction", direction);
        }

        ResultActions actual = mockMvc.perform(request);

        actual.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("data").isNotEmpty());
    }

    private ResultSet<WorkResource> getDataMock() {
        List<WorkResource> workResources = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            Long id = (long) i;

            WorkResource workResource = WorkResource.builder()
                    .id(id)
                    .workName("work name")
                    .startingDate(LocalDate.of(2022, 1, 1))
                    .endingDate(LocalDate.of(2022, 1, 2))
                    .status(Status.PLANNING)
                    .build();

            workResources.add(workResource);
        }

        return new ResultSet<>(workResources, 3);
    }

    private static Stream<Arguments> parameter_test_200() {
        return Stream.of(
                Arguments.of("3", "1", "id", "ASC"),
                Arguments.of("1", "3", "workName", "DESC"));
    }
}
