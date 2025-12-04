package com.github.stoynko.easydoc.web.utilities;

import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.resolvers.RoleViewResolver;
import com.github.stoynko.easydoc.web.resolvers.RoleViewResolverRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static com.github.stoynko.easydoc.user.model.AccountRole.PATIENT;

@Component
@RequiredArgsConstructor
public class PageBuilder {

    private final RoleViewResolverRegistry viewResolverRegistry;


    public ModelAndView buildPage(DtoContext dtoContext) {

        RoleViewResolver resolver = (dtoContext.principal() != null)
                ? viewResolverRegistry.getResolverForRole(dtoContext.principal().getRole())
                : viewResolverRegistry.getResolverForRole(PATIENT);

        Map<String, Object> modelData = resolver.buildModelData(dtoContext);
        return new ModelAndView(dtoContext.viewPage().getPage()).addAllObjects(modelData);
    }

    public <T> ModelAndView addErrors(ModelAndView modelAndView, String dtoName, T dtoRequest, BindingResult bindingResult) {
        modelAndView.addObject(dtoName, dtoRequest);
        modelAndView.addObject(BindingResult.MODEL_KEY_PREFIX + dtoName, bindingResult);
        return modelAndView;
    }
}