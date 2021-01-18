package com.syn.domo.specification;

import com.syn.domo.model.entity.Child;
import com.syn.domo.model.entity.Resident;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class ChildFilterSpecification implements Specification<Child> {

    private final String buildingId;
    private final String apartmentId;
    private final String parentId;

    public ChildFilterSpecification(String buildingId, String apartmentId, String parentId) {
        this.buildingId = buildingId;
        this.apartmentId = apartmentId;
        this.parentId = parentId;
    }

    @Override
    public Predicate toPredicate(Root<Child> root, CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (buildingId != null) {
            predicate.getExpressions().add(
                    cb.and(cb.equal(root.get("apartment").get("building").get("id"), this.buildingId))
            );
        }

        if (apartmentId != null) {
            predicate.getExpressions().add(
                    cb.and(cb.equal(root.get("apartment").get("id"), this.apartmentId))
            );
        }

        if (parentId != null) {
            Join<Child, Resident> parents = root.join("parents", JoinType.LEFT);

            predicate.getExpressions().add(
                    cb.equal(parents.get("id"), this.parentId)
            );
        }

        return predicate;
    }
}
