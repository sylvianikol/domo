package com.syn.domo.specification;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Resident;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class ApartmentFilterSpecification implements Specification<Apartment> {

    private final String buildingId;

    public ApartmentFilterSpecification(String buildingId) {
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
