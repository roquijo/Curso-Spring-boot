package com.devsenior.co.producto.model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDto {

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 3, message = "El nombre debe tener más de 2 caracteres")
    private String nombre;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad no puede ser menor a 0")
    @Max(value = 15, message = "La cantidad no puede ser mayor a 15")
    private Integer cantidad;

    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 0, message = "El precio no puede ser menor a 0")
    private Double precio;

    @NotNull(message = "Esta disponible no puede ser nulo")
    private Boolean estaDisponible;
}