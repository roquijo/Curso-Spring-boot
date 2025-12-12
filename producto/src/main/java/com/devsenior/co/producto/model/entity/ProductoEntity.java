package com.devsenior.co.producto.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "producto")
@Builder
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private Integer cantidad;
    private Double precio;
    private Boolean estaDisponible;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoriaEntity categoria;

    public ProductoEntity(String nombre, Integer cantidad, Double precio, Boolean estaDisponible) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.estaDisponible = estaDisponible;
    }
}