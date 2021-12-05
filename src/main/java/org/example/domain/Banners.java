package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
public class Banners {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name should not be empty")
    @Size(max=50, message = "Name should contain no more than 50 characters")
    private String name;

    @NotBlank(message = "Text field should not be empty")
    @Size(max=1000, message = "Text field should contain no more than 1000 characters")
    private String textField;

    @NotNull(message = "Price should not be empty")
    @Min(value = 0, message = "Price should not be negative")
    @Max(value = 10000000, message = "Price should not be that big")
    private Double price;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "banner_categories",
                joinColumns = @JoinColumn(name = "banner_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Categories> categories;

}
