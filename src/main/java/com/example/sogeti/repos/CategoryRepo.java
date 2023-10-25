package com.example.sogeti.repos;

import com.example.sogeti.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.List;

public interface CategoryRepo extends CrudRepository<Category, Long> {

    Optional<Category> findByName(String name);
    List<Category> findAll();
}
