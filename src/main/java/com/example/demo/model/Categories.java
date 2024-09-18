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
import java.io.Serializable;

import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"categories\"")
public class Categories implements Serializable{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="category_id")
  private Integer categoryId;
  
  @Column(name = "category_name")
  private String categoryName;

  @Column (name="is_deleted")
  private Boolean isDeleted;

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

  @OneToMany(mappedBy ="categories")
  private Set<Foods> foods;
}
// category_id
// is_deleted
// created_time
// modified_time
// category_name
// created_by
// modified_by