package com.devsenior.co.producto.service;

import com.devsenior.co.producto.model.ProductoDto;
import com.devsenior.co.producto.model.entity.ProductoEntity;
import com.devsenior.co.producto.repository.IProductoRepository;
import com.devsenior.co.producto.shared.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductoService{

    @Autowired
    private IProductoRepository iProductoRepository;

    public List<ProductoDto> findAll() {
        return iProductoRepository.findAll().stream().map( producto ->
                new ProductoDto(producto.getNombre(),
                        producto.getCantidad(),
                        producto.getPrecio(),
                        producto.getEstaDisponible())
        ).toList();
    }

    public ProductoEntity create(ProductoDto producto) {
        ProductoEntity entity = new ProductoEntity(producto.getNombre(), producto.getCantidad(), producto.getPrecio(), producto.getEstaDisponible());
        return iProductoRepository.save(entity);
    }

    public ProductoEntity update(Integer idEntity, ProductoDto producto) {
        ProductoEntity entity = iProductoRepository.findById(idEntity).orElseThrow(() ->
                new CustomException("El producto con id: " + idEntity + " no existe.", new Date()));
        entity.setNombre(producto.getNombre());
        entity.setCantidad(producto.getCantidad());
        entity.setPrecio(producto.getPrecio());
        entity.setEstaDisponible(producto.getEstaDisponible());
        return iProductoRepository.save(entity);
    }

    public void delete(Integer idEntity) {
        iProductoRepository.findById(idEntity).orElseThrow(() ->
                new CustomException("El producto con id: " + idEntity + " no existe.", new Date()));
        iProductoRepository.deleteById(idEntity);
    }


}
