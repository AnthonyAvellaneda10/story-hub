package com.uni.pe.storyhub.modules.user.service;

import com.uni.pe.storyhub.models.Alert;
import com.uni.pe.storyhub.modules.user.model.GetProfilePicture;
import com.uni.pe.storyhub.modules.user.model.GetUserProfileResponse;
import com.uni.pe.storyhub.modules.user.model.UpdatePassword;
import com.uni.pe.storyhub.modules.user.model.UpdatePhotoUser;
import com.uni.pe.storyhub.modules.user.model.UpdateProfileUser;
import com.uni.pe.storyhub.modules.user.model.UserDao;
import com.uni.pe.storyhub.modules.user.model.UserDtoLogin;
import com.uni.pe.storyhub.modules.user.model.UserDtoRegistro;
import com.uni.pe.storyhub.modules.user.model.UserProfileBlogResponse;
import com.uni.pe.storyhub.modules.user.model.UserResponse;
import com.uni.pe.storyhub.modules.user.repository.IUserRepository;
import com.uni.pe.storyhub.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository iUserRepository;
    Utilidades utilidades = new Utilidades();
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Alert registrarUsuario(UserDtoRegistro usuarioDto) {

        try {
            String nombreCompleto = usuarioDto.getNombre_completo();
            String email = usuarioDto.getEmail();
            String contraseña = usuarioDto.getContraseña();
            
            String username = null;
            if (nombreCompleto != null) {
                username = utilidades.extraerPrimerNombre(nombreCompleto);
            }

            // Verificar el formato del correo electrónico
            if (!utilidades.isValidEmailFormat(email)) {
                // El formato del correo no es válido, devolver un TestResponse con mensaje de error
                return new Alert(Utilidades.getNextAlertId(), "El formato de email no es válido", 5000, "danger", 404);
            }

            // Verificar si el correo esta escrito en minúsculas
            if (!(email == email.toLowerCase())) {

                return new Alert(Utilidades.getNextAlertId(), "Solo se permiten letras (a-z), números (0-9)  y puntos (.)", 5000, "danger", 404);
            }

            // Verificar si el correo ya existe en la base de datos
            if (iUserRepository.existeCorreo(email)) {
                return new Alert(Utilidades.getNextAlertId(), "Este correo ya existe", 5000, "danger", 404);
            }

            // Verificar si el nombre de usuario ya existe en la base de datos
            if (iUserRepository.existeNombreUsuario(username)) {
                return new Alert(Utilidades.getNextAlertId(), "El nombre de usuario ya existe", 5000, "danger", 404);
            }

            // Encriptando la contraseña del usuario
            String encodedPassword = passwordEncoder.encode(contraseña);

            UserDao obtenerUserDao = setearUsuario( nombreCompleto,  username,  email,  encodedPassword);

            // Insertar usuario en la base de datos
            boolean registroExitoso = iUserRepository.registrarUsuario(obtenerUserDao.getNombre_completo(), obtenerUserDao.getUsername(), obtenerUserDao.getEmail(), obtenerUserDao.getContraseña(), obtenerUserDao.getImagen_perfil());

            if (registroExitoso) {
                return new Alert(Utilidades.getNextAlertId(), "Registro exitoso", 5000, "success", 201);
            } else {
                return new Alert(Utilidades.getNextAlertId(), "Ups, parece que algo salió mal", 5000, "warning", 500);
            }

        } catch (Exception e) {
            // Manejar la excepción
            e.printStackTrace();
            // Devolver una respuesta de error
            return new Alert(Utilidades.getNextAlertId(), "Ups, parece que algo salió mal", 5000, "warning", 500);
        }

    }

    @Override
    public Alert loguearUsuario(UserDtoLogin usuarioLoguear) {
        try {
            // Obtener el correo del Usuario
            String email = usuarioLoguear.getEmail();
            // Obtener la contraseña del Usuario
            String contraseña = usuarioLoguear.getContraseña();

            // Verificar el formato del correo electrónico
            if (!utilidades.isValidEmailFormat(email)) {
                return new Alert(Utilidades.getNextAlertId(), "El formato de correo electrónico no es válido", 5000, "danger", 404);
            }

            // Verificar si el usuario existe en la base de datos
            if (!iUserRepository.existeCorreo(email)) {
                return new Alert(Utilidades.getNextAlertId(), "Usuario no encontrado", 5000, "danger", 404);
            }

            // Obtener la contraseña asociada con el correo electrónico proporcionado por el usuario
            String contraseñaAlmacenada = iUserRepository.obtenerContraseñaPorEmail(email);


            // Comparar la contraseña almacenada con la proporcionada por el usuario
            if (!passwordEncoder.matches(contraseña, contraseñaAlmacenada)) {
                // La contraseña no coincide
                return new Alert(Utilidades.getNextAlertId(), "Contraseña incorrecta", 5000, "danger", 404);
            } else {
                UserResponse userResponse = iUserRepository.obtenerDataDelUsuarioPorEmail(email);
                // Inicio de sesión exitoso
                return new Alert(Utilidades.getNextAlertId(), "Inicio de sesión exitoso", 5000, "success", 200, userResponse);
            }

        } catch (Exception e) {
            // Manejar la excepción
            e.printStackTrace();
            // Devolver una respuesta de error
            return new Alert(Utilidades.getNextAlertId(), "Ups, parece que algo salió mal", 5000, "warning", 500);
        }
    }

    @Override
    public ResponseEntity<?> obtenerPerfilDelUsuario(String username) {
        try {
            // Verificar si el nickname del usuario existe
            if (!iUserRepository.existeNombreUsuario(username)){
                Alert alert = new Alert(Utilidades.getNextAlertId(), "No existe dicho usuario", 5000, "warning", 500);;;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(alert);
            }

            UserProfileBlogResponse userProfile = iUserRepository.getUserProfile(username);
            return ResponseEntity.ok(userProfile);

        } catch (Exception e) {
            // Manejar la excepción
            e.printStackTrace();
            Alert alert = new Alert(Utilidades.getNextAlertId(), "Ups, parece que algo salio mal", 5000, "warning", 500);;;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(alert);
        }

    }

    @Override
    public ResponseEntity<?> obtenerPerfilDelUsuarioPorEmail(String email) {
        try {

            // Verificar el formato del correo electrónico
            if (!utilidades.isValidEmailFormat(email)) {
                Alert alert = new Alert(Utilidades.getNextAlertId(), "El formato de correo electrónico no es válido", 5000, "danger", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(alert);
            }

            // Verificar si el email del usuario existe
            if (!iUserRepository.existeCorreo(email)){
                Alert alert = new Alert(Utilidades.getNextAlertId(), "No existe dicho correo", 5000, "warning", 500);;;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(alert);
            }

            GetUserProfileResponse userProfile = iUserRepository.obtenerPerfilDelUsuarioPorEmail(email);
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            // Manejar la excepción
            e.printStackTrace();
            Alert alert = new Alert(Utilidades.getNextAlertId(), "Ups, parece que algo salio mal", 5000, "warning", 500);;;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(alert);
        }
    }

    @Override
    public Alert actualizarFotoDePerfil(UpdatePhotoUser updatePhotoUser) {
        try {
            // Obtener email
            String email = updatePhotoUser.getEmail();

            // Verificar el formato del correo electrónico
            if (!utilidades.isValidEmailFormat(email)) {
                return new Alert(Utilidades.getNextAlertId(), "El formato de correo electrónico no es válido", 5000, "danger", 404);
            }

            // Verificar si el usuario existe en la base de datos
            if (!iUserRepository.existeCorreo(email)) {
                return new Alert(Utilidades.getNextAlertId(), "Correo no existente", 5000, "danger", 404);
            }

            int rowsAffected = iUserRepository.actualizarFotoDePerfil(updatePhotoUser);
            if (rowsAffected > 0) {
                // Si se actualizó algún registro, enviar una respuesta de éxito

                return new Alert(Utilidades.getNextAlertId(), "Se actualizó su foto de perfil \uD83D\uDCF7", 5000, "success", 200);
            } else {
                return new Alert(Utilidades.getNextAlertId(), "No se pudo actualizar su foto de perfil", 5000, "success", 200);
            }
        } catch (DataIntegrityViolationException dv) {
            return new Alert(Utilidades.getNextAlertId(), "El nombre de la imagen es demasiado grande, trate de reducir el nombre de su imagen", 5000, "warning", 500);
        }
        catch (Exception e) {
            // Manejar la excepción
            e.printStackTrace();
            // Devolver una respuesta de error
            return new Alert(Utilidades.getNextAlertId(), "Ups, parece que algo salió mal", 5000, "warning", 500);
        }
    }

    @Override
    public Alert actualizarPerfil(UpdateProfileUser updateProfileUser) {
        try {
            // Obtener email
            String email = updateProfileUser.getEmail();

            // Verificar el formato del correo electrónico
            if (!utilidades.isValidEmailFormat(email)) {
                return new Alert(Utilidades.getNextAlertId(), "El formato de correo electrónico no es válido", 5000, "danger", 404);
            }

            // Verificar si el usuario existe en la base de datos
            if (!iUserRepository.existeCorreo(email)) {
                return new Alert(Utilidades.getNextAlertId(), "Correo no existente", 5000, "danger", 404);
            }

            int rowsAffected = iUserRepository.actualizarPerfil(updateProfileUser);

            if (rowsAffected > 0) {
                // Si se actualizó algún registro, enviar una respuesta de éxito

                return new Alert(Utilidades.getNextAlertId(), "Se actualizó su perfil \uD83D\uDCDD", 5000, "success", 200);
            } else {
                return new Alert(Utilidades.getNextAlertId(), "No se pudo actualizar su perfil", 5000, "success", 200);
            }
        } catch (DuplicateKeyException e){
            // Manejar la excepción de clave duplicada (nombre de usuario duplicado)
            return new Alert(Utilidades.getNextAlertId(), "El nombre de usuario ya está en uso", 5000, "danger", 404);
        }
        catch (Exception e) {
            // Manejar la excepción
            e.printStackTrace();
            // Devolver una respuesta de error
            return new Alert(Utilidades.getNextAlertId(), "Ups, parece que algo salió mal", 5000, "warning", 500);
        }
    }

    @Override
    public Alert actualizarContraseña( UpdatePassword updatePassword) {

        try {
            String email = updatePassword.getEmail();
            String currentPassword = updatePassword.getCurrent_password();
            String newPassword = updatePassword.getNew_password();

            // Obtener la contraseña encriptada almacenada en la base de datos
            String storedEncryptedPassword = iUserRepository.obtenerContraseñaPorEmail(email);

            // Verificar si la contraseña antigua proporcionada coincide con la contraseña almacenada en la base de datos
            boolean isOldPasswordCorrect = passwordEncoder.matches(currentPassword, storedEncryptedPassword);

            if (isOldPasswordCorrect) {
                // Verificar si la nueva contraseña es igual a la contraseña anterior
                if (currentPassword.equals(newPassword)) {
                    return new Alert(Utilidades.getNextAlertId(), "La nueva contraseña debe ser diferente a la actual", 5000, "danger", 404);
                }

                // La contraseña antigua coincide, encriptar la nueva contraseña y actualizarla en la base de datos
                String encryptedNewPassword = passwordEncoder.encode(newPassword);
                int rowsAffected = iUserRepository.actualizarContraseña(email, encryptedNewPassword);

                if (rowsAffected > 0) {
                    return new Alert(Utilidades.getNextAlertId(), "Contraseña actualizada correctamente", 5000, "success", HttpStatus.OK.value());
                } else {
                    return new Alert(Utilidades.getNextAlertId(), "Error al actualizar la contraseña", 5000, "warning", 500);
                }
            } else {
                // La contraseña antigua no coincide
                return new Alert(Utilidades.getNextAlertId(), "La contraseña actual no es correcta", 5000, "danger", 404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Alert(Utilidades.getNextAlertId(), "Ups, parece que algo salió mal", 5000, "warning", 500);
        }
    }

    @Override
    public ResponseEntity<?> obtenerFotoDePerfil(String email) {
        try {

            // Verificar el formato del correo electrónico
            if (!utilidades.isValidEmailFormat(email)) {
                Alert alert = new Alert(Utilidades.getNextAlertId(), "El formato de correo electrónico no es válido", 5000, "danger", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(alert);
            }

            // Verificar si el email del usuario existe
            if (!iUserRepository.existeCorreo(email)){
                Alert alert = new Alert(Utilidades.getNextAlertId(), "No existe dicho correo", 5000, "warning", 500);;;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(alert);
            }

            GetProfilePicture profilePicture = iUserRepository.obtenerFotoDePerfil(email);
            return ResponseEntity.ok(profilePicture);
        } catch (Exception e) {
            // Manejar la excepción
            e.printStackTrace();
            Alert alert = new Alert(Utilidades.getNextAlertId(), "Ups, parece que algo salio mal", 5000, "warning", 500);;;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(alert);
        }
    }


    private UserDao setearUsuario(String nombreCompleto, String username, String email, String encodedPassword) {
        // Crear el UserDto
        UserDao usuarioDao = new UserDao();
        usuarioDao.setNombre_completo(nombreCompleto);
        usuarioDao.setUsername(username);
        usuarioDao.setEmail(email);
        usuarioDao.setContraseña(encodedPassword);
        usuarioDao.setImagen_perfil("assets/images/user-profile.png");

        return usuarioDao;
    }

}
