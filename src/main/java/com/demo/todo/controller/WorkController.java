package com.demo.todo.controller;

import com.demo.todo.common.domain.PaginationParam;
import com.demo.todo.common.domain.ResultSet;
import com.demo.todo.common.domain.SortParam;
import com.demo.todo.common.exception.ResourceNotFoundException;
import com.demo.todo.resource.WorkResource;
import com.demo.todo.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.relativeTo;

/**
 * Work Controller
 */
@RestController
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkController
{
    private static final String RESOURCE_ID_HEADER = "Resource-Id";

    /**
     * Collection Resource URI
     */
    private static final String COLLECTION_RESOURCE_URI = "/works";

    /**
     * Item tag collection resource URI
     */
    private static final String MEMBER_RESOURCE_URI = COLLECTION_RESOURCE_URI + "/{id}";

    /**
     * Work service
     */
    private final WorkService workService;

    /**
     * Response API get work resource list
     *
     * @param pagination PaginationParam
     * @param sort SortParam
     * @return ResponseEntity
     */
    @GetMapping(COLLECTION_RESOURCE_URI)
    public ResponseEntity<ResultSet<WorkResource>> getWorkResourceList(
            PaginationParam pagination,
            SortParam sort) {
        return ResponseEntity.ok(workService.getWorkResourceList(pagination, sort));
    }

    /**
     * API get work resource
     *
     * @param id Long
     * @return ResponseEntity
     */
    @GetMapping(MEMBER_RESOURCE_URI)
    public ResponseEntity<WorkResource> getWorkResource(
            @PathVariable("id") Long id) {
        return workService.getWorkResource(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(WorkResource.class, id));
    }

    /**
     * API create work resource
     *
     * @param workResource WorkResource
     * @param uriBuilder UriComponentsBuilder
     * @return ResponseEntity
     */
    @PostMapping(COLLECTION_RESOURCE_URI)
    public ResponseEntity<Void> createWorkResource(
            @Valid @RequestBody WorkResource workResource,
            UriComponentsBuilder uriBuilder) {
        Long id = workService.createWorkResource(workResource).getId();
        URI uri = relativeTo(uriBuilder)
                .withMethodCall(on(getClass()).getWorkResource(id))
                .build()
                .encode()
                .toUri();
        return ResponseEntity.created(uri)
                .header(RESOURCE_ID_HEADER, id.toString())
                .build();
    }

    /**
     * API update work resource
     *
     * @param id Long
     * @param workResource WorkResource
     * @return ResponseEntity
     */
    @PutMapping(MEMBER_RESOURCE_URI)
    public ResponseEntity<WorkResource> updateWorkResource(
            @PathVariable("id") Long id,
            @Valid @RequestBody WorkResource workResource) {
        WorkResource updatedWorkResource = workService.updateWorkResource(id, workResource);
        return ResponseEntity.ok(updatedWorkResource);
    }

    /**
     * API delete work resource
     *
     * @param id Long
     * @return ResponseEntity
     */
    @DeleteMapping(MEMBER_RESOURCE_URI)
    public ResponseEntity<Void> deleteWorkResource(
            @PathVariable("id") Long id) {
        workService.deleteWorkResource(id);
        return ResponseEntity.noContent().build();
    }
}
