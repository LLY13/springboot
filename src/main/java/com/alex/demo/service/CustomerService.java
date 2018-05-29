package com.alex.demo.service;

import com.alex.demo.constant.DatabaseEnum.Gender;
import com.alex.demo.dao.CustomerMapper;
import com.alex.demo.entity.Customer;
import com.alex.demo.exception.Exception;
import com.alex.demo.pojo.dto.UserLoginDto;
import com.alex.demo.security.jwt.JwtUser;
import com.alex.demo.security.jwt.JwtUtil;
import com.alex.demo.util.CommonUtil;
import com.alex.demo.util.redis.RedisUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

  @Autowired
  private CustomerMapper customerMapper;
  @Autowired
  private RedisUtil redis;

  public UserLoginDto login(Long phone, String password) {
    Customer customer = customerMapper.checkExist(phone, password);
    if (customer != null) {
      List<String> rights = new ArrayList<>();
//      rights.add("1");
      rights.add("2");
      JwtUser user = new JwtUser(customer.getId(), customer.getPhone().toString(), "APP", rights,
          new Date(System.currentTimeMillis() + 10 * 24 * 3600 * 1000L));
      String token = JwtUtil.create(user);
      UserLoginDto dto = new UserLoginDto(customer, token);
      return dto;
    }else {
      throw Exception.badRequest("账号密码错误");
    }
  }

  @Cacheable(value = "customer", key = "#id")
  public Customer getCustomer(String id) {
    return customerMapper.selectByPrimaryKey(id);
  }


  public Page<Customer> getCustomers(Integer page, Integer size) {
    Page<Customer> result = PageHelper.startPage(page, size)
        .doSelectPage(() -> customerMapper.getCustomers());
    return result;
  }


  @Transactional
  public Customer setCustomer(Long phone, LocalDateTime time, Gender gender, String password) {
    Customer customer = new Customer(CommonUtil.uuid(), phone, time, gender, password);
    customerMapper.insert(customer);
    return (customer);
  }


  @CachePut(value = "customer", key = "#id")
  @Transactional
  public Customer updateCustomer(String id, String nickname) {
    Customer customer = new Customer(id, nickname);
    customerMapper.updateByPrimaryKeySelective(customer);
    customer = customerMapper.selectByPrimaryKey(id);
    return (customer);
  }


  @CacheEvict(value = "customer", key = "#id")
  @Transactional
  public String deleteCustomer(String id) {
    customerMapper.deleteByPrimaryKey(id);
    return "success";
  }
}
