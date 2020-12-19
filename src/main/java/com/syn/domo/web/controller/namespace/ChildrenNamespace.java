package com.syn.domo.web.controller.namespace;

import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("all")
@RequestMapping(ChildrenNamespace.URI_CHILDREN)
public interface ChildrenNamespace {

    String URI_CHILDREN =
            BuildingsNamespace.URI_BUILDING +
            ApartmentsNamespace.URI_APARTMENTS +
            "/{apartmentId}/children";
}
