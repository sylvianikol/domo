package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(FeesNamespace.URI_FEES)
public interface FeesNamespace {

    String URI_FEES = BaseNamespace.BASE_URI + "/fees";
}
