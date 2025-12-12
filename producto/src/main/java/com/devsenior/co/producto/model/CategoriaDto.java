package com.devsenior.co.producto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaDto {

    @NotBlank(message = "El nombre de la categoria no puede ser nulo o estar vacio")
    @Size(min = 3, message = "El nombre debe tener más de 2 caracteres")
    private String nombre;
    @NotBlank(message = "La descripcion de la categoria no puede ser nulo o estar vacio")
    @Size(min = 10, message = "La descripcion debe tener más de 10 caracteres")
    private String descripcion;
}