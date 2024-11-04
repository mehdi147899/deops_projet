package tn.esprit.spring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.services.EmailService;

@RestController
public class EmailController {

    private final EmailService emailService;

    // Constructor injection
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/sendEmail")
    public String sendEmail(@RequestParam String recipient) {
        emailService.sendPipelineAlert("Pipeline Completed", "Your pipeline process has finished successfully.", recipient);
        return "Email sent to " + recipient;
    }
}
