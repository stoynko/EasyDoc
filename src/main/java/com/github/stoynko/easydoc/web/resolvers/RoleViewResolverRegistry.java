package com.github.stoynko.easydoc.web.resolvers;

import com.github.stoynko.easydoc.shared.exception.ResolverNotFoundException;
import com.github.stoynko.easydoc.user.model.AccountRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import java.util.EnumMap;
import java.util.List;

@Component
public class RoleViewResolverRegistry {

    private final EnumMap<AccountRole, RoleViewResolver> map;
    private final View view;

    public RoleViewResolverRegistry(List<RoleViewResolver> resolvers, View view) {
        this.map = new EnumMap<>(AccountRole.class);

        for (RoleViewResolver resolver : resolvers) {
            if (map.containsKey(resolver.getSupportedRole())) {
                continue;
            }
            map.putIfAbsent(resolver.getSupportedRole(), resolver);
        }
        this.view = view;
    }

    public RoleViewResolver getResolverForRole(AccountRole role) {
        RoleViewResolver resolver = map.get(role);
        if (resolver == null) {
            throw new ResolverNotFoundException();
        }
        return resolver;
    }


}
