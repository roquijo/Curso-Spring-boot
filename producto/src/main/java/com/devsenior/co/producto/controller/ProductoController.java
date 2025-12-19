package com.devsenior.co.producto.controller;

import com.devsenior.co.producto.model.ProductoDto;
import com.devsenior.co.producto.model.entity.ProductoEntity;
import com.devsenior.co.producto.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
@SecurityRequirement(name = "token")
//@PreAuthorize("hasAnyRole('producto-rol2')")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista con todos los productos disponibles")
    public List<ProductoDto> findAll() {
        return productoService.findAll();
    }

    @PostMapping
    @Operation(summary = "Crear un producto", description = "Metodo para crear un producto")
    public ProductoEntity create(@Valid @RequestBody ProductoDto producto) {
        return productoService.create(producto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto", description = "Metodo para actualizar un producto")
    public ProductoEntity update(@PathVariable Integer id, @Valid @RequestBody ProductoDto producto) {
        return productoService.update(id, producto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto", description = "Metodo para actualizar un producto")
    public void delete(@PathVariable Integer id) {
         productoService.delete(id);
    }
}