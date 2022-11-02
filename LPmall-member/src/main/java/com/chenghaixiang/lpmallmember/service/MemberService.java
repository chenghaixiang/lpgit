package com.chenghaixiang.lpmallmember.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.lpmallmember.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 16:57:21
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

