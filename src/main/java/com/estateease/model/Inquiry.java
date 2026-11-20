package com.estateease.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name="inquiries")
public class Inquiry {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=1500)
    @Column(length=1500,nullable=false)
    private String message;

    @Size(max=20)
    private String phone;

    @Size(max=50)
    private String preferredDate;

    @Size(max=30)
    private String inquiryType = "GENERAL";

    @Size(max=30)
    private String status = "NEW";

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="property_id")
    private Property property;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="tenant_id")
    private AppUser tenant;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Inquiry() {}

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getMessage(){ return message; }
    public void setMessage(String v){ message = v; }

    public String getPhone(){ return phone; }
    public void setPhone(String v){ phone = v; }

    public String getPreferredDate(){ return preferredDate; }
    public void setPreferredDate(String v){ preferredDate = v; }

    public String getInquiryType(){ return inquiryType != null ? inquiryType : "GENERAL"; }
    public void setInquiryType(String v){ inquiryType = v; }

    public String getStatus(){ return status != null ? status : "NEW"; }
    public void setStatus(String v){ status = v; }

    public Property getProperty(){ return property; }
    public void setProperty(Property v){ property = v; }

    public AppUser getTenant(){ return tenant; }
    public void setTenant(AppUser v){ tenant = v; }

    public LocalDateTime getCreatedAt(){ return createdAt; }
    public void setCreatedAt(LocalDateTime v){ createdAt = v; }
}
