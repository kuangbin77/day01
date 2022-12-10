package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: 20201003133
 * Param:
 * return:
 * Author:杨浩滨
 * Date: 2022/11/6
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
