package com.chenghaixiang.lpmallware.Vo;

import lombok.Data;

import java.util.List;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Data
public class PurchaseDoneVo {
    private Long id;

    private List<PurchaseItemDoneVo> items;
}
