package com.devsenior.co.producto.service;

import com.devsenior.co.producto.model.ProductoEntity;
import com.devsenior.co.producto.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private IProductoRepository iProductoRepository;

    public  List<ProductoEntity> findAll() {
        return iProductoRepository.findAll();
    }
}
