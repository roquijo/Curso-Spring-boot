package com.devsenior.co.producto.service;

import com.devsenior.co.producto.model.CategoriaDto;
import com.devsenior.co.producto.model.entity.CategoriaEntity;

public interface ICategoriaService {

    CategoriaEntity create(CategoriaDto categoria);

    CategoriaEntity update(Integer id, CategoriaDto categoria);
}
