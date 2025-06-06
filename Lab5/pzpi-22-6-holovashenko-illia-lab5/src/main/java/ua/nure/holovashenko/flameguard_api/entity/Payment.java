package ua.nure.holovashenko.flameguard_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;

    @NotNull
    @Size(max = 50)
    @Column(name = "payment_method", length = 50, nullable = false)
    private String paymentMethod;

    @NotNull
    @Size(max = 20)
    @Column(name = "payment_status", length = 20, nullable = false)
    private String paymentStatus;

    @NotNull
    @Column(name = "payment_datetime", nullable = false)
    private LocalDateTime paymentDateTime;

    @OneToOne
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "maintenance_id", unique = true, nullable = false)
    private Maintenance maintenance;

    // Getters and Setters
    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    @PrePersist
    public void validatePaymentFields() {
        if (!paymentMethod.matches("credit card|paypal|cash")) {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
        if (!paymentStatus.matches("pending|completed|failed")) {
            throw new IllegalArgumentException("Invalid payment status: " + paymentStatus);
        }
    }

    public @NotNull LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(@NotNull LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }
}
