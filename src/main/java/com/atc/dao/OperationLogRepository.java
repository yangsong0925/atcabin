package com.atc.dao;

import com.atc.dao.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog,Integer> {

    int countByOperationIdAndCloseTimeIsNull(String operationId);

}
