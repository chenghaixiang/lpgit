package com.chenghaixiang.lpmallsearch.service;

import com.chenghaixiang.common.to.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */

public interface ProductService {
    Boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
