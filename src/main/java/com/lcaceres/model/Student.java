package com.lcaceres.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "students")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String names;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String surnames;

    @NotNull
    @NotEmpty
    @Size(min = 8, max = 8)
    private String dni;

    @NotNull
    private Integer age;
}
