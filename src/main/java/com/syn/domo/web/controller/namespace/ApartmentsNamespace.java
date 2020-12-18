package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("all")
@RequestMapping(BuildingsNamespace.URI_BUILDING + ApartmentsNamespace.URI_APARTMENTS)
public interface ApartmentsNamespace {

    String URI_APARTMENTS = BuildingsNamespace.URI_BUILDING + "/{buildingId}/apartments";
}
