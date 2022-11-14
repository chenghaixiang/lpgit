package com.chenghaixiang.lpmallware.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghaixiang.lpmallware.entity.WareInfoEntity;
import com.chenghaixiang.lpmallware.service.WareInfoService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.R;



/**
 * 仓库信息
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 17:09:52
 */
@RestController
@RequestMapping("lpmallware/wareinfo")
public class WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;

    /**
     * 列表仓库列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lpmallware:wareinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("lpmallware:wareinfo:info")
    public R info(@PathVariable("id") Long id){
		WareInfoEntity wareInfo = wareInfoService.getById(id);

        return R.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lpmallware:wareinfo:save")
    public R save(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.save(wareInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lpmallware:wareinfo:update")
    public R update(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.updateById(wareInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lpmallware:wareinfo:delete")
    public R delete(@RequestBody Long[] ids){
		wareInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
