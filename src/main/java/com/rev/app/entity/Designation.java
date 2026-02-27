package com.rev.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DESIGNATIONS")
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DESIGNATION_ID")
    private Long designationId;

    @Column(name = "TITLE")
    private String title;

    public Designation() {}

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}