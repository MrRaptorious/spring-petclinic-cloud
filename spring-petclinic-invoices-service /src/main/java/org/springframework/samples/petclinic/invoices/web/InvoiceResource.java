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
package org.springframework.samples.petclinic.invoices.web;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.invoices.model.Invoice;
import org.springframework.samples.petclinic.invoices.model.InvoiceRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Timed("petclinic.invoice")
class InvoiceResource {

    private final InvoiceRepository invoiceRepository;

    @GetMapping("invoice")
    public Invoice read(@RequestParam("visitId") @Min(1) int visitId) {
        return invoiceRepository.findByVisitId(visitId);
    }

    // todo potenziell bloed wegen visits? 
    @PostMapping("visits/{visitId}/invoice")
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice create(
        @Valid @RequestBody Invoice invoice,
        @PathVariable("visitId") @Min(1) int visitId) {

        // Die VisitId in der Rechnung setzen
        invoice.setVisitId(visitId);
        log.info("Saving invoice {}", invoice);

        // Rechnung speichern und zurückgeben
        return invoiceRepository.save(invoice);
    }
}
