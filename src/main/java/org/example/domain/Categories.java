package org.example.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Request ID should not be empty")
    @Size(max=50, message = "Request ID should contain no more than 50 characters")
    private String requestId;

    @NotBlank(message = "Name should not be empty")
    @Size(max=50, message = "Name should contain no more than 50 characters")
    private String name;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;

    @ManyToMany(mappedBy = "categories")
    private List<Banners> banners;

    @ManyToMany(mappedBy = "categories")
    private List<LogRecords> logRecords;

}
