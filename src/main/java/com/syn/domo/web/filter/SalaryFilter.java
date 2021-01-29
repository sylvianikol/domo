package com.syn.domo.web.filter;

import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Salary;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class SalaryFilter implements Specification<Salary> {

    private final String  buildingId;
    private final String  staffId;
    private final Boolean isPaid;

    public SalaryFilter(String buildingId, String staffId, Boolean isPaid) {
        this.buildingId = buildingId;
        this.staffId = staffId;
        this.isPaid = isPaid;
    }

    @Override
    public Predicate toPredicate(Root<Salary> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        Predicate predicate = cb.conjunction();

        if (buildingId != null) {
            Join<Salary, Building> buildings = root.join("buildings", JoinType.LEFT);

            predicate.getExpressions().add(
                    cb.and(cb.equal(buildings.get("id"), this.buildingId))
            );
        }

        if (this.staffId != null) {
            predicate.getExpressions().add(
                    cb.and(cb.equal(root.get("staff").get("id"), this.staffId))
            );
        }

        if (this.isPaid != null) {
            predicate.getExpressions().add(
                    cb.and(cb.equal(root.get("isPaid"), this.isPaid))
            );
        }

        return predicate;
    }
}
