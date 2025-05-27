package com.aplazo.backend_challenge_demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_schemes")
public class PaymentScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_scheme_id")
    private Long paymentSchemeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "number_of_payments", nullable = false)
    private Integer numberOfPayments;

    @Column(name = "frequency", nullable = false)
    private Integer frequency;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    public Long getPaymentSchemeId() {
        return paymentSchemeId;
    }

    public void setPaymentSchemeId(Long paymentSchemeId) {
        this.paymentSchemeId = paymentSchemeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfPayments() {
        return numberOfPayments;
    }

    public void setNumberOfPayments(Integer numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
}
