package com.desa.miprestamito.controlador;

import com.desa.miprestamito.modelo.Usuario;
import com.desa.miprestamito.seguridad.AuthenticationRequest;
import com.desa.miprestamito.seguridad.AuthenticationResponse;
import com.desa.miprestamito.seguridad.JWTUtil;
import com.desa.miprestamito.seguridad.UsuarioDetailService;
import com.desa.miprestamito.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {
    //http://localhost:8081/muestras-medicas/api/auth/authenticate
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioDetailService usuarioDetailService;


    @Autowired
    private UsuarioServicio usuarioServicio;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static Logger logger
            = Logger.getLogger(
            AuthController.class.getName());
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest request) {
        String contraseña = getContraseña(request.getUsername()); //obtiene la contraseña del usuario
        if (contraseña != null && passwordEncoder.matches(request.getPassword(), contraseña)) { // si el usuario existe y la contraseña es correcta
          try {
            logger.log(Level.INFO, "se ejecuta el metodo createToken antes de authenticate");
            UserDetails userDetails = usuarioDetailService.loadUserByUsername(request.getUsername());
            logger.log(Level.INFO, "se ejecuta el metodo createToken despues de authenticate");
            String jwt = jwtUtil.generateToken(userDetails);
            String nombre = getDpi(request.getUsername()).get("dpi");
            String rol = getDpi(request.getUsername()).get("rol");
            String puntoAtencion = getDpi(request.getUsername()).get("puntoAtencion");

            return new ResponseEntity<>(new AuthenticationResponse(jwt, nombre, rol, puntoAtencion), HttpStatus.OK);
           } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
           }
        }else{
            System.out.println("false");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    public Map<String, String> getDpi(String username){
        Optional<Usuario> usuario = usuarioServicio.findbyCorreo(username);
        String dpi = usuario.get().getDpi();
        String rol = usuario.get().getRol()+"";
        String puntoAtencion = usuario.get().getIdpuntoatencion()+"";
        Map<String, String> response = new HashMap<>();
        response.put("dpi", dpi);
        response.put("rol", rol);
        response.put("puntoAtencion", puntoAtencion+"");
        return response;
    }


    public String getContraseña(String username){
        Optional<Usuario> usuario = usuarioServicio.findbyCorreo(username);
        String contraseña = usuario.get().getPassword();
        return contraseña;
    }
}