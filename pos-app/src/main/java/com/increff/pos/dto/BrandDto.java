package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
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
import java.util.Objects;

import static com.increff.pos.dto.helper.BrandDtoHelper.*;
import static com.increff.pos.util.TsvUtil.createRow;
import static com.increff.pos.util.TsvUtil.parseHeader;

@Component
public class BrandDto {

    @Autowired
    private BrandApi api;

    public int add(BrandForm form) throws ApiException {
        normalize(form);
        ValidationUtil.checkValid(form);
        BrandPojo p = convert(form);
        return api.add(p);
    }

    public void tsvUpload(MultipartFile file) throws ApiException, UploadException {
        List<List<String>> error = new ArrayList<>();
        List<BrandForm> brandForms = null;
        try {
            brandForms = tsvToBrandForm(file, error);
            if(error.size() > 0) throw new UploadException("Upload failed", error);
            ValidationUtil.checkValidMultiple(brandForms, error);
            if(error.size() > 0) throw new UploadException("Upload failed", error);
            normalizeAndCheckDuplicateForms(brandForms, error);
            if(error.size() > 0) throw new UploadException("Upload failed", error);
            List<BrandPojo> brandPojos = convert(brandForms);
            if(error.size() > 0) throw new UploadException("Upload failed", error);
            api.addAll(brandPojos);
        } catch (IOException e) {
            throw new UploadException("Something went wrong! make sure tsv contains less than 5000 lines");
        }
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo p = api.get(id);
        return convert(p);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> list = api.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void update(int id,BrandForm form) throws ApiException {
        normalize(form);
        ValidationUtil.checkValid(form);
        BrandPojo p = convert(form);
        api.update(id, p);
    }

    public List<BrandForm> tsvToBrandForm(MultipartFile file, List<List<String>>error) throws UploadException, IOException, ApiException {
        List<BrandForm> objectsList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        int i = 1;
        String line = br.readLine();
        HashMap<String, Integer> map = parseHeader(line, new String[]{"brand", "category"});
        while (i <= 5000 && (line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                i++;
                continue;
            }
            String[] columns = line.split("\t");
            if (columns.length != 2) {
                error.add(createRow(i, "Column size: " + columns.length, "Columns size must be 2"));
            } else {
                objectsList.add(createBrandForm(columns, map));
            }
            i++;
        }
        if(br.readLine() != null) throw new UploadException("Input file contains more than 5000 lines");
        return objectsList;
    }

    public void normalizeAndCheckDuplicateForms(List<BrandForm> forms, List<List<String>> error) throws UploadException{
        HashMap<String, String> map = new HashMap<>();
        for(int i = 0 ; i < forms.size();i++){
            BrandForm form = forms.get(i);
            normalize(form);
            try{
                BrandPojo p = api.getCheck(form.getBrand(), form.getCategory());
                error.add(createRow(i+1, "Brand: "+form.getBrand()+" | Category: " + form.getCategory(),"Brand category already exists in master"));
            }
            catch (ApiException e){
            }
            if(Objects.equals(map.get(form.getBrand()), form.getCategory())){
                error.add(createRow(i+1, "Brand: "+form.getBrand()+" | Category: " + form.getCategory(),"Brand category already exists in tsv"));
            }
            map.put(form.getBrand(), form.getCategory());
        }
    }
}
