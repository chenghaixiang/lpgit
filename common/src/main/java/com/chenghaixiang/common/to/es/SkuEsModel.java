package com.chenghaixiang.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Data
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private BigDecimal skuPrice;
    private String skuTitle;
    private String skuIamge;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attrs> atts;


    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
