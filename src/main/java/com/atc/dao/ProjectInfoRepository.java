package com.atc.dao;

import com.atc.dao.entity.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo,Integer> {
}
