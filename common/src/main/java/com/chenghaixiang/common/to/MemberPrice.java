package com.chenghaixiang.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Data
public class MemberPrice {
    private Long id;
    private String name;
    private BigDecimal price;
}
