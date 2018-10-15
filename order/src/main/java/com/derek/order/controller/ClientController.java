package com.derek.order.controller;

import com.derek.order.client.ProductClient;
import com.derek.order.dataobject.ProductInfo;
import com.derek.order.dto.CartDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class ClientController {

    @Autowired
    private ProductClient productClient;

    @GetMapping("/getProductMsg")
    public String getProductMsg(){
        String msg = productClient.productMsg();
        log.info("response={}", msg);
        return msg;
    }

    @GetMapping("/getProductList")
    public String getProductList(){
        List<ProductInfo> list = productClient.listForOrder(Arrays.asList("157875227953464068"));
        log.info("response={}", list);
        return "ok";
    }

    @GetMapping("/productDecreaseStock")
    public String productDecreaseStock(){
        productClient.decreaseStock(Arrays.asList(new CartDTO("157875227953464068", 2)));
        return "ok";
    }
}
