package com.dawidrozewski.sandbox.admin.order.repositor;


import com.dawidrozewski.sandbox.admin.order.model.AdminOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminOrderRepository extends JpaRepository<AdminOrder, Long> {
}
