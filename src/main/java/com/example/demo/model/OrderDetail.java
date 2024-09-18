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
@Table(name = "\"order_detail\"")
public class OrderDetail implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_detail_id")
  private Integer orderDetailId;

  @ManyToOne
  @JoinColumn(name = "food_id")
  private Foods food;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Orders order;

  @Column(name = "qty")
  private Integer qty;

  @Column(name = "total_price")
  private Integer totalPrice;

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

  @ManyToOne
  @JoinColumn(name = "cart_id")
  private Carts Carts;
}
// order_detail_id
// food_id
// order_id
// qty
// total_price
// created_by
// created_time
// modified_by
// modified_time
// cart_id