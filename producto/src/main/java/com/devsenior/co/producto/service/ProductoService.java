package com.devsenior.co.producto.service;

import com.devsenior.co.producto.model.ProductoDto;
import com.devsenior.co.producto.model.ProductoEntity;
import com.devsenior.co.producto.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private IProductoRepository iProductoRepository;

    public List<ProductoDto> findAll() {
        return iProductoRepository.findAll().stream().map( entity ->
                ProductoDto.builder().nombre(entity.getNombre()).build()
        ).toList();
    }

    public ProductoEntity create(ProductoDto producto) {
        ProductoEntity entity = new ProductoEntity(producto.getNombre(), producto.getCantidad(), producto.getPrecio(), producto.getEstaDisponible());
        return iProductoRepository.save(entity);
    }

    public ProductoEntity update(Integer idEntity, ProductoDto producto) {
        ProductoEntity entity = iProductoRepository.findById(idEntity).orElseThrow();
        entity.setNombre(producto.getNombre());
        entity.setCantidad(producto.getCantidad());
        entity.setPrecio(producto.getPrecio());
        entity.setEstaDisponible(producto.getEstaDisponible());
        return iProductoRepository.save(entity);
    }

    public void delete(Integer idEntity) {
        iProductoRepository.deleteById(idEntity);
    }
}
