package manyWorker.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import manyWorker.entity.Actor;
import manyWorker.service.ActorService;
import manyWorker.service.AdminService;
import manyWorker.service.ClienteService;
import manyWorker.service.TrabajadorService;

@Component
public class JWTUtils {
	@Autowired
	private ActorService actorService;

	@Autowired
	@Lazy
	private AdminService adminService;
	
	@Autowired
	@Lazy
	private TrabajadorService trabajadorService;
	
	@Autowired
	@Lazy
	private ClienteService clienteService;

	private static final String JWT_FIRMA = "ManyWorker";
	private static final long EXTENCION_TOKEN = 86400 * 1000L;

	public String getToken(HttpServletRequest request) {
		String tokenBearer = request.getHeader("Authorization");
		if (StringUtils.hasText(tokenBearer) && tokenBearer.startsWith("Bearer ")) {
			return tokenBearer.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(JWT_FIRMA).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			throw new AuthenticationCredentialsNotFoundException("JWT ha experido o no es valido");
		}
	}

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date fechaActual = new Date();
		Date fechaExpiracion = new Date(fechaActual.getTime() + EXTENCION_TOKEN);
		String rol = authentication.getAuthorities().iterator().next().getAuthority();
		String token = Jwts.builder().setSubject(username).setIssuedAt(fechaActual).setExpiration(fechaExpiracion)
				.claim("rol", rol).signWith(SignatureAlgorithm.HS512, JWT_FIRMA).compact();
		return token;
	}

	public String getUsernameOfToken(String token) {
		return Jwts.parser().setSigningKey(JWT_FIRMA).parseClaimsJws(token).getBody().getSubject();
	}

	public <T> T userLogin() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (StringUtils.isEmpty(username)) {
			return null;
		}
		Optional<Actor> actorO = actorService.findByUsername(username);
		if (!actorO.isPresent()) {
			return null;
		}
		Actor actor = actorO.get();
		switch (actor.getRol()) {
		case ADMINISTRADOR:
			return (T) adminService.findByUsername(username).orElse(null);
		case TRABAJADOR: 
            return (T) trabajadorService.findByUsername(username).orElse(null);
		case CLIENTE: 
            return (T) clienteService.findByUsername(username).orElse(null);
		default:
			return null;
		}
	}
}
