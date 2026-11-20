package com.estateease.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="properties")
public class Property {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=120)
    private String title;

    @NotBlank
    @Column(length=2500)
    private String description;

    @NotBlank
    @Size(max=100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private PropertyType type = PropertyType.APARTMENT;

    @NotNull
    @DecimalMin("1.00")
    @Digits(integer=12,fraction=2)
    @Column(precision=14,scale=2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Availability availability = Availability.AVAILABLE;

    @NotNull
    @Min(0)
    @Max(30)
    private Integer bedrooms = 1;

    @NotNull
    @Min(0)
    @Max(30)
    private Integer bathrooms = 1;

    @Min(0)
    private Integer areaSqFt = 1000;

    @Size(max=50)
    private String furnishing = "Semi-Furnished";

    @Column(length=500)
    private String amenities = "Parking, 24/7 Security, Power Backup";

    @Column(length=1000)
    private String imageUrl;

    private Boolean featured = false;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="owner_id")
    private AppUser owner;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Property() {}

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getTitle(){ return title; }
    public void setTitle(String v){ title = v; }

    public String getDescription(){ return description; }
    public void setDescription(String v){ description = v; }

    public String getLocation(){ return location; }
    public void setLocation(String v){ location = v; }

    public PropertyType getType(){ return type; }
    public void setType(PropertyType v){ type = v; }

    public BigDecimal getPrice(){ return price; }
    public void setPrice(BigDecimal v){ price = v; }

    public Availability getAvailability(){ return availability; }
    public void setAvailability(Availability v){ availability = v; }

    public Integer getBedrooms(){ return bedrooms; }
    public void setBedrooms(Integer v){ bedrooms = v; }

    public Integer getBathrooms(){ return bathrooms; }
    public void setBathrooms(Integer v){ bathrooms = v; }

    public Integer getAreaSqFt(){ return areaSqFt != null ? areaSqFt : 0; }
    public void setAreaSqFt(Integer v){ areaSqFt = v; }

    public String getFurnishing(){ return furnishing != null ? furnishing : "Unfurnished"; }
    public void setFurnishing(String v){ furnishing = v; }

    public String getAmenities(){ return amenities != null ? amenities : ""; }
    public void setAmenities(String v){ amenities = v; }

    public String getImageUrl(){ return imageUrl; }
    public void setImageUrl(String v){ imageUrl = v; }

    public Boolean getFeatured(){ return featured != null && featured; }
    public void setFeatured(Boolean v){ featured = v; }

    public AppUser getOwner(){ return owner; }
    public void setOwner(AppUser v){ owner = v; }

    public LocalDateTime getCreatedAt(){ return createdAt; }
    public void setCreatedAt(LocalDateTime v){ createdAt = v; }

    public String getImageUrlOrFallback() {
        if (imageUrl != null && !imageUrl.isBlank()) {
            return imageUrl;
        }
        if (type == null) {
            return "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=1200&auto=format&fit=crop&q=80";
        }
        return switch (type) {
            case APARTMENT -> "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=1200&auto=format&fit=crop&q=80";
            case VILLA -> "https://images.unsplash.com/photo-1613977257363-707ba9348227?w=1200&auto=format&fit=crop&q=80";
            case HOUSE -> "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=1200&auto=format&fit=crop&q=80";
            case COMMERCIAL -> "https://images.unsplash.com/photo-1497366216548-37526070297c?w=1200&auto=format&fit=crop&q=80";
            case PLOT -> "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=1200&auto=format&fit=crop&q=80";
        };
    }
}
