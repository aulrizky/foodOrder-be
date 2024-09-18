package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.io.Serializable;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(FavfoodsId.class)
@Table(name = "\"favorite_foods\"")
public class Favfoods implements Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "food_id",referencedColumnName = "food_id")
  private Foods foods;
 
  @Column(name = "is_favorite")
  private Boolean isFavorite;

  @Id
  @ManyToOne
  @JoinColumn(name="user_id",referencedColumnName = "user_id")
  private Users users;

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

}
// food_id
// is_favorite
// user_id
// created_time
// modified_time
// created_by
// modified_by