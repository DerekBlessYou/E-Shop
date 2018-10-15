package com.derek.order.repository;

import com.derek.order.dataobject.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

// 不需要写方法，直接调用save方法进行插入
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {
}
