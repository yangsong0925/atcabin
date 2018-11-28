package com.atc.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "t_register_info")
public class RegisterInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int registerId;
    private String userName;
    private String phone;
    private String projectName;

}
