package com.devsenior.co.producto.repository;

import com.devsenior.co.producto.model.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<ProductoEntity, Integer> {
}