package com.projects.airBnbApp.controller;


import com.projects.airBnbApp.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/payment")
    @Operation(summary = "Capture the payments", tags = {"Webhook"})
    // This endpoint is called by Stripe to capture payments after a successful payment session
    // As this method will be on web in production. We use sigHearder to verify the authenticity of the request is coming from stripe.
    public ResponseEntity<Void> capturePayments(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            // using below code it verifies its a vaid api call coming from stripe only
            //Event tells weather its successful payment or not
            // If the event is not valid, it will throw SignatureVerificationException
            // If the event is valid, it will return the Event object which contains the details of the event
            // The endpointSecret is the secret key that you get from Stripe dashboard for your webhook endpoint
            // This is used to verify the authenticity of the request
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            bookingService.capturePayment(event);
            return ResponseEntity.noContent().build();
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }

}
