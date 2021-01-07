package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("all")
@RequestMapping(FeesNamespace.URI_FEES)
public interface FeesNamespace {

    String URI_FEES = BuildingsNamespace.URI_BUILDING +
            "/{buildingId}/fees";
}
