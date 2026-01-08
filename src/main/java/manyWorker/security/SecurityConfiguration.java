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
					.requestMatchers("/tutoriales/**").permitAll()

					// Rutas ADMINISTRADOR
					.requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR")
		            .requestMatchers(HttpMethod.GET, "/trabajador").hasAuthority("ADMINISTRADOR")
		            .requestMatchers(HttpMethod.GET, "/cliente", "/cliente/*").hasAuthority("ADMINISTRADOR")
					.requestMatchers(HttpMethod.PUT, "/banear/**").hasAuthority("ADMINISTRADOR")
					.requestMatchers(HttpMethod.PUT, "/desbanear/**").hasAuthority("ADMINISTRADOR")
					.requestMatchers("/categorias/**").hasAuthority("ADMINISTRADOR")
					.requestMatchers("/mensajes/broadcast").hasAuthority("ADMINISTRADOR")

					// Rutas TRABAJADOR
					.requestMatchers("/trabajador/**").hasAuthority("TRABAJADOR")
					.requestMatchers("/solicitudes", "/solicitudes/**").hasAuthority("TRABAJADOR")
					.requestMatchers(HttpMethod.PUT, "/trabajador").hasAuthority("TRABAJADOR")
					.requestMatchers(HttpMethod.DELETE, "/trabajador").hasAuthority("TRABAJADOR")

					// Rutas CLIENTE
					.requestMatchers("/cliente/**").hasAuthority("CLIENTES")
					.requestMatchers("/tareas/**").hasAuthority("CLIENTES")
					.requestMatchers("/solicitudes/**/aceptar").hasAuthority("CLIENTES")
					.requestMatchers("/solicitudes/**/rechazar").hasAuthority("CLIENTES")
					.requestMatchers(HttpMethod.PUT, "/cliente").hasAuthority("CLIENTES")
					.requestMatchers(HttpMethod.DELETE, "/cliente").hasAuthority("CLIENTES")
					.requestMatchers("/cliente/miPerfil").hasAuthority("CLIENTES")

					// Endpoints compartidos (CLIENTE y TRABAJADOR)
					.requestMatchers("/mensajes/**").hasAnyAuthority("CLIENTES", "TRABAJADOR")
					.requestMatchers("/perfilSocial/**").hasAnyAuthority("CLIENTES", "TRABAJADOR")
					
					// Poned los endpoints de la tarea 8 aqui ordenados (cada uno los endpoint de las entity q hicisteis en la primera parte)
					
					// Endpoints de Categoría
					// Permitir ver categorías a todo el mundo (público)
					.requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
					// Solo el ADMIN puede crear, editar o borrar categorías
					.requestMatchers("/categorias/**").hasAuthority("ADMINISTRADOR")
					
					// Reglas para Tarea
					// Tanto CLIENTES (quien crea la tarea) como TRABAJADOR (quien la realiza) deben tener acceso 
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