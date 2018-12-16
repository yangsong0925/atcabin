package com.atc.dao;

import com.atc.common.enums.UserEnum;
import com.atc.dao.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Integer> , JpaSpecificationExecutor<UserInfo> {


    UserInfo findByUserId(String userId);

    @Modifying
    @Query("update UserInfo as user set user.status = :status where user.userId = :userId")
    int updateStatusByUserId(@Param("userId")int userId,@Param("status")UserEnum userEnum);

    UserInfo findByPhoneAndPassword(String phone, String passwordMd5);

    int countByUserName(@Param("userName")String userName);

    int countByphone(@Param("phone")String phone);

    Page<UserInfo> findByStatus(@Param("status")UserEnum valueOf, Pageable pageable);

    UserInfo findByUserNameAndPassword(String username, String passwordMd5);
}
