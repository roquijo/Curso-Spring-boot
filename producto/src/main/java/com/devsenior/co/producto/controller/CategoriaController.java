package com.devsenior.co.producto.controller;

import com.devsenior.co.producto.model.CategoriaDto;
import com.devsenior.co.producto.model.entity.CategoriaEntity;
import com.devsenior.co.producto.service.ICategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Categorias", description = "API para gestión de categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService iCategoriaService;

    @PostMapping
    @Operation(summary = "Crear una categoria", description = "Metodo para crear una categoria")
    public CategoriaEntity create(@Valid @RequestBody CategoriaDto categoriaDto) {
        return iCategoriaService.create(categoriaDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoria", description = "Metodo para actualizar una categoria")
    public CategoriaEntity update(@PathVariable Integer id, @Valid @RequestBody CategoriaDto categoriaDto) {
        return iCategoriaService.update(id, categoriaDto);
    }
}
