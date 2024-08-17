package com.uni.pe.storyhub.modules.blog.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.uni.pe.storyhub.models.Alert;
import com.uni.pe.storyhub.modules.blog.model.Blog;;

public interface IBlogService {
    Alert añadirBlog(Blog blogRequest);

    ResponseEntity<?> obtenerListaDeBlogs(String email, int page, int size);

    ResponseEntity<?> buscarTodosLosBlogs(Pageable pageable);
    ResponseEntity<?> obtenerTagsCreados();

    ResponseEntity<?> obtenerInformacionDelBlog(String slug, Integer idUsuario);
    ResponseEntity<?> verificarLike(Integer idUsuario, Integer idBlog);
    ResponseEntity<?> darLikeAlBlog(Integer idUsuario, Integer idBlog);

    ResponseEntity<?> eliminarBlog(Integer idBlog);
    ResponseEntity<?> editarBlog(Integer idBlog, String breveDescripcion, String contenidoBlog, Boolean publicado);
}
