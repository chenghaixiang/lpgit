package com.chenghaixiang.lpmallware.Vo;

import lombok.Data;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Data
public class PurchaseItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
