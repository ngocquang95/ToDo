package com.demo.todo.entity;

import com.demo.todo.domain.Status;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Work entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@With
@Entity
@Table(name = "work")
@EntityListeners(AuditingEntityListener.class)
public class Work
{
    /**
     * Work ID
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * Work Name
     */
    @Column(name = "work_name")
    private String workName;

    /**
     * Starting Date
     */
    @Column(name = "starting_date")
    private LocalDate startingDate;

    /**
     * Ending Date
     */
    @Column(name = "ending_date")
    private LocalDate endingDate;

    /**
     * Status Of Work
     */
    @Column(name = "status")
    private Status status;

    /**
     * Delete flag
     */
    @Column(name = "is_deleted")
    @Accessors(fluent = true)
    private Boolean isDeleted;
}
