package com.syn.domo.specification;

import com.syn.domo.model.entity.Fee;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FeeFilterSpecification implements Specification<Fee> {

    private final String buildingId;
    private final String apartmentId;

    public FeeFilterSpecification(String buildingId, String apartmentId) {
        this.buildingId = buildingId;
        this.apartmentId = apartmentId;
    }

    @Override
    public Predicate toPredicate(Root<Fee> root, CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (this.buildingId != null) {
            predicate.getExpressions().add(
                    cb.and(cb.equal(root.get("apartment")
                            .get("building").get("id"), this.buildingId))
            );
        }

        if (this.apartmentId != null) {
            predicate.getExpressions().add(
                    cb.and(cb.equal(root.get("apartment").get("id"), this.apartmentId))
            );
        }

        return predicate;
    }
}
