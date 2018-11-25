package com.atc.dao;

import com.atc.entity.RegisterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterInfoRepository extends JpaRepository<RegisterInfo,Integer> {
    List<RegisterInfo> findByUserNameAndPhone(String userName, String phone);

    int countByUserNameAndPhone(String userName, String phone);
}
