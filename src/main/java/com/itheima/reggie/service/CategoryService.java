package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * Description: 20201003133
 * Param:
 * return:
 * Author:杨浩滨
 * Date: 2022/11/10
 */

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
