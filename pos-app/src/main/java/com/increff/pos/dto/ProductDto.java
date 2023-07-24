package com.increff.pos.dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.increff.pos.api.ProductApi;
import com.increff.pos.api.flow.ProductFlow;

import com.increff.pos.exception.UploadException;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.increff.pos.exception.ApiException;
import org.springframework.web.multipart.MultipartFile;

import static com.increff.pos.dto.helper.ProductDtoHelper.*;
import static com.increff.pos.util.TsvUtil.createRow;
import static com.increff.pos.util.TsvUtil.parseHeader;
import static com.increff.pos.util.ValidationUtil.checkValid;
import static com.increff.pos.util.ValidationUtil.checkValidMultiple;

@Component
public class ProductDto {

    @Autowired
    private ProductApi api;

    @Autowired
    private ProductFlow flow;

    public int add(ProductForm form) throws ApiException {
        normalize(form) ;
        checkValid(form);
        int brandCatId = flow.getIdByBrandCategory(form.getBrand(), form.getCategory());
        ProductPojo p = convert(form, brandCatId);
        int id = api.add(p);
        flow.initInventory(id);

        return id;
    }

    public void tsvUpload(MultipartFile file) throws ApiException, UploadException {
        List<List<String>> errors = new ArrayList<>();
        try {
            List<ProductForm> productForms = tsvToProductForm(file, errors);
            if(errors.size() > 0) throw new UploadException("Upload failed", errors);
            checkValidMultiple(productForms, errors);
            if(errors.size() > 0) throw new UploadException("Upload failed", errors);
            normalizeAndCheckDuplicateForms(productForms, errors);
            if(errors.size() > 0) throw new UploadException("Upload failed", errors);
            List<ProductPojo> productPojos = convertToList(productForms, errors);
            if(errors.size() > 0) throw new UploadException("Upload failed", errors);


            api.addAll(productPojos);
            for(ProductPojo p: productPojos) {
                flow.initInventory(p.getId());
            }
        }
        catch (IOException e){
            throw new UploadException("Something went wrong! make sure tsv contains less than 5000 lines");
        }
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo p = api.getCheck(id);
        BrandPojo brandPojo = flow.getBrandById(p.getBrandCatId());
        return convert(p, brandPojo.getBrand(), brandPojo.getCategory());
    }

    public List<ProductData> getAll() throws ApiException {
        List<Object[]> list = api.getAll();
        List<ProductData> list2 = new ArrayList<>();
        for(Object[] obj: list) {
            ProductData data = new ProductData();
            data.setName((String)obj[0]);
            data.setBarcode((String)obj[1]);
            data.setBrand((String)obj[2]);
            data.setCategory((String)obj[3]);
            data.setMrp(String.valueOf(obj[4]));
            data.setId((int)obj[5]);
            list2.add(data);
        }
        return list2;
    }

    public void update(int id, ProductForm form) throws ApiException{
        normalize(form);
        checkValid(form);
        int brandCatId = flow.getIdByBrandCategory(form.getBrand(), form.getCategory());
        ProductPojo p = convert(form, brandCatId);
        api.update(id, p);
    }

    public List<ProductPojo> convertToList(List<ProductForm> productForms, List<List<String>> errors) throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<>();
        int i = 1;
        for(ProductForm productForm: productForms){
            ProductPojo p = new ProductPojo();
            p.setName(productForm.getName());
            p.setBarcode(productForm.getBarcode());
            p.setMrp(Double.parseDouble(productForm.getMrp()));
            try {
                p.setBrandCatId(flow.getIdByBrandCategory(productForm.getBrand(), productForm.getCategory()));
            }
            catch (ApiException e){
                errors.add(createRow(i, productForm.getBrand() + " | " + productForm.getCategory(), "Brand category does not exist"));
            }
            productPojoList.add(p);
            i++;
        }
        return productPojoList;
    }

    public List<ProductForm> tsvToProductForm(MultipartFile file, List<List<String>> error) throws ApiException, UploadException, IOException {
        List<ProductForm> objectsList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        String line;
        int i = 1;
        HashMap<String, Integer> map = parseHeader(br.readLine(), new String[]{"name","brand","category","barcode","mrp"});
        while ((line = br.readLine()) != null && i < 5000) {
            if (line.trim().isEmpty()) {
                i++;
                continue;
            }

            String[] columns = line.split("\t");

            if(columns.length != 5){
                error.add(createRow(i, "Column size: " + columns.length,"Column size must be 5"));
                continue;
            }
            ProductForm object = createProductForm(columns, map, error, i+1);
            objectsList.add(object);
            i++;
        }
        if(br.readLine()!=null) throw new UploadException("Input file contains more than 5000 lines");
        return objectsList;
    }
    public void normalizeAndCheckDuplicateForms(List<ProductForm> forms, List<List<String>> errors) throws UploadException{
        HashSet<String> set = new HashSet<>();
        for(int i = 0 ; i < forms.size();i++){
            ProductForm form = forms.get(i);
            normalize(form);
            try{
                ProductPojo p = api.getCheck(form.getBarcode());
                errors.add(createRow(i+1, "Barcode: "+form.getBarcode(),"Barcode already exists in master"));
            }
            catch (ApiException ignored){
            }
            if(set.contains(form.getBarcode())){
                List<String> temp = new ArrayList<>();
                errors.add(createRow(i+1, "Barcode: "+form.getBarcode(), "Duplicate barcode found in tsv"));
            }
            set.add(form.getBarcode());
        }
    }
}
