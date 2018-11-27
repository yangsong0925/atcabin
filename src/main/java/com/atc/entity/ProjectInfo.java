package com.atc.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "t_project_info")
public class ProjectInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int projectId;
    private String projectName;
    private Date beginTime;
    private Date endTime;
    private String userId;
    private String code;

}
