package com.uni.pe.storyhub.modules.blog.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uni.pe.storyhub.models.Tags;
import com.uni.pe.storyhub.modules.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogResponse {
    private String titulo;
    private String breve_descripcion;
    private String img_banner;
    private String fecha_creacion;
    private String slug;
    private User user;
    private List<Tags> tags;
}
