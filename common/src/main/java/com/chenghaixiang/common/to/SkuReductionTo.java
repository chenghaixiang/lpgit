package com.chenghaixiang.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
