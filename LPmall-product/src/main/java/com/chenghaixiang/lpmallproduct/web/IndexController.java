package com.chenghaixiang.lpmallproduct.web;

import com.chenghaixiang.lpmallproduct.Vo.Catelog2Vo;
import com.chenghaixiang.lpmallproduct.entity.CategoryEntity;
import com.chenghaixiang.lpmallproduct.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/",".index.html"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities=categoryService.getLevelOneCategorys();

        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<Long, List<Catelog2Vo>> getCatalogJson(){
        Map<Long, List<Catelog2Vo>> map=categoryService.getCatalogJson();
        return map;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
