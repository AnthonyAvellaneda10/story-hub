package com.uni.pe.storyhub.modules.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uni.pe.storyhub.models.Tags;
import com.uni.pe.storyhub.modules.blog.model.Blog;
import com.uni.pe.storyhub.modules.blog.model.BlogDetailResponse;
import com.uni.pe.storyhub.modules.blog.model.BlogDto;
import com.uni.pe.storyhub.modules.blog.model.BlogResponse;

import java.util.List;

public interface IBlogRepository {
    boolean añadirBlog(Blog blogRequest);

    boolean existeIdUsuario(Integer idUsuario);

    boolean existeIdBlog(Integer idBlog);

    boolean blogEliminado(Integer idBlog);

    boolean existeEmailDelUsuario(String email);

    Page<BlogDto> obtenerBlogsDelUsuario(String email, Pageable pageable);

    Page<BlogResponse> obtenerTodosLosBlogsCreados(Pageable pageable);
    List<Tags> obtenerTags();
    int obtenerIdBlogBySlug(String slug);

    BlogDetailResponse obtenerDetalleDelBlog(String slug, Integer idBlog, Integer idUsuario);
    int verificarSiDioLike(Integer idUsuario, Integer idBlog);
    int darLikePorPrimeraVez(Integer idUsuario, Integer idBlog);
    boolean verEstadoDeLike(Integer userId, Integer blogId);
    boolean existeLike(Integer idUsuario, Integer idBlog);
    int agregarLike(Integer idUsuario, Integer idBlog);
    int quitarLike(Integer idUsuario, Integer idBlog);

    boolean existeSlug(String slug);

    boolean esPublico(String slug);

    int actualizarVistasDelBlog(String slug);

    int eliminarBlog(Integer idBlog);
    int editarBlog(Integer idBlog, String breveDescripcion, String contenidoBlog, Boolean publicado);
    int blogEliminadoBySlug(String slug);
}
