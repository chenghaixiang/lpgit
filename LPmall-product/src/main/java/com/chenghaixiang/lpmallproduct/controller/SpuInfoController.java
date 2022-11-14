package com.chenghaixiang.lpmallproduct.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;


import com.chenghaixiang.lpmallproduct.Vo.JsonVo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghaixiang.lpmallproduct.entity.SpuInfoEntity;
import com.chenghaixiang.lpmallproduct.service.SpuInfoService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.R;



/**
 * spu信息
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 14:18:12
 */
@RestController
@RequestMapping("lpmallproduct/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 商品上架
     */
    @RequestMapping("/{spuId}/up")
    public R supUp(@PathVariable Long spuId) throws IOException {
        spuInfoService.up(spuId);
        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lpmallproduct:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page =spuInfoService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("lpmallproduct:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lpmallproduct:spuinfo:save")
    public R save(@RequestBody SpuSaveVo spuSaveVo){
		spuInfoService.saveSpuInfo(spuSaveVo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lpmallproduct:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lpmallproduct:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
