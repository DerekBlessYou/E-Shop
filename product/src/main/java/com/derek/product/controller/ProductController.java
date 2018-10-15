package com.derek.product.controller;

import com.derek.product.dto.CartDTO;
import com.derek.product.dataobject.ProductCategory;
import com.derek.product.dataobject.ProductInfo;
import com.derek.product.service.CategoryService;
import com.derek.product.service.ProductService;
import com.derek.product.utils.ResultVoUtils;
import com.derek.product.vo.ProductInfoVo;
import com.derek.product.vo.ProductVo;
import com.derek.product.vo.ResultVo;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 *商品
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 1.查询所有在架的商品
     * 2.获取类目type列表
     * 3.查询类目
     * 4.构造数据
     */
    @GetMapping("/list")
    public ResultVo<ProductVo> list(){
        //查询所有在架的商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        //获取类目type列表
        List<Integer> categoryTypeList = productInfoList.stream().map(ProductInfo::getCategoryType).collect(Collectors.toList());
        //从数据库查询类目
        List<ProductCategory> categoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
        //构造数据
        List<ProductVo> productVoList = Lists.newArrayList();
        for (ProductCategory productCategory: categoryList){
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVo> productInfoVoList = Lists.newArrayList();
            for(ProductInfo productInfo: productInfoList){
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    //copy对象，将productInfo对象中的内容copy到productInfoVo中去
                    BeanUtils.copyProperties(productInfo, productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
            }
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);
        }

        return ResultVoUtils.success(productVoList);
    }

    /**
     * 获取商品列表(给订单服务用的)
     * @param productIdList
     * @return
     */
    @PostMapping("/listForOrder")
    public List<ProductInfo> listForOrder(@RequestBody List<String> productIdList){
        return productService.findList(productIdList);
    }

    @PostMapping("/decreaseStock")
    public void decreaseStock(@RequestBody List<CartDTO> cartDTO){
        productService.decreaseStock(cartDTO);
    }
}
