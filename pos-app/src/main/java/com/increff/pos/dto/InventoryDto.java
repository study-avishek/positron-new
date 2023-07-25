package com.increff.pos.dto;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.flow.InventoryFlow;
import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.increff.pos.dto.helper.InventoryDtoHelper.convert;
import static com.increff.pos.dto.helper.InventoryDtoHelper.normalize;
import static com.increff.pos.util.StringUtil.trimZeros;
import static com.increff.pos.util.TsvUtil.createRow;
import static com.increff.pos.util.TsvUtil.parseHeader;
import static com.increff.pos.util.ValidationUtil.checkValid;


@Component
public class InventoryDto {

    @Autowired
    private InventoryApi api;
    @Autowired
    private InventoryFlow flow;

    public void update(int id, InventoryForm f) throws ApiException {
        normalize(f);
        checkValid(f);
        InventoryPojo p = convert(f);
        api.update(id, p);
    }

    public void tsvUpload(MultipartFile file) throws UploadException, ApiException {
        List<String> barcodes = new ArrayList<>();
        List<List<String>> errors = new ArrayList<>();
        try {
            List<InventoryForm> inventoryForms = tsvToInventoryForm(file, barcodes, errors);
            if(errors.size() > 0) throw new UploadException("Error uploading inventory", errors);
            ValidationUtil.checkValidMultiple(inventoryForms, errors);
            if(errors.size() > 0) throw new UploadException("Error uploading inventory", errors);
            List<InventoryPojo> inventoryPojos = convertToList(inventoryForms, barcodes, errors);
            if(errors.size() > 0) throw new UploadException("Error uploading inventory", errors);

            api.addAll(inventoryPojos);
        }
        catch (IOException e){
            throw new UploadException("Something went wrong! make sure tsv contains less than 5000 lines");
        }
    }

    public List<InventoryData> getAll() throws ApiException {
        List<Object[]> list = api.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (Object[] obj : list) {
            InventoryData inventoryData = new InventoryData();
            inventoryData.setBarcode((String)obj[0]);
            inventoryData.setProd((String)obj[1]);
            inventoryData.setBrand((String)obj[2]);
            inventoryData.setCategory((String)obj[3]);
            inventoryData.setQuantity(String.valueOf(obj[4]));
            inventoryData.setId((int)obj[5]);
            list2.add(inventoryData);
        }
        return list2;
    }

    public InventoryData get(int id) throws ApiException{
        InventoryPojo p = api.getCheck(id);
        int prodId = p.getId();
        return convert(p, flow.getProd(prodId), flow.getBrand(prodId), flow.getCategory(prodId),
                flow.getBarcode(prodId));
    }

    public List<InventoryForm> tsvToInventoryForm(MultipartFile file, List<String> barcodes, List<List<String>> errors)
            throws UploadException, IOException {
        int i = 1;
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        List<InventoryForm> objectsList = new ArrayList<>();
        HashMap<String, Integer> map = parseHeader(br.readLine(), new String[]{"barcode", "quantity"});
        String line;
        while (i <= 5000 && (line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                i++;
                continue;
            }
            String[] columns = line.split("\t");
            if(columns.length != 2){
                errors.add(createRow(i, "Column size: " + columns.length, "Column size must be 2"));
                continue;
            }
            InventoryForm object = createInventoryForm(columns,barcodes,map,errors, i);
            objectsList.add(object);
            i++;
        }

        if(br.readLine() != null) throw new UploadException("Input contains more than 5000 lines");

        return objectsList;
    }

    public InventoryForm createInventoryForm(String[] columns, List<String> barcodes, HashMap<String, Integer> map,
                                             List<List<String>> errors, int i){
        InventoryForm form = new InventoryForm();
        form.setQuantity((trimZeros(columns[map.get("quantity")])));
        barcodes.add(columns[map.get("barcode")].toLowerCase().trim());
        return form;
    }

    public List<InventoryPojo> convertToList(List<InventoryForm> inventoryForms, List<String> barcodes, List<List<String>> errors) throws ApiException {
        return flow.convert(inventoryForms, barcodes, errors);
    }
}
