package com.bank.transfer.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter;
    private final ObjectMapper objectMapper;

    public KeycloakJwtAuthenticationConverter(@Autowired ObjectMapper objectMapper) {
        this.defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        this.objectMapper = objectMapper;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream
                .concat(defaultGrantedAuthoritiesConverter.convert(jwt).stream(), extractAuthorities(jwt).stream())
                .collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Set<String> roles = new HashSet<>();
        roles.addAll(getRealmRoles(jwt));
        roles.addAll(getResourceRoles(jwt));
        return AuthorityUtils.createAuthorityList(roles.toArray(new String[0]));
    }

    private Set<String> getRealmRoles(Jwt jwt) {
        Set<String> realmRoles = new HashSet<>();
        JsonNode json = objectMapper.convertValue(jwt.getClaim("realm_access"), JsonNode.class);
        json.elements().forEachRemaining(
                e -> e.elements().forEachRemaining(r -> realmRoles.add(r.asText().toUpperCase())));
        return realmRoles;
    }

    private Set<String> getResourceRoles(Jwt jwt) {
        Set<String> roles = new HashSet<>();
        Map<String, JsonNode> map = objectMapper.convertValue(jwt.getClaim("resource_access"),
                new TypeReference<Map<String, JsonNode>>() {
                });
        for (Map.Entry<String, JsonNode> jsonNode : map.entrySet()) {
            jsonNode
                    .getValue()
                    .elements()
                    .forEachRemaining(e -> e
                            .elements()
                            .forEachRemaining(r -> roles.add(r.asText().toUpperCase())));
        }
        return roles;
    }
}