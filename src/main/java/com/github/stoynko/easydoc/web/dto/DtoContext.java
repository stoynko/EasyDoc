package com.github.stoynko.easydoc.web.dto;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.model.ViewAction;
import com.github.stoynko.easydoc.web.model.ViewFragment;
import com.github.stoynko.easydoc.web.model.ViewPage;

import java.util.UUID;

public record DtoContext(
        ViewPage viewPage,
        ViewFragment viewFragment,
        UserAuthenticationDetails principal,
        UUID resourceId,
        ViewAction action,
        Object content
) {
    public static DtoContext forPage(ViewPage page, UserAuthenticationDetails principal) {

        return new DtoContext(page, null, principal, null, null, null);
    }

    public static DtoContext forFragment(ViewPage page,
                                         ViewFragment fragment,
                                         UserAuthenticationDetails principal) {


        return new DtoContext(page, fragment, principal, null, null, null);
    }

    public static DtoContext forTargetResource(ViewPage page,
                                               UserAuthenticationDetails principal,
                                               UUID targetEntityId) {

        return new DtoContext(page, null, principal, targetEntityId, null, null);
    }

    public static DtoContext forTargetResourceWithAction(ViewPage page,
                                               UserAuthenticationDetails principal,
                                               UUID targetEntityId,
                                               ViewAction action) {

        return new DtoContext(page, null, principal, targetEntityId, action, null);
    }

    public static DtoContext forTargetResourceWithContent(ViewPage page,
                                                           UserAuthenticationDetails principal,
                                                           UUID targetEntityId,
                                                           Object content) {

        return new DtoContext(page, null, principal, targetEntityId, null, content);
    }
}

