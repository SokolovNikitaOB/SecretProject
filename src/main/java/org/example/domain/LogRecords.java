package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class LogRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestIpAddress;

    private String userAgent;

    private Date requestTime;

    private Long bannerId;

    private Double bannerPrice;

    private String noContentReason;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "lod_categories",
            joinColumns = @JoinColumn(name = "log_records_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Categories> categories;

}
