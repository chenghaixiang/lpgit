package com.chenghaixiang.lpmallproduct.controller;

import java.util.Arrays;
import java.util.Map;


import com.chenghaixiang.lpmallproduct.Vo.AttrGroupRelation;
import com.chenghaixiang.lpmallproduct.Vo.AttrResVo;
import com.chenghaixiang.lpmallproduct.Vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghaixiang.lpmallproduct.entity.AttrEntity;
import com.chenghaixiang.lpmallproduct.service.AttrService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.R;



/**
 * 商品属性
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 14:18:13
 */
@RestController
@RequestMapping("lpmallproduct/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lpmallproduct:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    @RequestMapping("/{attrType}/list/{catelogId}")
    //@RequiresPermissions("lpmallproduct:attr:list")
    public R baselist(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId,
                      @PathVariable("attrType") String type){
        PageUtils page =attrService.queryBaseAttrPage(params,catelogId,type);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("lpmallproduct:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
        AttrResVo attrResVo=attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attrResVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lpmallproduct:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lpmallproduct:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lpmallproduct:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }


}
