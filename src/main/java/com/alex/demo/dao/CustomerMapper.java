package com.alex.demo.dao;

import com.alex.demo.entity.Customer;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomerMapper {

  int deleteByPrimaryKey(String id);

  int insert(Customer record);

  int insertSelective(Customer record);

  Customer selectByPrimaryKey(String id);

  int updateByPrimaryKeySelective(Customer record);

  int updateByPrimaryKey(Customer record);

  List<Customer> getCustomers();

  Customer checkExist(@Param("phone") Long phone, @Param("password") String password);
}