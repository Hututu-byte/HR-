package com.lee.vo;

import com.lee.entity.BSalaryRecord;
import lombok.Data;

@Data
public class BSalaryRecordVO extends BSalaryRecord {
    private String name;
    private String deptname;
}
