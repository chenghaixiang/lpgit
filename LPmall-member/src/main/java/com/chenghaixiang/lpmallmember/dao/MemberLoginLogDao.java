package com.chenghaixiang.lpmallmember.dao;

import com.chenghaixiang.lpmallmember.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 16:57:21
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
