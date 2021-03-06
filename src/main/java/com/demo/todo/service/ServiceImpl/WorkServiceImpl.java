package com.demo.todo.service.ServiceImpl;

import com.demo.todo.common.domain.PaginationParam;
import com.demo.todo.common.domain.ResultSet;
import com.demo.todo.common.domain.SortParam;
import com.demo.todo.common.exception.ResourceNotFoundException;
import com.demo.todo.common.exception.UnexpectedPhantomReadCommonException;
import com.demo.todo.common.exception.ValidationErrorException;
import com.demo.todo.entity.Work;
import com.demo.todo.repository.WorkRepository;
import com.demo.todo.service.WorkService;
import com.demo.todo.resource.WorkResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Work service implement
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkServiceImpl implements WorkService
{
    /**
     * Work repository
     */
    private final WorkRepository workRepository;

    @Override
    @Transactional(readOnly = true)
    public ResultSet<WorkResource> getWorkResourceList(PaginationParam pagination, SortParam sort) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getLimit(),
                Sort.by(sort.getDirection(), sort.getSortBy()));
        Page<Work> resultSet = workRepository.findAll(pageable);
        List<WorkResource> resources = convertEntitiesToResources(resultSet.getContent());
        return new ResultSet<>(resources, resultSet.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkResource> getWorkResource(Long id) {
        return workRepository.findById(id).map(this::convertEntityToResource);
    }

    @Override
    @Transactional
    public WorkResource createWorkResource(WorkResource workResource) {
        // The ending date must be after the starting date
        validateDateApply(workResource);

        Long id = workRepository.save(workResource.toBuilder()
                .isDeleted(false)
                .build()
                .toEntity()).getId();
        return getWorkResource(id).orElseThrow((UnexpectedPhantomReadCommonException::new));
    }

    @Override
    @Transactional
    public WorkResource updateWorkResource(Long id, WorkResource workResource) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(WorkResource.class, id));
        workRepository.save(workResource.toBuilder()
                .isDeleted(work.isDeleted())
                .build()
                .toEntity().withId(id));
        return getWorkResource(id).orElseThrow((UnexpectedPhantomReadCommonException::new));
    }

    @Override
    @Transactional
    public void deleteWorkResource(Long id) {
        workRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(WorkResource.class, id));
        workRepository.deleteById(id);
    }

    @Override
    public void validateDateApply(WorkResource workResource) {
        if (workResource.getStartingDate() != null
                && workResource.getStartingDate().isAfter(workResource.getEndingDate())) {
            throw new ValidationErrorException("EndingDate must be after than StartingDate");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WorkResource convertEntityToResource(Work entity) {
        return convertEntitiesToResources(Collections.singletonList(entity)).get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkResource> convertEntitiesToResources(List<Work> entities) {
        return entities.stream()
                .map(WorkResource::new)
                .collect(toList());
    }
}
