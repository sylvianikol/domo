package com.syn.domo.model.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BasePaymentEntity extends BaseEntity  {

    private BigDecimal total;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDateTime paidDate;
    private boolean isPaid;

    public BasePaymentEntity() {
    }

    public BasePaymentEntity(BigDecimal total, LocalDate issueDate, LocalDate dueDate, LocalDateTime paidDate, boolean isPaid) {
        this.total = total;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.paidDate = paidDate;
        this.isPaid = isPaid;
    }

    @ColumnDefault("0")
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Column(name = "issue_date", nullable = false)
    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    @Column(name = "due_date")
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Column(name = "paid_date")
    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    @Column(name = "paid")
    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
