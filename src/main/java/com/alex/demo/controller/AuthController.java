package com.alex.demo.controller;

import com.alex.demo.entity.Customer;
import com.alex.demo.pojo.dto.UserLoginDto;
import com.alex.demo.response.TResponse;
import com.alex.demo.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("authentication")
public class AuthController {

  @Autowired
  private CustomerService customerService;

  @ApiOperation(value = "用户登录", response = UserLoginDto.class)
  @PostMapping
  public ResponseEntity get(
      @ApiParam(value = "电话", required = true) @RequestParam Long phone,
      @ApiParam(value = "密码", required = true) @RequestParam String password

  ) {
    return TResponse.success(customerService.login(phone, password));
  }
}
