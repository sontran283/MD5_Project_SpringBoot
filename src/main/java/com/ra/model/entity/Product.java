package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Float price;
    private String image;
    @Column(columnDefinition = "boolean default true")
    private Boolean status = true;
    @ManyToOne
    @JoinColumn(name = "catId", referencedColumnName = "id")
    private Category category;
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<OrderDetail> orderDetail;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Cart_item> cartItem;
}
