package org.springframework.samples.petclinic.invoices.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.invoices.model.Invoice;
import org.springframework.samples.petclinic.invoices.model.InvoiceRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InvoiceResource.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class InvoiceResourceTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  InvoiceRepository invoiceRepository;

  @Test
  void shouldFetchVisits() throws Exception {

    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = formatter.format(date);

    Invoice simpleInvoice = new Invoice();
    simpleInvoice.setId(1);
    simpleInvoice.setAmount(10.00);
    simpleInvoice.setDueDate(date); 
    simpleInvoice.setVisitId(111);

    given(invoiceRepository.findByVisitId(111))
        .willReturn(simpleInvoice);

    mvc.perform(get("/invoice?visitId=111"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.invoiceData.id").value(1))
        .andExpect(jsonPath("$.invoiceData.amount").value(10.00))
        .andExpect(jsonPath("$.invoiceData.dueDate").value(formattedDate))
        .andExpect(jsonPath("$.invoiceData.visitId").value(111))
        .andExpect(jsonPath("$.servedByPod").exists());
  }
}
