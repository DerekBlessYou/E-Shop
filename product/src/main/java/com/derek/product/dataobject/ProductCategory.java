package com.derek.product.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ProductCategory {

    @Id
    @GeneratedValue//自增
    private Integer categoryId;
    /** 类目名称*/
    private String categoryName;
    /** 类目编号*/
    private Integer categoryType;
    private Date createTime;
    private Date updateTime;

}
