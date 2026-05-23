package com.antiphishing.controller;

import com.antiphishing.model.Email;
import com.antiphishing.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/api/emails/analyze")
    @ResponseBody
    public ResponseEntity<Email> analyzeEmail(@RequestBody Email email) {
        Email analyzedEmail = emailService.analyzeEmail(email);
        return ResponseEntity.ok(analyzedEmail);
    }

    @GetMapping("/api/emails")
    @ResponseBody
    public ResponseEntity<List<Email>> getAllEmails() {
        return ResponseEntity.ok(emailService.getAllEmails());
    }

    @GetMapping("/api/emails/phishing")
    @ResponseBody
    public ResponseEntity<List<Email>> getPhishingEmails() {
        return ResponseEntity.ok(emailService.getPhishingEmails());
    }

    @GetMapping("/api/emails/legitimate")
    @ResponseBody
    public ResponseEntity<List<Email>> getLegitimateEmails() {
        return ResponseEntity.ok(emailService.getLegitimateEmails());
    }

    @GetMapping("/api/emails/{id}")
    @ResponseBody
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        Email email = emailService.getEmailById(id);
        return email != null ? ResponseEntity.ok(email) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/emails/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailService.deleteEmail(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/emails/statistics")
    @ResponseBody
    public ResponseEntity<EmailService.Statistics> getStatistics() {
        return ResponseEntity.ok(emailService.getStatistics());
    }
}