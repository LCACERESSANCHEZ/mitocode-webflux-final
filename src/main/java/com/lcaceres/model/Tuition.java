package com.lcaceres.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "licensePlates")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tuition {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @NotNull
    private Student student;

    @NotNull
    private List<Course> courseList;

    @NotNull
    private LocalDate enrollmentDate;

    @NotNull
    private Boolean state;

}
