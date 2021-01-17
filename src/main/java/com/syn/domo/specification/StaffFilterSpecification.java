package com.syn.domo.specification;

import com.syn.domo.model.entity.Staff;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class StaffFilterSpecification implements Specification<Staff> {

    public StaffFilterSpecification() {
    }

    @Override
    public Predicate toPredicate(Root<Staff> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
