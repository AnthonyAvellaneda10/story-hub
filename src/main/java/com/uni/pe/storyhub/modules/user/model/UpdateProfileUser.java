package com.uni.pe.storyhub.modules.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProfileUser {
    private String email;
    private String nombre_completo;
    private String username;
    private String descripcion;
    private String linkedin;
    private String telegram;
    private String instagram;
    private String facebook;
    private String youtube;
}
