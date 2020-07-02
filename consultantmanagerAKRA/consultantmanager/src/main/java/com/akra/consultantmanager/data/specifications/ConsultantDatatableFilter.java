package com.akra.consultantmanager.data.specifications;

import com.akra.consultantmanager.data.entities.Consultant;

import javax.persistence.criteria.*;
import java.util.ArrayList;

public class ConsultantDatatableFilter implements org.springframework.data.jpa.domain.Specification<Consultant>{

    String userQuery;

    public ConsultantDatatableFilter(String queryString) {
        this.userQuery = queryString;
    }

    @Override
    public Predicate toPredicate(Root<Consultant> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        ArrayList<Predicate> predicates = new ArrayList<>();

        if (userQuery != null && userQuery != "") {
            predicates.add(criteriaBuilder.like(root.get("firstName"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("lastName"), '%' + userQuery + '%'));

        }

        return (! predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
    }
}
