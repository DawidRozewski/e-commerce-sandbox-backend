package com.dawidrozewski.sandbox.admin.category.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class AdminCategoryDto {
    @NotBlank
    @Length(min = 4)
    private String name;
    private String description;
    @NotBlank
    @Length(min = 4)
    private String slug;

}
