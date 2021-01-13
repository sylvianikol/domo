package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ResidentsNamespace.URI_RESIDENTS)
public interface ResidentsNamespace {

    String URI_RESIDENTS = BaseNamespace.BASE_URI + "/residents";
}
