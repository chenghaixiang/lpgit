package com.chenghaixiang.lpmallware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.chenghaixiang.lpmallware.Vo.MergeVo;
import com.chenghaixiang.lpmallware.Vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chenghaixiang.lpmallware.entity.PurchaseEntity;
import com.chenghaixiang.lpmallware.service.PurchaseService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.R;



/**
 * 采购信息
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 17:09:52
 */
@RestController
@RequestMapping("lpmallware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;


    /**
     * 完成采购
     */
    @RequestMapping("/done")
    public R finish(@RequestBody PurchaseDoneVo doneVo){
        purchaseService.done(doneVo);
        return R.ok();
    }

    /**
     * 领取采购单
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }


    /**
     * 合并采购需求
     * @param mergeVo
     * @return
     */
    // /ware/purchase/merge
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("lpmallware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表 查询未领取的采购单
     */
    @RequestMapping("/unreceive/list")
    //@RequiresPermissions("lpmallware:purchase:list")
    public R unreceivelist(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("lpmallware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("lpmallware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("lpmallware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("lpmallware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
