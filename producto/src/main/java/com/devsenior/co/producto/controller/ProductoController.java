package com.devsenior.co.producto.controller;

import com.devsenior.co.producto.model.ProductoEntity;
import com.devsenior.co.producto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<ProductoEntity> findAll() {
        return productoService.findAll();
    }

}
