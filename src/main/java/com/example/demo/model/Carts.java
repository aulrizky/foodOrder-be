package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"carts\"")
public class Carts implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_id")
  private Integer cartId;

  @ManyToOne
  @JoinColumn(name = "food_id", referencedColumnName = "food_id")
  private Foods foods;

  @Column(name = "is_deleted")
  private boolean isDeleted;

  @Column(name = "qty")
  private Integer qty;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private Users users;

  @CreatedBy
  @Column(name = "created_by")
  private String createdBy;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_time")
  private Timestamp createdTime;

  @LastModifiedDate
  @Column(name = "modified_by")
  private String modifiedBy;

  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modified_time")
  private Timestamp modified_time;

}
// cart_id
// food_id
// is_deleted
// qty
// user_id
// created_time
// modified_time
// created_by
// modified_by