/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.invoices.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import java.util.Date;

/**
 * Simple JavaBean domain object representing an invoice.
 */
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private double amount;

    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate = new Date();

    @Size(max = 32)
    @Column(name = "status")
    private String status = "OPEN";

    @Column(name = "visit_id")
    private int visitId;

    public Integer getId() {
        return this.id;
    }

    public double getAmount() {
        return this.amount;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public String getStatus() {
        return this.status;
    }

    public int getVisitId() {
        return this.visitId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public static final class InvoiceBuilder {
        private Integer id;
        private double amount;
        private Date dueDate;
        private @Size(max = 32) String status;
        private int visitId;

        private InvoiceBuilder() {
        }

        public static InvoiceBuilder anInvoice() {
            return new InvoiceBuilder();
        }

        public InvoiceBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public InvoiceBuilder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public InvoiceBuilder dueDate(Date dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public InvoiceBuilder status(String status) {
            this.status = status;
            return this;
        }

        public InvoiceBuilder visitId(int visitId) {
            return this.visitId(visitId);
        }

        public Invoice build() {
            Invoice invoice = new Invoice();
            invoice.setId(id);
            invoice.setAmount(amount);
            invoice.setDueDate(dueDate);
            invoice.setStatus(status);
            invoice.setVisitId(visitId);
            return invoice;
        }
    }
}
