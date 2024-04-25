package com.dawidrozewski.sandbox.admin;

import com.dawidrozewski.sandbox.admin.category.controller.dto.AdminCategoryDto;
import com.dawidrozewski.sandbox.admin.category.model.AdminCategory;

public class AdminHelper {


    public static AdminCategory mapToEntity(AdminCategoryDto adminCategoryDto, Long id) {
        return new AdminCategory(id, adminCategoryDto.getName(), adminCategoryDto.getDescription(), adminCategoryDto.getSlug());
    }

    public static AdminCategoryDto mapToDto(AdminCategory adminCategory) {
        return new AdminCategoryDto(adminCategory.getName(), adminCategory.getDescription(), adminCategory.getSlug());
    }
}
