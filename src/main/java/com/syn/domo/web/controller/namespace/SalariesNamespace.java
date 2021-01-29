package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(SalariesNamespace.URI_SALARIES)
public interface SalariesNamespace {

    String URI_SALARIES = BaseNamespace.BASE_URI + "/salaries";
}
