package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ChildrenNamespace.URI_CHILDREN)
public interface ChildrenNamespace {

    String URI_CHILDREN = BaseNamespace.BASE_URI + "/children";
}
