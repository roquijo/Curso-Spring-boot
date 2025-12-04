package com.devsenior.co.producto.model;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    private String nombre;
    private int cantidad;
    private double precio;
    private boolean estaDisponible;
}