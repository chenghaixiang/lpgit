package com.chenghaixiang.lpmallware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.chenghaixiang.lpmallware.Vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghaixiang.lpmallware.entity.WareSkuEntity;
import com.chenghaixiang.lpmallware.service.WareSkuService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.R;



/**
 * 商品库存
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 17:09:52
 */
@RestController
@RequestMapping("lpmallware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 查询sku是否有库存
     */
    @RequestMapping("/hasstock")
    public R getSkuHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockVo> vos=wareSkuService.getSkuHasStock(skuIds);
        return R.ok().put("data",vos);
    }

    /**
     * 列表查询商品库存
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lpmallware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("lpmallware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lpmallware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lpmallware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lpmallware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
