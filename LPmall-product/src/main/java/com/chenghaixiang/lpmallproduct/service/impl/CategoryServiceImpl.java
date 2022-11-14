package com.chenghaixiang.lpmallproduct.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chenghaixiang.lpmallproduct.Vo.Catelog2Vo;
import com.chenghaixiang.lpmallproduct.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.Query;

import com.chenghaixiang.lpmallproduct.dao.CategoryDao;
import com.chenghaixiang.lpmallproduct.entity.CategoryEntity;
import com.chenghaixiang.lpmallproduct.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithtree() {
        /** 查所有分类 **/
        List<CategoryEntity> entities = baseMapper.selectList(null);

        /**
         * 找到一级分类
         */
        List<CategoryEntity> menus1 = entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(menu->{
                    menu.setChildren(getChilrens(menu,entities));
                    return menu; })
                .sorted((menu1,menu2)->{
                    return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort()); })
                .collect(Collectors.toList());

        return menus1;
    }

    @Override
    public void removeMenuByids(List<Long> asList) {
        //TODO 1.检查当前删除菜单
        //
        baseMapper.deleteBatchIds(asList);
    }

    private List<CategoryEntity> getChilrens(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> chilren = all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId())
                .map(categoryEntity -> {
                    categoryEntity.setChildren(getChilrens(categoryEntity,all));
                    return categoryEntity; })
                .sorted((menu1,menu2)->{
                    return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort());})
                .collect(Collectors.toList());
        return chilren;
    }
    // 找到当前分类的分类路径
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths =new ArrayList<>();

        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    // 更新时，更新与它相关的表信息
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    @Override
    public List<CategoryEntity> getLevelOneCategorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    /**
     * 从数据库查3级分类有缓存
     * @return
     */
    @Override
    public Map<Long, List<Catelog2Vo>> getCatalogJson(){
        /**
         * 1.解决缓存穿透 ，空值缓存
         * 2.解决缓存雪崩 ，随机过期时间
         * 3.解决缓存击穿， 加锁
         */
        // 查询redis数据库是否有数据
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if(StringUtils.isEmpty(catalogJson)){
            Map<Long, List<Catelog2Vo>> catalogJsonFromdb = getCatalogJsonFromdb();
            String s = JSON.toJSONString(catalogJsonFromdb);
            stringRedisTemplate.opsForValue().set("catalogJson",s,1, TimeUnit.DAYS);
            return catalogJsonFromdb;
        }
        // 将redis数据库的JSON数据反序列化成一个对象
        Map<Long, List<Catelog2Vo>> result=JSON.parseObject(catalogJson,new TypeReference<Map<Long, List<Catelog2Vo>>>(){});
        return result;
    }
    /**
     * 从数据库查3级分类无缓存
     * @return
     */
    public Map<Long, List<Catelog2Vo>> getCatalogJsonFromdb() {
        synchronized (this) {
            String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
            if(StringUtils.isEmpty(catalogJson)){
                Map<Long, List<Catelog2Vo>> result=JSON.parseObject(catalogJson,new TypeReference<Map<Long, List<Catelog2Vo>>>(){});
                return result;
            }
            List<CategoryEntity> selectList = baseMapper.selectList(null);

            // 查出所有1级分类
            List<CategoryEntity> levelOneCategorys = getParent_cid(selectList, 0L);

            Map<Long, List<Catelog2Vo>> parent_id = levelOneCategorys.stream().collect(Collectors.toMap(k -> k.getCatId(), v -> {
                // 每一个一级分类的二级分类
                List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
                List<Catelog2Vo> catelog2Vos = null;
                if (categoryEntities != null) {
                    catelog2Vos = categoryEntities.stream().map(item -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, item.getCatId().toString(), item.getName());

                        // 当前二级分类的的三级分类封装成vo3
                        List<CategoryEntity> level3Catelog = getParent_cid(selectList, item.getCatId());
                        if (level3Catelog != null) {
                            List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                                Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(item.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                return catelog3Vo;
                            }).collect(Collectors.toList());
                            catelog2Vo.setCatalog3List(collect);
                        }
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }
                return catelog2Vos;
            }));
            return parent_id;
        }
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList,Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> {
            return item.getParentCid()==parent_cid ;
        }).collect(Collectors.toList());
        return collect;
//        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
    }


}