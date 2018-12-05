package com.atc.common.criteria;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ExpandCriteria<T> implements Specification<T> {
    private List<ExpandCriterion> criterions = new ArrayList<ExpandCriterion>();

    public static void main(String[] args) {
        //使用示例Demo
//    	Criteria<Entity> c = new Criteria<Entity>();
//    	c.add(Restrictions.like("code", searchParam.getCode(), true));
//    	        c.add(Restrictions.eq("level", searchParam.getLevel(), false));
//    	        c.add(Restrictions.eq("mainStatus", searchParam.getMainStatus(), true));
//    	        c.add(Restrictions.eq("flowStatus", searchParam.getFlowStatus(), true));
//    	        c.add(Restrictions.eq("createUser.userName", searchParam.getCreateUser(), true));
//    	        c.add(Restrictions.lte("submitTime", searchParam.getStartSubmitTime(), true));
//    	        c.add(Restrictions.gte("submitTime", searchParam.getEndSubmitTime(), true));
//    	        c.add(Restrictions.eq("needFollow", searchParam.getIsfollow(), true));
//    	        c.add(Restrictions.ne("flowStatus", searchParam.getMainStatus() true));
//    	        c.add(Restrictions.in("solveTeam.code",teamCodes, true));
//    	repository.findAll(c);
    }

    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        if (!criterions.isEmpty()) {
            List<Predicate> predicates = new ArrayList<Predicate>();
            for (ExpandCriterion c : criterions) {
                predicates.add(c.toPredicate(root, query, builder));
            }
            // 将所有条件用 and 联合起来
            if (predicates.size() > 0) {
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }
        return builder.conjunction();
    }

    /**
     * 增加简单条件表达式
     *
     * @param expression0 void
     * @Methods Name add
     * @Create In 2012-2-8 By lee
     */
    public void add(ExpandCriterion criterion) {
        if (criterion != null) {
            criterions.add(criterion);
        }
    }
}