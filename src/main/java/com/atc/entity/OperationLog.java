package com.atc.entity;

import com.atc.common.enums.CabinEnum;
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
@Table(name = "t_operation_log")
public class OperationLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int    operationId;
    @Enumerated(EnumType.ORDINAL)
    private CabinEnum openStatus;
    @Enumerated(EnumType.ORDINAL)
    private CabinEnum closeStatus;
    private Date   openTime;
    private Date   closeTime;
    private String userId;
    private String projectName;

}
