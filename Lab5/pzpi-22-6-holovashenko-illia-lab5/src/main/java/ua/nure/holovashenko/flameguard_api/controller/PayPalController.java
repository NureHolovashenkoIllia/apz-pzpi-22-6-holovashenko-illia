package ua.nure.holovashenko.flameguard_api.controller;

import com.paypal.api.payments.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.holovashenko.flameguard_api.dto.PaymentDto;
import ua.nure.holovashenko.flameguard_api.dto.PaymentResponse;
import ua.nure.holovashenko.flameguard_api.service.PayPalService;
import ua.nure.holovashenko.flameguard_api.service.PaymentService;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

/**
 * REST controller for managing PayPal payments.
 * Provides endpoints to create, execute, and cancel PayPal payments.
 */
@RestController
@RequestMapping("/api/payments")
@Tag(name = "PayPal Payments", description = "Endpoints for PayPal payment processing")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;
    @Autowired
    private PaymentService paymentService;

    /**
     * Creates a PayPal payment and returns the approval URL.
     *
     * @param total       the total amount for the payment.
     * @param currency    the currency for the payment.
     * @param description the payment description.
     * @param maintenanceId the associated maintenance ID.
     * @return the approval URL or an error message.
     */
    @Operation(summary = "Create a PayPal payment", description = "Creates a PayPal payment and returns the approval URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment created successfully, approval URL returned."),
            @ApiResponse(responseCode = "400", description = "Invalid input or error in payment creation.")
    })
    @PostMapping("/paypal")
    public ResponseEntity<?> createPayPalPayment(@RequestParam Double total, @RequestParam String currency,
                                                 @RequestParam String description, @RequestParam Integer maintenanceId) {
        try {
            //String cancelUrl = "http://localhost:8080/api/payments/cancel";
            //String successUrl = "http://localhost:8080/api/payments/success?maintenanceId=" + maintenanceId;

            String cancelUrl = "https://flameguard-api-ejfwdhe0dne6b2gw.centralus-01.azurewebsites.net/api/payments/cancel";
            String successUrl = "https://flameguard-api-ejfwdhe0dne6b2gw.centralus-01.azurewebsites.net/api/payments/success?maintenanceId=" + maintenanceId;

            Payment payment = payPalService.createPayment(total, currency, "paypal",
                    "sale", description, cancelUrl, successUrl);

            try {
                String approvalUrl = payPalService.getApprovalUrl(payment);
                return ResponseEntity.ok("{\"approvalUrl\":\"" + approvalUrl + "\"}");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Executes a PayPal payment after user approval.
     *
     * @param paymentId    the PayPal payment ID.
     * @param PayerID      the PayPal payer ID.
     * @param maintenanceId the associated maintenance ID.
     * @return the payment details or an error message.
     */
    @Operation(summary = "Execute a PayPal payment", description = "Executes a PayPal payment after user approval.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment executed successfully."),
            @ApiResponse(responseCode = "400", description = "Error in payment execution.")
    })
    @GetMapping("/success")
    public void executePayPalPayment(@RequestParam String paymentId, @RequestParam String PayerID,
                                                  @RequestParam Integer maintenanceId,
                                                  HttpServletResponse response) throws IOException {
        try {
            Payment payment = payPalService.executePayment(paymentId, PayerID);

            PaymentDto paymentDto = new PaymentDto();
            String paymentMethod = payment.getPayer().getPaymentMethod();
            paymentDto.setPaymentMethod("paypal".equalsIgnoreCase(paymentMethod) ? "paypal" : paymentMethod);
            paymentDto.setPaymentStatus("completed");
            paymentDto.setPaymentDateTime(LocalDateTime.now());
            paymentDto.setMaintenanceId(maintenanceId);

            paymentService.createPayment(paymentDto);

            response.sendRedirect("https://flameguard-frontend-apf3fsg8cbg2ccc2.centralus-01.azurewebsites.net/");
        } catch (Exception e) {
            response.sendRedirect("https://flameguard-frontend-apf3fsg8cbg2ccc2.centralus-01.azurewebsites.net/payment-error?message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));        }
    }

    /**
     * Handles the cancellation of a PayPal payment.
     *
     * @return a cancellation confirmation message.
     */
    @Operation(summary = "Cancel a PayPal payment", description = "Handles the cancellation of a PayPal payment.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment cancellation acknowledged.")
    })
    @GetMapping("/cancel")
    public ResponseEntity<?> cancelPayPalPayment() {
        return ResponseEntity.ok("{\"message\":\"Payment canceled\"}");
    }
}
