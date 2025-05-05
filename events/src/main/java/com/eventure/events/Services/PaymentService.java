package com.eventure.events.Services;

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

    public String createPayment(String amount) throws IOException {
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
                .brandName("MyShop")
                .landingPage("BILLING")
                .shippingPreference("NO_SHIPPING")
                .userAction("PAY_NOW");

        orderRequest.applicationContext(applicationContext);

        // Create an order
        OrdersCreateRequest request = new OrdersCreateRequest()
                .requestBody(orderRequest);

        try {
            Order order = payPalHttpClient.execute(request).result();

            // Extract approval URL correctly
            for (LinkDescription link : order.links()) {
                if ("approve".equalsIgnoreCase(link.rel())) {
                    return link.href();
                }
            }
            throw new RuntimeException("Approval URL not found");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create PayPal payment", e);
        }
    }
}

