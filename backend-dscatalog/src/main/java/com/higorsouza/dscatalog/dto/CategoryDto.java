package com.higorsouza.dscatalog.dto;

import com.higorsouza.dscatalog.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@RequiredArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

}
