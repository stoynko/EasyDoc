package com.github.stoynko.easydoc.web.dto;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.model.ViewFragment;
import com.github.stoynko.easydoc.web.model.ViewPage;

import java.util.UUID;

public record DtoContext(
        ViewPage viewPage,
        ViewFragment viewFragment,
        UserAuthenticationDetails principal,
        UUID resourceId
) {
    public static DtoContext forPage(ViewPage page, UserAuthenticationDetails principal) {

        return new DtoContext(page, null, principal, null);
    }

    public static DtoContext forFragment(ViewPage page,
                                         ViewFragment fragment,
                                         UserAuthenticationDetails principal) {


        return new DtoContext(page, fragment, principal, null);
    }

    public static DtoContext forTargetResource(ViewPage page,
                                               UserAuthenticationDetails principal,
                                               UUID targetEntityId) {

        return new DtoContext(page, null, principal, targetEntityId);
    }

    public static DtoContext forTargetResourceWithFragment(ViewPage page,
                                                           ViewFragment fragment,
                                                           UserAuthenticationDetails principal,
                                                           UUID targetEntityId) {

        return new DtoContext(page, fragment, principal, targetEntityId);
    }
}

