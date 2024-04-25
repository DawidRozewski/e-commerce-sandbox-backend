package com.dawidrozewski.sandbox.admin.category.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminCategoryDto {
    @NotBlank
    @Length(min = 4)
    private String name;
    private String description;
    @NotBlank
    @Length(min = 4)
    private String slug;

}
