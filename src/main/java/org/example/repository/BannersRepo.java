package org.example.repository;

import org.example.domain.Banners;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannersRepo extends CrudRepository<Banners, Long> {
}
