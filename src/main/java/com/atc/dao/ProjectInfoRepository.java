package com.atc.dao;

import com.atc.dao.entity.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo,Integer>, JpaSpecificationExecutor<ProjectInfo> {

    @Modifying
    @Query("update ProjectInfo p set p.longitude = :x ,p.latitude = :y where p.projectId = :id")
    int updateById(@Param("id") int projectId,@Param("x") String x,@Param("y") String y);
}
