package com.uni.pe.storyhub.modules.comentario.repository;

import java.util.List;

import com.uni.pe.storyhub.modules.comentario.model.ComentarioBlog;

public interface IComentarioRepository {
    List<ComentarioBlog> obtenerComentariosBlog(int idBlog);
    int publicarComentario(String comentario, Integer parent_comentario_id, String reply_to, int idUsuario, int idBlog);
    int actualizarComentario(String comentarioEdit, int idComentario);
    int eliminarComentario(int idComentario);
}
