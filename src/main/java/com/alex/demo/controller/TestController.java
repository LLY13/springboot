package com.alex.demo.controller;

import com.alex.demo.config.CustomConfig;
import com.alex.demo.constant.DatabaseEnum.Gender;
import com.alex.demo.entity.Customer;
import com.alex.demo.service.CustomerService;
import com.github.pagehelper.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class TestController {

  @Autowired
  private CustomConfig config;

  @Autowired
  private CustomerService customerService;

  @ApiOperation(value = "获取信息", response = Customer.class)
  @GetMapping("{id}")
  public Customer get(
      @ApiParam(value = "id", required = true) @PathVariable String id
  ) {
    return customerService.getCustomer(id);
  }


  @ApiOperation(value = "获取所有信息", response = Page.class)
  @GetMapping
  public Page<Customer> getAll(
      @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer page,
      @ApiParam(value = "页大小") @RequestParam(defaultValue = "15") Integer size
  ) {
    return customerService.getCustomers(page, size);
  }


  @ApiOperation(value = "创建信息", response = Customer.class)
  @PostMapping
  public Customer create(
      @ApiParam(value = "电话", required = true) @RequestParam Long phone,
      @ApiParam(value = "性别", required = true) @RequestParam Gender gender,
      @ApiParam(value = "密码", required = true) @RequestParam String password
  ) {
    return customerService.setCustomer(phone, gender, password);
  }

  @ApiOperation(value = "更新信息", response = Customer.class)
  @PutMapping("{id}")
  public Customer update(
      @ApiParam(value = "id", required = true) @PathVariable String id,
      @ApiParam(value = "nickname", required = true) @RequestParam String nickname
  ) {
    return customerService.updateCustomer(id, nickname);
  }

  @ApiOperation(value = "删除信息")
  @DeleteMapping("{id}")
  public String delete(
      @ApiParam(value = "id", required = true) @PathVariable String id
  ) {
    return customerService.deleteCustomer(id);
  }


}
