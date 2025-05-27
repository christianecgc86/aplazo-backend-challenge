package com.aplazo.backend_challenge_demo.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long purchaseId;

    @Column(name = "purchase_date", nullable = false)
    private Date purchaseDate;

    @Column(name = "commission_amount", nullable = false)
    private Double commissionAmount;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "installment_amount", nullable = false)
    private Double installmentAmount;

    @Column(name = "due_dates", nullable = false, columnDefinition = "DATE[]")
    private List<Date> dueDates;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public List<Date> getDueDates() {
        return dueDates;
    }

    public void setDueDates(List<Date> dueDates) {
        this.dueDates = dueDates;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
