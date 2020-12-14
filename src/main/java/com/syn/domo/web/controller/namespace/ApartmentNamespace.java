package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ApartmentNamespace.URI_APARTMENTS)
public interface ApartmentNamespace {
    String URI_APARTMENTS = "/apartments";
}
