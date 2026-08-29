package pl.advansoft.aachen.catalog.domain;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    ProductEntity findByCode(String code);
}
