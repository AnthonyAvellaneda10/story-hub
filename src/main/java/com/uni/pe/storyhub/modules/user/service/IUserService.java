package com.uni.pe.storyhub.modules.user.service;

import org.springframework.http.ResponseEntity;

import com.uni.pe.storyhub.models.Alert;
import com.uni.pe.storyhub.modules.user.model.UpdatePassword;
import com.uni.pe.storyhub.modules.user.model.UpdatePhotoUser;
import com.uni.pe.storyhub.modules.user.model.UpdateProfileUser;
import com.uni.pe.storyhub.modules.user.model.UserDtoLogin;
import com.uni.pe.storyhub.modules.user.model.UserDtoRegistro;

public interface IUserService {
    Alert registrarUsuario(UserDtoRegistro usuarioDto);

    Alert loguearUsuario(UserDtoLogin usuarioLoguear);

    ResponseEntity<?> obtenerPerfilDelUsuario(String username);

    Alert actualizarFotoDePerfil(UpdatePhotoUser updatePhotoUser);

    Alert actualizarPerfil(UpdateProfileUser updateProfileUser);

    ResponseEntity<?> obtenerPerfilDelUsuarioPorEmail(String email);

    Alert actualizarContraseña( UpdatePassword updatePassword);

    ResponseEntity<?> obtenerFotoDePerfil(String email);
}
