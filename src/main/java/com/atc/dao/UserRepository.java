package com.atc.dao;

import com.atc.dao.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Integer> {


    UserInfo findByUserId(String userId);

//    @Modifying
//    @Query("update UserInfo as user set user.code = :code where user.userId = :userId")
//    int updateCodeByUserId(@Param("userId") String userId,@Param("code") String code);

    UserInfo findByUserNameAndPassword(@Param("username") String username, @Param("password") String password);

    int countByLoginName(@Param("loginName") String loginName);

    int countByLoginNameOrPhone(@Param("loginName") String loginName, @Param("phone") String phone);

    int countByphone(@Param("phone") String phone);

    UserInfo findByLoginNameAndPassword(@Param("loginName") String loginName, @Param("password") String password);
}
