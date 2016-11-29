package com.pikiranrakyat.prevent.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Ads.
 */
@Entity
@Table(name = "master_ads")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "master_ads")
public class Ads extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;


    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Min(value = 0)
    @Column(name = "cols", nullable = false)
    private Integer cols;

    @NotNull
    @Min(value = 0)
    @Column(name = "millimeter", nullable = false)
    private Integer millimeter;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_price", precision = 30, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @NotNull
    private AdsCategory adsCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Ads code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Ads name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCols() {
        return cols;
    }

    public Ads cols(Integer cols) {
        this.cols = cols;
        return this;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public Integer getMillimeter() {
        return millimeter;
    }

    public Ads millimeter(Integer millimeter) {
        this.millimeter = millimeter;
        return this;
    }

    public void setMillimeter(Integer millimeter) {
        this.millimeter = millimeter;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Ads totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public Ads description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AdsCategory getAdsCategory() {
        return adsCategory;
    }

    public Ads adsCategory(AdsCategory adsCategory) {
        this.adsCategory = adsCategory;
        return this;
    }

    public void setAdsCategory(AdsCategory adsCategory) {
        this.adsCategory = adsCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ads ads = (Ads) o;
        if (ads.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ads.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ads{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", cols='" + cols + "'" +
            ", millimeter='" + millimeter + "'" +
            ", totalPrice='" + totalPrice + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
