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

import java.sql.Date;
import java.sql.Timestamp;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"orders\"")
public class Orders implements Serializable {
  // order_id
  // user_id
  // order_date
  // total_item
  // total_order_price
  // created_by
  // created_time
  // modified_by
  // modified_time
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Integer orderId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private Users user;

  // @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "order_date")
  @Temporal(TemporalType.DATE)
  private Date orderDate;

  @Column(name = "total_item")
  private Integer totalItem;

  @Column(name = "total_order_price")
  private Integer totalOrderPrice;

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
