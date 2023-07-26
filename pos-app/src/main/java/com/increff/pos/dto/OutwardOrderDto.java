package com.increff.pos.dto;

import com.increff.pos.api.OutwardOrderApi;
import com.increff.pos.api.flow.OutwardOrderFlow;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OutwardOrderData;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.model.form.CustomerForm;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.pojo.OutwardOrderPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class OutwardOrderDto {
    @Autowired
    private OutwardOrderApi api;

    @Autowired
    private OutwardOrderFlow flow;

    @Autowired
    private RestTemplate restTemplate;


    private ResponseEntity<FileSystemResource> responseOut;

    @Value("${pdf.filepath}")
    private String pdfFilePath;

    private String invoiceAppUrl = "http://localhost:9050/invoice/api/invoice";;

    public OutwardOrderData add(){
        OutwardOrderPojo p = new OutwardOrderPojo();
        OutwardOrderPojo temp = api.add(p);
        OutwardOrderData data = new OutwardOrderData();
        data.setId(temp.getId());
        data.setOrderStatus(temp.getOrderStatus());
        return data;
    }

    public void delete(int id) throws ApiException {
        api.delete(id, flow.getOrderItemCount(id));
    }

    public List<OutwardOrderData> getAll() throws ApiException{
        List<Object[]> list1 =  api.getAll();
        List<OutwardOrderData> list2 = new ArrayList<>();

        for(Object[] obj: list1){
            OutwardOrderData data = new OutwardOrderData();
            data.setId((int)obj[0]);
            data.setOrderStatus(OrderStatus.valueOf(obj[1].toString()));
            data.setItemCount((long)obj[2]);
            data.setRevenue((double)obj[3]);
            list2.add(data);
        }

        return list2;
    }

    @Transactional(rollbackOn = ApiException.class)
    public ResponseEntity complete(int id, CustomerForm customerForm) throws ApiException, IOException {
        try {
            ValidationUtil.checkValid(customerForm);
            InvoiceForm form = flow.complete(id, customerForm);

            ResponseEntity<String> response = restTemplate.postForEntity(invoiceAppUrl, form, String.class);
            String encodedString = response.getBody();
            byte[] pdfBytes = Base64.getDecoder().decode(encodedString);

            File pdfFile = new File(pdfFilePath + "/invoice-" + id + ".pdf");
            FileOutputStream out = new FileOutputStream(pdfFile);
            out.write(pdfBytes);
            out.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", pdfFile.getName());

            responseOut = new ResponseEntity<>(
                    new FileSystemResource(pdfFile),
                    headers,
                    HttpStatus.OK
            );
            api.changeStatus(id);
            return responseOut;
        }
        catch (ApiException e){
            throw new ApiException("Invoice generation failed");
        }
    }
}
