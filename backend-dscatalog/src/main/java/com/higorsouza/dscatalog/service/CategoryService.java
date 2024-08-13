package com.higorsouza.dscatalog.service;

import com.higorsouza.dscatalog.dto.CategoryDto;
import com.higorsouza.dscatalog.model.Category;
import com.higorsouza.dscatalog.repository.CategoryRepository;
import com.higorsouza.dscatalog.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll(){
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(x -> new CategoryDto(x)).collect(Collectors.toList());

//        List<CategoryDto> listDto = new ArrayList<>();
//        for (Category cat : list){
//            listDto.add(new CategoryDto(cat));
//        }

//        return listDto;
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
//        Category entity = category.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new CategoryDto(category);
    }
}
