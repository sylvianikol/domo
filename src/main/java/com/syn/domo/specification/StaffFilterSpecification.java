package com.syn.domo.specification;

import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Staff;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class StaffFilterSpecification implements Specification<Staff> {

    private final String buildingId;

    public StaffFilterSpecification(String buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public Predicate toPredicate(Root<Staff> root, CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (buildingId != null) {
            Join<Staff, Building> buildings = root.join("buildings", JoinType.LEFT);

            predicate.getExpressions().add(
                    cb.and(cb.equal(buildings.get("id"), this.buildingId))
            );
        }

        return predicate;
    }
}
