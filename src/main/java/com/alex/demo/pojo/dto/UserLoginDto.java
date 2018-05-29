package com.alex.demo.pojo.dto;

import com.alex.demo.entity.Customer;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户登录模型")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

  private Customer userInfo;

  private String token;

}
