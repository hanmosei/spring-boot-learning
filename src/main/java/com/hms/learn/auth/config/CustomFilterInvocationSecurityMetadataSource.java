package com.hms.learn.auth.config;

import com.hms.learn.domain.MenuEntity;
import com.hms.learn.domain.RoleEntity;
import com.hms.learn.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    MenuRepository menuRepository;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<MenuEntity> menuEntities = menuRepository.findAll();
        for (MenuEntity menuEntity : menuEntities) {
            if (antPathMatcher.match(menuEntity.getPattern(), requestUrl)) {
                List<RoleEntity> roleEntities = menuEntity.getRoles().parallelStream().collect(Collectors.toList());
                String[] roleArr = new String[roleEntities.size()];
                for (int i = 0; i < roleArr.length; i++) {
                    roleArr[i] = roleEntities.get(i).getName();
                }
                return SecurityConfig.createList(roleArr);
            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
