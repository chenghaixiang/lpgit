package com.chenghaixiang.lpmallproduct.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.chenghaixiang.lpmallproduct.Vo.AttrGroupRelation;
import com.chenghaixiang.lpmallproduct.Vo.AttrGroupWithAttrsVo;
import com.chenghaixiang.lpmallproduct.Vo.AttrResVo;
import com.chenghaixiang.lpmallproduct.entity.AttrEntity;
import com.chenghaixiang.lpmallproduct.service.AttrAttrgroupRelationService;
import com.chenghaixiang.lpmallproduct.service.AttrService;
import com.chenghaixiang.lpmallproduct.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghaixiang.lpmallproduct.entity.AttrGroupEntity;
import com.chenghaixiang.lpmallproduct.service.AttrGroupService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.R;



/**
 * 属性分组
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 14:18:12
 */
@RestController
@RequestMapping("lpmallproduct/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

    @RequestMapping("/{attrgroupId}/attr/relation")
    //@RequiresPermissions("lpmallproduct:attrgroup:list")
    public R attrRelation(@PathVariable Long attrgroupId){
        List<AttrEntity> attrEntityList=attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",attrEntityList);
    }

    @RequestMapping("/{attrgroupId}/noattr/relation")
    //@RequiresPermissions("lpmallproduct:attrgroup:list")
    public R attrNoRelation(@PathVariable Long attrgroupId,@RequestParam Map<String, Object> params){

        PageUtils page=attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }


    @RequestMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){
        // 查出当前分类下的所以属性分组
        //查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> vos=attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);

        return R.ok().put("data",vos);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lpmallproduct:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("lpmallproduct:attrgroup:list")
    public R listId(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        System.out.println("带分类id查");
        PageUtils page = attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("lpmallproduct:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long[] catelogPath = categoryService.findCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lpmallproduct:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    @RequestMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelation> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lpmallproduct:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lpmallproduct:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    //删除同时删除关联关系
    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelation[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }


}
