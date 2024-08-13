package com.higorsouza.dscatalog.service;

import com.higorsouza.dscatalog.dto.CategoryDto;
import com.higorsouza.dscatalog.model.Category;
import com.higorsouza.dscatalog.repository.CategoryRepository;
import com.higorsouza.dscatalog.service.exceptions.ControllerNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
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
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Entity not found"));
//        Category entity = category.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new CategoryDto(category);
    }

    @Transactional
    public CategoryDto insert(CategoryDto categoryDto) {
        log.info("chegou na service insert");
        Category category = new Category();
        category.setName(categoryDto.getName());
        Category categorySaved = categoryRepository.save(category);
        return new CategoryDto(categorySaved);
    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        log.info("chegou na service update");
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(categoryDto.getName());
            return new CategoryDto(categoryRepository.save(category));
        } catch (EntityNotFoundException e){
            log.error("Id not found {}",id);
            throw  new ControllerNotFoundException("Id not found " + id);
        }
    }
}
