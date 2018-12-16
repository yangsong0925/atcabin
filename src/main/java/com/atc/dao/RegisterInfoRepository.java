package com.atc.dao;

import com.atc.dao.entity.RegisterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterInfoRepository extends JpaRepository<RegisterInfo,Integer>, JpaSpecificationExecutor<RegisterInfo> {
    List<RegisterInfo> findByUserNameAndPhone(String userName, String phone);

    int countByUserNameAndPhone(String userName, String phone);

    int countByUserNameAndPhoneAndProjectNameAndBeginTimeAndEndTime(String userName, String phone, @Param("projectName") String project, String startDate, String endDate);
}
