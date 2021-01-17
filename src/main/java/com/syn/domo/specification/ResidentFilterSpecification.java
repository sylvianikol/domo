package com.syn.domo.specification;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Resident;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class ResidentFilterSpecification implements Specification<Resident> {

    private final String buildingId;
    private final String apartmentId;

    public ResidentFilterSpecification(String buildingId, String apartmentId) {
        this.buildingId = buildingId;
        this.apartmentId = apartmentId;
    }

    @Override
    public Predicate toPredicate(Root<Resident> root, CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {

        Predicate predicate = cb.conjunction();

        if (buildingId != null) {

            Join<Resident, Building> buildings =
                    root.join("apartments", JoinType.LEFT)
                            .join("building", JoinType.LEFT);

            predicate.getExpressions().add(cb.equal(buildings.get("id"), this.buildingId));
        }

        if (apartmentId != null) {

            Join<Resident, Apartment> apartments =
                    root.join("apartments", JoinType.LEFT);

            predicate.getExpressions().add(cb.equal(apartments.get("id"), this.apartmentId));
        }

        return predicate;
    }
}
