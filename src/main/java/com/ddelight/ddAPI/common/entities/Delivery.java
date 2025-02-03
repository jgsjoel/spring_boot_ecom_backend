package com.ddelight.ddAPI.common.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double base_cost;
    private Float latitude;
    private Float longitude;
    private Double increment_cost;
    private Integer base_radius;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBase_cost() {
        return base_cost;
    }

    public void setBase_cost(Double base_cost) {
        this.base_cost = base_cost;
    }

    public Double getIncrement_cost() {
        return increment_cost;
    }

    public void setIncrement_cost(Double increment_cost) {
        this.increment_cost = increment_cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getBase_radius() {
        return base_radius;
    }

    public void setBase_radius(Integer base_radius) {
        this.base_radius = base_radius;
    }
}
