package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(BaseNamespace.BASE_URI)
public interface BaseNamespace {

    String BASE_URI = "/v1";
}
