package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ChildrenNamespace.URI_CHILDREN)
public interface ChildrenNamespace {

    String URI_CHILDREN = ApartmentsNamespace.URI_APARTMENTS +
            "/{apartmentId}/children";
}
