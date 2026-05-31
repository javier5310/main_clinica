package com.demo.persistencia.demopersistencia.controllers;

import com.demo.persistencia.demopersistencia.dto.LoginRequestDTO;
import com.demo.persistencia.demopersistencia.dto.LoginResponseDTO;
import com.demo.persistencia.demopersistencia.entidades.Usuario;
import com.demo.persistencia.demopersistencia.repositorio.UsuarioRepository;
import com.demo.persistencia.demopersistencia.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        
        // Obtener el usuario de la BD
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        String token = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
        
        LoginResponseDTO response = new LoginResponseDTO(
                token,
                refreshToken,
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority(),
                usuario.getNombreCompleto(),
                usuario.getUsuarioId()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token no proporcionado");
        }
        
        try {
            String username = jwtTokenUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtTokenUtil.validateToken(refreshToken, userDetails)) {
                String newToken = jwtTokenUtil.generateToken(userDetails);
                String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
                
                Usuario usuario = usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
                LoginResponseDTO response = new LoginResponseDTO(
                    newToken,
                    newRefreshToken,
                    username,
                    "ROLE_" + usuario.getRol(),
                    usuario.getNombreCompleto(),
                    usuario.getUsuarioId()
                );
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Refresh token inválido");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error al renovar token: " + e.getMessage());
        }
    }
    
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("API funcionando correctamente");
    }
}