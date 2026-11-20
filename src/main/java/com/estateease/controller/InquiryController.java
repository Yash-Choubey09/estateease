package com.estateease.controller;

import com.estateease.model.*;
import com.estateease.repository.*;
import com.estateease.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InquiryController {

    private final InquiryRepository inquiries;
    private final PropertyRepository properties;
    private final CurrentUserService current;

    public InquiryController(InquiryRepository i, PropertyRepository p, CurrentUserService c) {
        this.inquiries = i;
        this.properties = p;
        this.current = c;
    }

    @PostMapping("/tenant/inquiries/send/{propertyId}")
    public String send(
        @PathVariable Long propertyId,
        @Valid @ModelAttribute("inquiry") Inquiry inquiry,
        BindingResult result,
        @RequestParam(required = false) String phone,
        @RequestParam(required = false) String preferredDate,
        @RequestParam(required = false, defaultValue = "GENERAL") String inquiryType,
        Authentication a,
        RedirectAttributes f
    ) {
        var p = properties.findById(propertyId).orElseThrow();
        if (result.hasErrors() || p.getAvailability() != Availability.AVAILABLE) {
            f.addFlashAttribute("error", "Please enter a message. Note: inquiries are only accepted for available listings.");
            return "redirect:/properties/" + propertyId;
        }
        inquiry.setProperty(p);
        inquiry.setTenant(current.get(a));
        inquiry.setPhone(phone);
        inquiry.setPreferredDate(preferredDate);
        inquiry.setInquiryType(inquiryType);
        inquiry.setStatus("NEW");
        inquiries.save(inquiry);

        String successMsg = "SCHEDULE_TOUR".equalsIgnoreCase(inquiryType)
            ? "Your property tour request has been submitted to the owner!"
            : "Your inquiry has been sent to the property owner!";
        f.addFlashAttribute("success", successMsg);
        return "redirect:/tenant/inquiries";
    }

    @PostMapping("/owner/inquiries/{id}/status")
    public String updateStatus(
        @PathVariable Long id,
        @RequestParam String status,
        Authentication a,
        RedirectAttributes f
    ) {
        Inquiry inq = inquiries.findById(id).orElseThrow();
        if (!inq.getProperty().getOwner().getEmail().equals(a.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
        inq.setStatus(status);
        inquiries.save(inq);
        f.addFlashAttribute("success", "Inquiry status updated to " + status + ".");
        return "redirect:/owner/inquiries";
    }

    @GetMapping("/owner/inquiries")
    public String owner(Authentication a, Model m) {
        m.addAttribute("inquiries", inquiries.findForOwner(current.get(a).getId()));
        return "owner-inquiries";
    }

    @GetMapping("/tenant/dashboard")
    public String tenant(Authentication a, Model m) {
        var user = current.get(a);
        m.addAttribute("user", user);
        m.addAttribute("inquiries", inquiries.findForTenant(user.getId()));
        m.addAttribute("recentProperties", properties.findTop6ByAvailabilityOrderByCreatedAtDesc(Availability.AVAILABLE));
        return "tenant-dashboard";
    }

    @GetMapping("/tenant/inquiries")
    public String tenantInquiries(Authentication a, Model m) {
        m.addAttribute("inquiries", inquiries.findForTenant(current.get(a).getId()));
        return "tenant-inquiries";
    }
}
