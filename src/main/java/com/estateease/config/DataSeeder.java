package com.estateease.config;

import com.estateease.model.*;
import com.estateease.repository.*;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(UserRepository users, PropertyRepository properties, PasswordEncoder encoder) {
        return args -> {
            if (users.count() > 0) return;

            AppUser owner = new AppUser();
            owner.setName("Aarav Mehta");
            owner.setEmail("owner@estateease.demo");
            owner.setPassword(encoder.encode("Owner@123"));
            owner.setRole(Role.OWNER);
            users.save(owner);

            AppUser owner2 = new AppUser();
            owner2.setName("Meera Kapoor");
            owner2.setEmail("meera@estateease.demo");
            owner2.setPassword(encoder.encode("Owner@123"));
            owner2.setRole(Role.OWNER);
            users.save(owner2);

            AppUser tenant = new AppUser();
            tenant.setName("Riya Sharma");
            tenant.setEmail("tenant@estateease.demo");
            tenant.setPassword(encoder.encode("Tenant@123"));
            tenant.setRole(Role.TENANT);
            users.save(tenant);

            AppUser tenant2 = new AppUser();
            tenant2.setName("Kabir Singh");
            tenant2.setEmail("kabir@estateease.demo");
            tenant2.setPassword(encoder.encode("Tenant@123"));
            tenant2.setRole(Role.TENANT);
            users.save(tenant2);

            add(properties, owner, "Sunlit 2BHK in South Delhi",
                "Bright apartment with double balcony, modular kitchen with chimney, Italian marble flooring, and dedicated covered parking. Just 5 minutes walk from metro station, leading hospitals, and premier schools.",
                "Greater Kailash, Delhi", PropertyType.APARTMENT, "32000", Availability.AVAILABLE, 2, 2, 1350, "Fully Furnished",
                "Swimming Pool, Gymnasium, 24/7 Security, High Speed Elevator, Reserved Parking, Modular Kitchen, Power Backup",
                "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=1200&auto=format&fit=crop&q=80", true);

            add(properties, owner, "Modern Luxury Family Home",
                "Independent designer residence nestled in a quiet gated avenue. Features spacious double-height living room, private terrace garden, dedicated home office, and EV charging ready driveway.",
                "Dwarka, Delhi", PropertyType.HOUSE, "18500000", Availability.AVAILABLE, 4, 3, 2900, "Semi-Furnished",
                "Private Lawn, Rooftop Terrace, Covered Garage, EV Charger, Solar Panels, Water Harvesting",
                "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=1200&auto=format&fit=crop&q=80", true);

            add(properties, owner, "Prime High-Street Retail Showroom",
                "Ground-floor commercial property with 45 feet glass frontage right on the main commercial boulevard. Unmatched visibility with continuous foot traffic, suitable for high-end boutique, bank, or cafe.",
                "Sector 18, Noida", PropertyType.COMMERCIAL, "85000", Availability.AVAILABLE, 0, 1, 1600, "Bare Shell",
                "Main Road Facing, Central AC Provisions, Heavy Power Load, Dedicated Customer Parking, Fire Safety System",
                "https://images.unsplash.com/photo-1497366216548-37526070297c?w=1200&auto=format&fit=crop&q=80", false);

            add(properties, owner, "Furnished Minimalist Studio Suite",
                "Well-maintained modern designer studio equipped with ergonomic work desk, high-speed fibre internet, smart TV, and washer-dryer. Ideal for single working professionals or startup founders.",
                "Sector 62, Noida", PropertyType.APARTMENT, "18000", Availability.RENTED, 1, 1, 550, "Fully Furnished",
                "Wi-Fi Included, Housekeeping Available, Clubhouse, Gym, Badminton Court, 24/7 Power",
                "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=1200&auto=format&fit=crop&q=80", false);

            add(properties, owner2, "Grand Garden View Architectural Villa",
                "Ultra-luxury 4BHK triplex villa with private swimming pool, private elevator, landscaped Japanese Zen garden, modular Poggenpohl kitchen, and maid quarters with separate entrance.",
                "Sector 46, Gurugram", PropertyType.VILLA, "42000000", Availability.AVAILABLE, 4, 4, 4800, "Fully Furnished",
                "Private Infinity Pool, Landscaped Garden, Private Lift, Home Automation, 3 Car Garage, Staff Quarters",
                "https://images.unsplash.com/photo-1613977257363-707ba9348227?w=1200&auto=format&fit=crop&q=80", true);

            add(properties, owner2, "Executive 3BHK Golf Course Residence",
                "Panoramic golf-course facing high-rise apartment in a prestigious gated township. Includes 5-star concierge, Olympic size pool, tennis courts, and climate-controlled underground parking.",
                "Golf Course Road, Gurugram", PropertyType.APARTMENT, "56000", Availability.AVAILABLE, 3, 3, 2200, "Semi-Furnished",
                "Golf Course View, Club Privileges, Heated Pool, Tennis & Squash Courts, Valet Parking, Concierge Service",
                "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=1200&auto=format&fit=crop&q=80", true);

            add(properties, owner2, "Corner Residential Villa Plot",
                "Prime corner plot in an upscale gated community with 40-foot wide asphalt roads on both sides, underground electrical wiring, and municipal water connections. Ready for immediate villa construction.",
                "Zirakpur, Punjab", PropertyType.PLOT, "7200000", Availability.AVAILABLE, 0, 0, 2700, "Unfurnished",
                "Gated Township, 24/7 Security Guard, Underground Sewage, LED Street Lights, Boundary Wall Built",
                "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=1200&auto=format&fit=crop&q=80", false);

            add(properties, owner2, "Chic 1BHK in Green Diplomatic Enclave",
                "Peaceful and secure boutique residence surrounded by lush green parks. Features warm hardwood floors, large double-glazed windows, modern bathroom fixtures, and quiet balcony.",
                "Vasant Kunj, Delhi", PropertyType.APARTMENT, "22000", Availability.AVAILABLE, 1, 1, 750, "Fully Furnished",
                "Park Facing, Gated Security, Intercom, Reserved Stilt Parking, Pet Friendly",
                "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=1200&auto=format&fit=crop&q=80", false);
        };
    }

    private void add(PropertyRepository r, AppUser o, String title, String desc, String loc, PropertyType type,
                     String price, Availability av, int beds, int baths, int sqft, String furnishing,
                     String amenities, String imageUrl, boolean featured) {
        Property p = new Property();
        p.setOwner(o);
        p.setTitle(title);
        p.setDescription(desc);
        p.setLocation(loc);
        p.setType(type);
        p.setPrice(new BigDecimal(price));
        p.setAvailability(av);
        p.setBedrooms(beds);
        p.setBathrooms(baths);
        p.setAreaSqFt(sqft);
        p.setFurnishing(furnishing);
        p.setAmenities(amenities);
        p.setImageUrl(imageUrl);
        p.setFeatured(featured);
        r.save(p);
    }
}
