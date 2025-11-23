package com.github.stoynko.easydoc.web.resolvers;

import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.web.dto.DtoContext;

import java.util.Map;

public interface RoleViewResolver {

    AccountRole getSupportedRole();

    Map<String, Object> buildModelData(DtoContext dtoContext);
}
