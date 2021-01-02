package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("all")
@RequestMapping(UsersNamespace.URI_USERS)
public interface UsersNamespace {

    String URI_USERS = ApartmentsNamespace.URI_APARTMENTS +
            "/{apartmentId}/users";
}
