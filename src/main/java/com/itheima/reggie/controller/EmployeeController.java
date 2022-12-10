package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Description: 20201003133
 * Param:
 * return:
 * Author:杨浩滨
 * Date: 2022/11/6
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    /**
     *对象注入
     */
    @Autowired
    private EmployeeService employeeService;
    /**
     *登录
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        /**
         *对数据库的密码进行md5加密然后再匹配
         */
        String passWord=employee.getPassword();
        passWord=DigestUtils.md5DigestAsHex(passWord.getBytes());

        /**
         *在数据库中查找是否有对应的账号，这里是username
         */

        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(lambdaQueryWrapper);


        /**
         *如果找不到
         */

        if(emp==null){
            return R.error("登陆失败，账号错误");
        }

        /**
         *如果密码不正确
         */
        if(!emp.getPassword().equals(passWord)){
            return R.error("登录失败，密码错误");
        }

        /**
         *用户账号异常
         */
        if(emp.getStatus()==0){
            return R.error("用户状态异常");
        }

        /**
         *正常情况，用session保存登录id,返回成功信息
         */
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }




    /**
     *退出
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    /**
     *添加
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }



    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        if(!(null==name)){
            queryWrapper.like(Employee::getName,name);
        }

        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }


    /**
     *
     * 根据id修改员工信息
     *
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

//        Long empId=(Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");

    }



    /**
     *
     * 根据id编辑员工信息
     *
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee=employeeService.getById(id);
        return R.success(employee);

    }

}
