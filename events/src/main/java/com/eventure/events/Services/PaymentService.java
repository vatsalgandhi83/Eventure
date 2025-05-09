package com.eventure.events.Services;

import com.eventure.events.dto.PaymentResponse;
import com.eventure.events.exception.PaymentException;
import com.paypal.orders.*;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PayPalHttpClient payPalHttpClient;

    public PaymentResponse createPayment(String amount) {
        try {
            // Validate amount
            validateAmount(amount);

            // Create PayPal order
            String approvalUrl = createPayPalOrder(amount);
            
            return new PaymentResponse(
                "success",
                "Payment created successfully",
                approvalUrl
            );
        } catch (PaymentException e) {
            return new PaymentResponse("error", e.getMessage(), e.getErrorCode(), null);
        } catch (Exception e) {
            return new PaymentResponse(
                "error",
                "Failed to create payment: " + e.getMessage(),
                "PAYMENT_CREATION_FAILED",
                null
            );
        }
    }

    private void validateAmount(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            throw new PaymentException("Amount is required", "INVALID_AMOUNT");
        }

        try {
            double amountValue = Double.parseDouble(amount);
            if (amountValue <= 0) {
                throw new PaymentException("Amount must be greater than 0", "INVALID_AMOUNT");
            }
        } catch (NumberFormatException e) {
            throw new PaymentException("Invalid amount format", "INVALID_AMOUNT_FORMAT");
        }
    }

    private String createPayPalOrder(String amount) throws IOException {
        // Create an order request
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        // Set up transaction details
        AmountWithBreakdown amountWithBreakdown = new AmountWithBreakdown()
                .currencyCode("USD")
                .value(amount);

        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .amountWithBreakdown(amountWithBreakdown)
                .description("Payment for Order");

        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));

        // Set up redirect URLs
        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl("http://localhost:8080/paypal/success")
                .cancelUrl("http://localhost:8080/paypal/cancel")
                .brandName("Eventure")
                .landingPage("BILLING")
                .shippingPreference("NO_SHIPPING")
                .userAction("PAY_NOW");

        orderRequest.applicationContext(applicationContext);

        // Create an order
        OrdersCreateRequest request = new OrdersCreateRequest()
                .requestBody(orderRequest);

        try {
            Order order = payPalHttpClient.execute(request).result();

            // Extract approval URL
            for (LinkDescription link : order.links()) {
                if ("approve".equalsIgnoreCase(link.rel())) {
                    return link.href();
                }
            }
            throw new PaymentException("Approval URL not found in PayPal response", "PAYMENT_CREATION_FAILED");
        } catch (IOException e) {
            throw new PaymentException("Failed to create PayPal payment: " + e.getMessage(), "PAYMENT_CREATION_FAILED");
        }
    }
}

