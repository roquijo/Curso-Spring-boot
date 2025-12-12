package com.devsenior.co.producto.service;

import com.devsenior.co.producto.model.CategoriaDto;
import com.devsenior.co.producto.model.entity.CategoriaEntity;
import com.devsenior.co.producto.repository.ICategoriaRepository;
import com.devsenior.co.producto.shared.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CategoriaService implements ICategoriaService {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Override
    public CategoriaEntity create(CategoriaDto categoria) {
        CategoriaEntity cat = createEntity(categoria);
        return categoriaRepository.save(cat);
    }

//    @Override
//    public CategoriaEntity update(Integer id, CategoriaDto categoria) {
//        categoriaRepository.findById(id).orElseThrow(() ->
//                new CustomException("La categoria con id: "+ id + " no existe", new Date()));
//        CategoriaEntity cat = createEntity(categoria);
//        cat.setId(id);
//        return categoriaRepository.save(cat);
//    }

    @Override
    public CategoriaEntity update(Integer id, CategoriaDto categoria) {
        CategoriaEntity entity = categoriaRepository.findById(id).orElseThrow(() ->
                new CustomException("La categoria con id: "+ id + " no existe", new Date()));
        entity.setNombre(categoria.getNombre());
        entity.setDescripcion(categoria.getDescripcion());
        return categoriaRepository.save(entity);
    }

    public CategoriaEntity createEntity(CategoriaDto categoria) {
        CategoriaEntity cat = new CategoriaEntity();
        cat.setNombre(categoria.getNombre());
        cat.setDescripcion(categoria.getDescripcion());
        return cat;
    }
}
