package ch.zhaw.freelancer4u.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;


public class RoleExtractor implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    @Nullable
    public AbstractAuthenticationToken convert(Jwt jwt)
    {
        return new JwtAuthenticationToken(jwt, Stream.concat(new JwtGrantedAuthoritiesConverter().convert(jwt)
                .stream(), extractResourceRoles(jwt).stream())
                .collect(Collectors.toSet()));
    }
    
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt)
    {
        var resourceRoles = new ArrayList<>();
        if (jwt.hasClaim("user_roles")) {
            List<String> userRole = jwt.getClaimAsStringList("user_roles");
            userRole.stream().forEach(role -> resourceRoles.add(role));
        }

        return resourceRoles.isEmpty() ? Collections.emptySet() : resourceRoles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toSet());
    }
}