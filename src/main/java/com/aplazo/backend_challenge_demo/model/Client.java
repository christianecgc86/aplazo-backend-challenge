package com.aplazo.backend_challenge_demo.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birthdate", nullable = false)
    private Date birthdate;

    @Column(name = "assigned_credit_amount", nullable = false)
    private Double assignedCreditAmount;

    @Column(name = "available_credit_amount", nullable = false)
    private Double availableCreditAmount;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Purchase> purchaseList;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Double getAssignedCreditAmount() {
        return assignedCreditAmount;
    }

    public void setAssignedCreditAmount(Double assignedCreditAmount) {
        this.assignedCreditAmount = assignedCreditAmount;
    }

    public Double getAvailableCreditAmount() {
        return availableCreditAmount;
    }

    public void setAvailableCreditAmount(Double availableCreditAmount) {
        this.availableCreditAmount = availableCreditAmount;
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }
}
