package com.alex.demo.entity;

import com.alex.demo.constant.DatabaseEnum.Gender;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

  private static final long serialVersionUID = 2427404465489542923L;

  private String id;

  private Long phone;

  private String nickname;

  private String avatar;

  private Gender gender;

  private String wechat;

  private String weibo;

  private Byte vipLevel;

  private LocalDateTime updatedOn;

  private LocalDateTime createdOn;

  private String password;

  public Customer(String id, Long phone, LocalDateTime createdOn, Gender gender, String password) {
    this.id = id;
    this.phone = phone;
    this.createdOn = createdOn;
    this.gender = gender;
    this.password = password;
  }

  public Customer(String id, String nickname) {
    this.id = id;
    this.nickname = nickname;
  }
}