package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(BuildingsNamespace.URI_BUILDINGS)
public interface BuildingsNamespace {
    String URI_BUILDINGS = BaseNamespace.BASE_URI + "/buildings";
}
