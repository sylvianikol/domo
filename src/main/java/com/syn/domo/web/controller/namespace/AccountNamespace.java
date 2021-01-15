package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(AccountNamespace.URI_ACCOUNT)
public interface AccountNamespace {

    String URI_ACCOUNT = BaseNamespace.BASE_URI + "/account";
}
