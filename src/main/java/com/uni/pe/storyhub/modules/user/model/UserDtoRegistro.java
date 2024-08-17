package com.uni.pe.storyhub.modules.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDtoRegistro {
    private String nombre_completo;
    private String email;
    private String contraseña;
}
