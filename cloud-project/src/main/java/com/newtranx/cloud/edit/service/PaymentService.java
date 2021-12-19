package com.newtranx.cloud.edit.service;

import com.newtranx.cloud.edit.entities.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * @auther Charlie
 * @create 2021-02-01 05:40
 */
public interface PaymentService
{
    public int create(Payment payment);

    public Payment getPaymentById(@Param("id") Long id);
}
