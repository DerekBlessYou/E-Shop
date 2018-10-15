package com.derek.product.service;

import com.derek.product.dataobject.ProductCategory;

import java.util.List;

public interface CategoryService {
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
