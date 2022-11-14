package com.chenghaixiang.lpmallproduct.Vo;

import com.chenghaixiang.lpmallproduct.entity.AttrEntity;
import com.chenghaixiang.lpmallproduct.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Data
public class AttrGroupWithAttrsVo extends AttrGroupEntity {
    private List<AttrEntity> attrs;
}
