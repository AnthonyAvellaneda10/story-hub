package com.uni.pe.storyhub.modules.user.repository;

import com.uni.pe.storyhub.modules.user.model.GetProfilePicture;
import com.uni.pe.storyhub.modules.user.model.GetUserProfileResponse;
import com.uni.pe.storyhub.modules.user.model.UpdatePhotoUser;
import com.uni.pe.storyhub.modules.user.model.UpdateProfileUser;
import com.uni.pe.storyhub.modules.user.model.UserProfileBlogResponse;
import com.uni.pe.storyhub.modules.user.model.UserResponse;

public interface IUserRepository {
    boolean existeCorreo(String email);

    boolean existeNombreUsuario(String username);

    boolean registrarUsuario(String nombre_completo, String username, String correo, String contraseña, String imagen_perfil);

    String obtenerContraseñaPorEmail(String email);

    UserResponse obtenerDataDelUsuarioPorEmail(String email);
    UserProfileBlogResponse getUserProfile(String username);

    int actualizarFotoDePerfil(UpdatePhotoUser updatePhotoUser);

    int actualizarPerfil(UpdateProfileUser updateProfileUser);

    GetUserProfileResponse obtenerPerfilDelUsuarioPorEmail(String email);

    int actualizarContraseña(String email, String newPassword);

    GetProfilePicture obtenerFotoDePerfil(String email);
}
