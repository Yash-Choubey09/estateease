package com.estateease.controller;

import com.estateease.model.*;
import com.estateease.repository.*;
import com.estateease.service.CurrentUserService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PropertyController {

    private final PropertyRepository properties;
    private final CurrentUserService current;

    public PropertyController(PropertyRepository p, CurrentUserService c) {
        this.properties = p;
        this.current = c;
    }

    @GetMapping("/properties")
    public String browse(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) PropertyType type,
        @RequestParam(required = false) Availability availability,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(required = false) Integer minBedrooms,
        @RequestParam(required = false, defaultValue = "newest") String sort,
        Model m
    ) {
        String cleanQuery = (query != null && !query.isBlank()) ? query.trim() : null;
        List<Property> list = properties.search(cleanQuery, type, availability, maxPrice, minBedrooms);

        // Sorting
        if ("price_asc".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparing(Property::getPrice));
        } else if ("price_desc".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparing(Property::getPrice).reversed());
        } else {
            list.sort(Comparator.comparing(Property::getCreatedAt).reversed());
        }

        m.addAttribute("properties", list);
        m.addAttribute("types", PropertyType.values());
        m.addAttribute("statuses", Availability.values());
        m.addAttribute("query", query);
        m.addAttribute("type", type);
        m.addAttribute("availability", availability);
        m.addAttribute("maxPrice", maxPrice);
        m.addAttribute("minBedrooms", minBedrooms);
        m.addAttribute("sort", sort);
        return "properties";
    }

    @GetMapping("/properties/{id}")
    public String detail(@PathVariable Long id, Model m) {
        Property property = properties.findById(id).orElseThrow();
        m.addAttribute("property", property);
        // Similar recommended properties
        List<Property> similar = properties.search(null, property.getType(), Availability.AVAILABLE, null, null)
            .stream()
            .filter(p -> !p.getId().equals(id))
            .limit(3)
            .toList();
        m.addAttribute("similarProperties", similar);
        return "property-detail";
    }

    @GetMapping("/owner/dashboard")
    public String owner(Authentication a, Model m) {
        var u = current.get(a);
        List<Property> userProperties = properties.findByOwnerIdOrderByCreatedAtDesc(u.getId());
        long availableCount = userProperties.stream().filter(p -> p.getAvailability() == Availability.AVAILABLE).count();
        long rentedCount = userProperties.stream().filter(p -> p.getAvailability() == Availability.RENTED).count();

        m.addAttribute("user", u);
        m.addAttribute("properties", userProperties);
        m.addAttribute("totalCount", userProperties.size());
        m.addAttribute("availableCount", availableCount);
        m.addAttribute("rentedCount", rentedCount);
        return "owner-dashboard";
    }

    @GetMapping("/owner/properties/new")
    public String newForm(Model m) {
        m.addAttribute("property", new Property());
        addEnums(m);
        return "property-form";
    }

    @GetMapping("/owner/properties/{id}/edit")
    public String edit(@PathVariable Long id, Authentication a, Model m) {
        Property p = owned(id, a);
        m.addAttribute("property", p);
        addEnums(m);
        return "property-form";
    }

    private void addEnums(Model m) {
        m.addAttribute("types", PropertyType.values());
        m.addAttribute("statuses", Availability.values());
    }

    @PostMapping("/owner/properties/save")
    public String save(@Valid @ModelAttribute Property property, BindingResult result, Authentication a, Model m, RedirectAttributes f) {
        if (result.hasErrors()) {
            addEnums(m);
            return "property-form";
        }
        var owner = current.get(a);
        if (property.getId() != null) {
            var old = owned(property.getId(), a);
            old.setTitle(property.getTitle());
            old.setDescription(property.getDescription());
            old.setLocation(property.getLocation());
            old.setType(property.getType());
            old.setPrice(property.getPrice());
            old.setAvailability(property.getAvailability());
            old.setBedrooms(property.getBedrooms());
            old.setBathrooms(property.getBathrooms());
            old.setAreaSqFt(property.getAreaSqFt());
            old.setFurnishing(property.getFurnishing());
            old.setAmenities(property.getAmenities());
            old.setImageUrl(property.getImageUrl());
            old.setFeatured(property.getFeatured());
            properties.save(old);
        } else {
            property.setOwner(owner);
            properties.save(property);
        }
        f.addFlashAttribute("success", "Property listing saved successfully.");
        return "redirect:/owner/dashboard";
    }

    @PostMapping("/owner/properties/{id}/toggle-status")
    public String toggleStatus(@PathVariable Long id, Authentication a, RedirectAttributes f) {
        Property p = owned(id, a);
        if (p.getAvailability() == Availability.AVAILABLE) {
            p.setAvailability(Availability.RENTED);
            f.addFlashAttribute("success", "Property marked as Rented/Occupied.");
        } else {
            p.setAvailability(Availability.AVAILABLE);
            f.addFlashAttribute("success", "Property marked as Available for rent/sale.");
        }
        properties.save(p);
        return "redirect:/owner/dashboard";
    }

    @PostMapping("/owner/properties/{id}/delete")
    public String delete(@PathVariable Long id, Authentication a, RedirectAttributes f) {
        properties.delete(owned(id, a));
        f.addFlashAttribute("success", "Property listing deleted successfully.");
        return "redirect:/owner/dashboard";
    }

    private Property owned(Long id, Authentication a) {
        var p = properties.findById(id).orElseThrow();
        if (!p.getOwner().getEmail().equals(a.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("Not your property");
        }
        return p;
    }
}
