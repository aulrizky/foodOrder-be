package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;

import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"users\"")
public class Users implements Serializable {
  
  @Column (name="is_deleted")
  private Boolean isDeleted;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="user_id",unique = true, nullable = false)
  private Integer userId;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_time")
  private Timestamp createdTime;

  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="modified_time")
  private Timestamp modifiedTime;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "fullname")
  private String fullname;

  @Column(name = "modified_by")
  private String modifiedBy;

  @Column(name = "password")
  private String password;

  @Column(name = "username",unique = true, nullable = false)
  private String username;

  @OneToMany(mappedBy = "users")
  private Set<Favfoods>favoriteFood;

  @OneToMany(mappedBy = "users")
  private Set<Carts>Cart;
}

