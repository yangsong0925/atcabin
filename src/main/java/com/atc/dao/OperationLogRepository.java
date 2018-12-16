package com.atc.dao;

import com.atc.dao.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog,Integer> {

    int countByOperationIdAndCloseTimeIsNull(String operationId);

    @Modifying
    @Query("update OperationLog operationLog set operationLog.closeTime = :endTime where operationLog.operationId = :id")
    int updateEndTimeById(@Param("id") int id, @Param("endTime") Date endTime);

    Page<OperationLog> findByProjectId(Integer projectId, Pageable pageable);

    OperationLog findByUserIdAndCloseTimeIsNull(Integer userId);

    @Modifying
    @Query("update OperationLog operationLog set operationLog.remark = :remark where operationLog.operationId = :id")
    int updateRemarkById(@Param("id") String operationId,@Param("remark") String remark);

    int countByUserIdAndCloseTimeIsNull(@Param("userId") int userId);
}
