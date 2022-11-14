package com.chenghaixiang.lpmallproduct.Vo;

import lombok.Data;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Data
public class AttrResVo extends AttrVo{
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
