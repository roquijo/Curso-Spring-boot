package com.devsenior.co.producto.shared.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Convierte el JWT de Keycloak extrayendo los roles de realm_access.roles
 * y los convierte en autoridades de Spring Security con el prefijo ROLE_
 */
public class JwtAuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Combinar las autoridades por defecto del JWT con los roles extraídos de realm_access
        return Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());
    }

    /**
     * Extrae los roles de realm_access.roles del JWT de Keycloak
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        
        if (realmAccess == null) {
            return List.of();
        }

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) realmAccess.get("roles");
        
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }

        // Convertir los roles a autoridades con el prefijo ROLE_ para que funcionen con hasRole()
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
    }
}

