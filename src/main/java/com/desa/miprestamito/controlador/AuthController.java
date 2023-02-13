package com.desa.miprestamito.controlador;

import com.desa.miprestamito.seguridad.AuthenticationRequest;
import com.desa.miprestamito.seguridad.AuthenticationResponse;
import com.desa.miprestamito.seguridad.JWTUtil;
import com.desa.miprestamito.servicio.UsuarioDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private static Logger logger
            = Logger.getLogger(
            AuthController.class.getName());
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest request) {

        try {
            logger.log(Level.INFO, "se ejecuta el metodo createToken antes de authenticate");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            logger.log(Level.INFO, "se ejecuta el metodo createToken despues de authenticate");
            UserDetails userDetails = usuarioDetailService.loadUserByUsername(request.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);
            return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}