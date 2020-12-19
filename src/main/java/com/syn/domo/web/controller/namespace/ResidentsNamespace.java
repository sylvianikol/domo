package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("all")
@RequestMapping(ResidentsNamespace.URI_RESIDENTS)
public interface ResidentsNamespace {

    String URI_RESIDENTS =
            BuildingsNamespace.URI_BUILDING +
            ApartmentsNamespace.URI_APARTMENTS +
            "/{apartmentId}/residents";
}
