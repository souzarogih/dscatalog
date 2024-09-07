package com.higorsouza.dscatalog.service;

import com.higorsouza.dscatalog.dto.CategoryDto;
import com.higorsouza.dscatalog.dto.ProductDto;
import com.higorsouza.dscatalog.model.Category;
import com.higorsouza.dscatalog.model.Product;
import com.higorsouza.dscatalog.repository.CategoryRepository;
import com.higorsouza.dscatalog.repository.ProductRepository;
import com.higorsouza.dscatalog.service.exceptions.ControllerNotFoundException;
import com.higorsouza.dscatalog.service.exceptions.DatabaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAllPaged(PageRequest pageRequest){
        Page<Product> list = productRepository.findAll(pageRequest);
        return list.map(x -> new ProductDto(x));

//        List<CategoryDto> listDto = new ArrayList<>();
//        for (Category.java cat : list){
//            listDto.add(new CategoryDto(cat));
//        }

//        return listDto;
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Entity not found"));
//        Category.java entity = category.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new ProductDto(product, product.getCategories());
    }

    @Transactional
    public ProductDto insert(ProductDto productDto) {
        log.info("chegou na service insert");
        Product product = new Product();
        copyDtoToEntity(productDto, product);
        Product productSaved = productRepository.save(product);
        return new ProductDto(productSaved);
    }

    @Transactional
    public ProductDto update(Long id, ProductDto productDto) {
        log.info("chegou na service update");
        try {
            Product product = productRepository.getReferenceById(id);
            copyDtoToEntity(productDto, product);
            product.setName(productDto.getName());
            return new ProductDto(productRepository.save(product));
        } catch (EntityNotFoundException e){
            log.error("UpdateError: {}", e.getMessage());
            log.error("Id not found {}",id);
            throw  new ControllerNotFoundException("Id not found " + id);
        }
    }

    private void copyDtoToEntity(ProductDto productDto, Product product){
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setDate(productDto.getDate());
        product.setImgUrl(productDto.getImgUrl());
        product.setPrice(productDto.getPrice());

        product.getCategories().clear();
        for (CategoryDto catDto : productDto.getCategories()){
            Category category = categoryRepository.getReferenceById(catDto.getId());
            product.getCategories().add(category);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        log.info("chegou na service delete");
        if (!productRepository.existsById(id)) {
            log.error("Recurso não encontrado for id {}", id);
            throw new ControllerNotFoundException("Recurso não encontrado");
        }
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            log.error("DeleteError: {}", e.getMessage());
            log.error("Id not found {}",id);
            throw  new ControllerNotFoundException("Id not found for category in delete" + id);
        } catch (DataIntegrityViolationException e){
            log.error("DeleteError: {}", e.getMessage());
            log.error("Id not found {}",id);
            throw  new DatabaseException("Integrity violation");
        }
    }
}
