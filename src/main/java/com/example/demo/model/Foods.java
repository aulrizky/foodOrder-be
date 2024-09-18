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
import java.util.Set;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"foods\"")
public class Foods implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="food_id")
  private Integer foodId;

  @ManyToOne
  @JoinColumn(name="category_id",referencedColumnName = "category_id")
  private Categories categories;

  @Column(name="food_name")
  private String foodName;

  @Column(name="image_filename")
  private String imageFilename;
  
  @Column(name="price")
  private Integer price;

  @Column(name="ingredient")
  private String ingredient;

  @CreatedBy
  @Column(name = "created_by")
  private String createdBy;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="created_time")
  private Timestamp createdTime;

  @LastModifiedDate
  @Column(name="modified_by")
  private String modifiedBy;

  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="modified_time")
  private Timestamp modified_time;

  @OneToMany (mappedBy = "foods")
  private Set<Carts> carts;

  @OneToMany (mappedBy = "foods")
  private Set<Favfoods> favoriteFoods;
}
// food_id
// category_id
// food_name
// image_filename
// price
// ingredient
// created_by
// created_time
// modified_by
// modified_time