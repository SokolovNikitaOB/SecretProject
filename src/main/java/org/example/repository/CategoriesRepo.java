package org.example.repository;

import org.example.domain.Categories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepo extends CrudRepository<Categories, Long> {
    Categories findByRequestId(String requestId);
}
