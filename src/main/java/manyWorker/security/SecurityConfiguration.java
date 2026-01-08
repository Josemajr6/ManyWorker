package manyWorker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JWTAuthenticationFilter JWTAuthenticationFilter;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
            // Rutas públicas
            .requestMatchers("/actor/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/trabajador").permitAll()
            .requestMatchers(HttpMethod.POST, "/cliente").permitAll()                

            // Rutas ADMINISTRADOR
            .requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR")
            .requestMatchers(HttpMethod.GET, "/trabajador").hasAuthority("ADMINISTRADOR")
            .requestMatchers(HttpMethod.GET, "/cliente", "/cliente/*").hasAuthority("ADMINISTRADOR")
            .requestMatchers(HttpMethod.PUT, "/banear/**").hasAuthority("ADMINISTRADOR")
            .requestMatchers(HttpMethod.PUT, "/desbanear/**").hasAuthority("ADMINISTRADOR")

            // Rutas TRABAJADOR
            .requestMatchers("/trabajador/**").hasAuthority("TRABAJADOR")
            .requestMatchers(HttpMethod.PUT, "/trabajador").hasAuthority("TRABAJADOR")
            .requestMatchers(HttpMethod.DELETE, "/trabajador").hasAuthority("TRABAJADOR")

            // Rutas CLIENTE
            .requestMatchers("/cliente/**").hasAuthority("CLIENTES")
            .requestMatchers(HttpMethod.PUT, "/cliente").hasAuthority("CLIENTES")
            .requestMatchers(HttpMethod.DELETE, "/cliente").hasAuthority("CLIENTES")
            .requestMatchers("/cliente/miPerfil").hasAuthority("CLIENTES")

            // Endpoints compartidos
            .requestMatchers("/perfilSocial/**").hasAnyAuthority("CLIENTES", "TRABAJADOR")
            
            // Endpoints Solocitud
            .requestMatchers(HttpMethod.POST, "/solicitudes").hasAuthority("CLIENTES")
            .requestMatchers(HttpMethod.GET, "/solicitudes").hasAnyAuthority("CLIENTES", "TRABAJADOR")
            .requestMatchers(HttpMethod.GET, "/solicitudes/{id}").hasAnyAuthority("CLIENTES", "TRABAJADOR")
            .requestMatchers(HttpMethod.PUT, "/solicitudes/{id}").hasAnyAuthority("CLIENTES", "TRABAJADOR")
            .requestMatchers(HttpMethod.DELETE, "/solicitudes/{id}").hasAuthority("ADMINISTRADOR")
            .requestMatchers("/solicitudes/**/asignar").hasAuthority("TRABAJADOR")
            .requestMatchers("/solicitudes/**/finalizar").hasAuthority("TRABAJADOR")
            .requestMatchers("/solicitudes/**/comenzar").hasAuthority("TRABAJADOR")
            
            // Operaciones específicas de cliente
            .requestMatchers("/solicitudes/**/aceptar").hasAuthority("CLIENTES")
            .requestMatchers("/solicitudes/**/rechazar").hasAuthority("CLIENTES")
            .requestMatchers("/solicitudes/**/cancelar").hasAuthority("CLIENTES")
            .requestMatchers("/solicitudes/**/valorar").hasAuthority("CLIENTES")
            
            // Rutas MENSAJE
            .requestMatchers(HttpMethod.POST, "/mensajes/enviar").hasAnyAuthority("CLIENTES", "TRABAJADOR", "ADMINISTRADOR")
	        .requestMatchers(HttpMethod.GET, "/mensajes/**").hasAnyAuthority("CLIENTES", "TRABAJADOR", "ADMINISTRADOR")
	        .requestMatchers(HttpMethod.DELETE, "/mensajes/**").hasAnyAuthority("CLIENTES", "TRABAJADOR", "ADMINISTRADOR")
	        .requestMatchers(HttpMethod.POST, "/mensajes/broadcast").hasAuthority("ADMINISTRADOR")
	        .requestMatchers(HttpMethod.GET, "/mensajes").hasAuthority("ADMINISTRADOR")
            
            // Rutas TUTORIALES
            .requestMatchers(HttpMethod.GET, "/tutoriales/**").permitAll() 
            .requestMatchers(HttpMethod.POST, "/tutoriales").hasAuthority("TRABAJADOR")
            .requestMatchers(HttpMethod.PUT, "/tutoriales/**").hasAuthority("TRABAJADOR")
            .requestMatchers(HttpMethod.DELETE, "/tutoriales/**").hasAnyAuthority("TRABAJADOR", "ADMINISTRADOR")
            
            // Rutas PERFIL SOCIAL
            .requestMatchers(HttpMethod.POST, "/perfilSocial").hasAnyAuthority("CLIENTES", "TRABAJADOR")
            .requestMatchers(HttpMethod.PUT, "/perfilSocial/**").hasAnyAuthority("CLIENTES", "TRABAJADOR")
            .requestMatchers(HttpMethod.DELETE, "/perfilSocial/**").hasAnyAuthority("CLIENTES", "TRABAJADOR")
            .requestMatchers(HttpMethod.GET, "/perfilSocial").hasAuthority("ADMINISTRADOR")
            .requestMatchers(HttpMethod.GET, "/perfilSocial/**").hasAuthority("ADMINISTRADOR")
            
            // Endpoints de Categoría
            .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
            .requestMatchers("/categorias/**").hasAuthority("ADMINISTRADOR")
            
            // Reglas para Tarea
            .requestMatchers("/tareas/**").hasAnyAuthority("CLIENTES", "TRABAJADOR")
            
            // Rutas SWAGGER
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
            
            // Resto de rutas requieren autenticación
            .anyRequest().authenticated())
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(JWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}