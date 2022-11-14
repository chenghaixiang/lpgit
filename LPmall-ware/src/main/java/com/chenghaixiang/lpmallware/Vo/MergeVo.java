package com.chenghaixiang.lpmallware.Vo;

import lombok.Data;

import java.util.List;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
