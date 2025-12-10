package com.devsenior.co.producto.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDto {

    private String nombre;
    private Integer cantidad;
    private Double precio;
    private Boolean estaDisponible;
}