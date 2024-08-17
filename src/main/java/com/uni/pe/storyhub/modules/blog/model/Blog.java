package com.uni.pe.storyhub.modules.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.uni.pe.storyhub.models.Tags;
import com.uni.pe.storyhub.modules.user.model.UserIDResponse;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Blog {
    private Integer id_blog;
    private String titulo;
    private String breve_descripcion;
    private String img_banner;
    private String img_portada;
    private String descripcion_img_portada;
    private String contenido_blog;
    private Boolean publicado;
    private Integer likes;
    private Integer vistas;
    private String slug;
    private UserIDResponse userIDResponse;
    private List<Tags> tags;
}
