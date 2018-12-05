package com.atc.common.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface ExpandCriterion {

    enum Operator {
        EQ, NE, LIKE, GT, LT, GTE, LTE, AND, OR
    }
    Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder);

}
