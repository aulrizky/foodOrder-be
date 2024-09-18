package com.example.demo.model;
import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FavfoodsId implements Serializable {
  private Foods foods;
  private Users users;
}
