package com.uni.pe.storyhub.modules.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserResponse {
    private Integer id_usuario;
    private String email;
    private String imagen_perfil;
    private String username;
}
