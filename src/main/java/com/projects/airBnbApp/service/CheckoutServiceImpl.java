package com.projects.airBnbApp.service;

import com.projects.airBnbApp.entity.Booking;
import com.projects.airBnbApp.entity.User;
import com.projects.airBnbApp.repository.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
// This service handles the checkout process for bookings, creating a Stripe checkout session.
// It uses the Stripe API to create a customer and a session for payment processing.
// The session includes details about the booking, such as the hotel name, room type, and
// the amount to be paid. The session URLs for success and failure are also provided.
public class CheckoutServiceImpl implements  CheckoutService {

    private final BookingRepository bookingRepository;

    @Override
    public String getCheckoutSession(Booking booking, String successUrl, String failureUrl) {
        log.info("Creating session for booking with ID: {}", booking.getId());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();
            Customer customer = Customer.create(customerParams);
            // we are creating the session for the payment
            // we are setting the mode to payment, and we are also setting the billing address collection
            // we are also setting the customer id, success url and failure url
            SessionCreateParams sessionParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    // we are doing below step to ensure that the billing address is collected and is required
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(booking.getHotel().getName() + " : " + booking.getRoom().getType())
                                                                    .setDescription("Booking ID: " + booking.getId())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(sessionParams);

            booking.setPaymentSessionId(session.getId());
            bookingRepository.save(booking);

            //Using this url to redirect the user to the Stripe checkout page
            //This url will be used to redirect the user to the Stripe checkout page
            //After the payment is successful, the user will be redirected to the success url
            //If the payment fails, the user will be redirected to the failure url
            log.info("Session created successfully for booking with ID: {}", booking.getId());
            return session.getUrl();

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
