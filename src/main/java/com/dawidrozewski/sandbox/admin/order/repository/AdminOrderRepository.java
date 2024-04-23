package com.dawidrozewski.sandbox.admin.order.repository;


import com.dawidrozewski.sandbox.admin.order.model.AdminOrder;
import com.dawidrozewski.sandbox.admin.order.model.AdminOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminOrderRepository extends JpaRepository<AdminOrder, Long> {


    List<AdminOrder> findAllByPlaceDateIsBetweenAndOrderStatus(LocalDateTime from, LocalDateTime to, AdminOrderStatus orderStatus);
}
