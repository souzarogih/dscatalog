package com.higorsouza.dscatalog.controller;

import com.higorsouza.dscatalog.dto.ProductDto;
import com.higorsouza.dscatalog.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Log4j2
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> findAll(Pageable pageable) {

        //page, size, sort

        Page<ProductDto> list = productService.findAllPaged(pageable);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {

        ProductDto productDto = productService.findById(id);
        return ResponseEntity.ok().body(productDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> insert(@RequestBody ProductDto productDto) {
        log.info("chegou na controller insert");
        ProductDto productInserted = productService.insert(productDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productInserted.getId())
                .toUri();
        return ResponseEntity.created(uri).body(productInserted);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id,
                                              @RequestBody ProductDto productDto) {
        log.info("chegou na controller update");
        ProductDto productInserted = productService.update(id, productDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productInserted.getId())
                .toUri();
        log.info("Log uri={}" ,uri);
        return ResponseEntity.ok().body(productInserted);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("chegou na controller delete");
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
