package com.syn.domo.web.filter;

import com.syn.domo.model.entity.Apartment;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class ApartmentFilter implements Specification<Apartment> {

    private final String buildingId;

    public ApartmentFilter(String buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public Predicate toPredicate(Root<Apartment> root, CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {

        Predicate predicate = cb.conjunction();

        if (buildingId != null) {
            predicate.getExpressions().add(cb.equal(root.get("building").get("id"), this.buildingId));
        }

        return predicate;
    }
}
