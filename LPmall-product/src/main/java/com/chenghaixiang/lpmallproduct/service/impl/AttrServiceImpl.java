package com.chenghaixiang.lpmallproduct.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenghaixiang.lpmallproduct.Vo.AttrGroupRelation;
import com.chenghaixiang.lpmallproduct.Vo.AttrResVo;
import com.chenghaixiang.lpmallproduct.Vo.AttrVo;
import com.chenghaixiang.lpmallproduct.dao.AttrAttrgroupRelationDao;
import com.chenghaixiang.lpmallproduct.dao.AttrGroupDao;
import com.chenghaixiang.lpmallproduct.dao.CategoryDao;
import com.chenghaixiang.lpmallproduct.entity.AttrAttrgroupRelationEntity;
import com.chenghaixiang.lpmallproduct.entity.AttrGroupEntity;
import com.chenghaixiang.lpmallproduct.entity.CategoryEntity;
import com.chenghaixiang.lpmallproduct.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.Query;

import com.chenghaixiang.lpmallproduct.dao.AttrDao;
import com.chenghaixiang.lpmallproduct.entity.AttrEntity;
import com.chenghaixiang.lpmallproduct.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    // ????????????????????????Vo??????????????????
    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        // ??????????????????
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.save(attrEntity);

        // ??????????????????
        if(attr.getAttrType()==1&&attr.getAttrGroupId()!=null){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }

    }

    // ????????????
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type","base".equalsIgnoreCase(type)?1:0);
        if (catelogId!=0){
            queryWrapper.eq("catelog_id",catelogId);
        }
        String key = (String) params.get("key");

        if(!StringUtils.hasLength(key)){
            queryWrapper.and(wrapper->{
               wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        List<AttrEntity> records = page.getRecords();

        List<AttrResVo> list = records.stream().map(attrEntity -> {
            AttrResVo attrResVo = new AttrResVo();
            BeanUtils.copyProperties(attrEntity, attrResVo);

            if("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity attr_id = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));

                if (attr_id != null&&attr_id.getAttrGroupId()!=null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attr_id.getAttrGroupId());
                    attrResVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }


            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrResVo.setCatelogName(categoryEntity.getName());
            }
            return attrResVo;
        }).collect(Collectors.toList());

        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(list);
        return pageUtils;
    }

    // ??????id????????????
    @Override
    public AttrResVo getAttrInfo(Long attrId) {
        AttrResVo resVo=new AttrResVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,resVo);

        // ??????????????????
        AttrAttrgroupRelationEntity attrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if(attrgroupRelationEntity!=null){
            resVo.setAttrGroupId(attrgroupRelationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupRelationEntity.getAttrGroupId());
            if(attrGroupEntity!=null){
                resVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }

        // ??????????????????
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);

        resVo.setCatelogPath(catelogPath);

        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity!=null){
            resVo.setCatelogName(categoryEntity.getName());
        }
        return resVo;
    }

    // ???????????????????????????
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity=new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        AttrAttrgroupRelationEntity relationEntity=new AttrAttrgroupRelationEntity();

        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attr.getAttrId());
        Integer count=attrAttrgroupRelationDao.selectCount(new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
        // ??????????????????????????????
        if(count>0){
            attrAttrgroupRelationDao.update(relationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
        }else {
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }

    // ????????????id????????????????????????
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        if(!relationEntities.isEmpty()){
            List<Long> attrIds = relationEntities.stream().map(attr -> {
                return attr.getAttrId();
            }).collect(Collectors.toList());
            List<AttrEntity> attrEntities = this.listByIds(attrIds);
            return attrEntities;
        }else {
            return null;
        }
    }

    // ??????????????????????????????
    @Override
    public void deleteRelation(AttrGroupRelation[] vos) {
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map(item -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }

    //?????????????????????????????????????????????
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        // ????????????????????????
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        // ?????????????????????????????????????????????
        List<AttrGroupEntity> groupEntityList = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> collect = groupEntityList.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());

        // ?????????????????????????????????????????????????????????????????????
        List<AttrAttrgroupRelationEntity> groupId=new ArrayList<>();
        if(!collect.isEmpty()){
            groupId = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        }
        List<Long> longList = groupId.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        // ??????????????????????????????,???????????????
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type",1);
        if(!longList.isEmpty()){
            wrapper.notIn("attr_id", longList);
        }
        // ????????????

        String key= (String) params.get("key");
        if (StringUtils.isEmpty(key)){
            wrapper.and(w->{
                w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        Page<AttrEntity> page = this.page(new Page<AttrEntity>(),wrapper);

        return new PageUtils(page);
    }

    @Override
    public List<Long> selectSearchAtts(List<Long> attrIds) {
        return this.baseMapper.selectSearchAttsIds(attrIds);
    }

}