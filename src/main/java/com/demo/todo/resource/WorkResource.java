package com.demo.todo.resource;

import com.demo.todo.domain.Status;
import com.demo.todo.entity.Work;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Getter
@Builder(toBuilder = true)
@ToString
public class WorkResource
{
    /**
     * Work ID
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Long id;

    /**
     * Work Name
     */
    @NotBlank
    @Size(max = 255)
    private final String workName;

    /**
     * Starting Date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate startingDate;

    /**
     * Ending Date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endingDate;

    /**
     * Status Of Work
     */
    private final Status status;

    /**
     * Delete flag
     */
    private final Boolean isDeleted;

    /**
     * Build a work resource using the specified work entity
     *
     * @param entity Work entity
     */
    public WorkResource(Work entity) {
        this.id = entity.getId();
        this.workName = entity.getWorkName();
        this.startingDate = entity.getStartingDate();
        this.endingDate = entity.getEndingDate();
        this.status = entity.getStatus();
        this.isDeleted = entity.isDeleted();
    }

    /**
     * Convert a work resource to a work entity
     *
     * @return Work entity
     */
    public Work toEntity() {
        return Work.builder()
                .id(id)
                .workName(workName)
                .startingDate(startingDate)
                .endingDate(endingDate)
                .status(status)
                .isDeleted(isDeleted)
                .build();
    }
}
