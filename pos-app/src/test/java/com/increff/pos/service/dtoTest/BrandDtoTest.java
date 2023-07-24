package com.increff.pos.service.dtoTest;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.exception.ApiException;

import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.service.AbstractUnitTest;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.increff.pos.service.util.ConstructorUtil.createBrandForm;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;


public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto dto;

    @Test
    public void testAddValid() throws ApiException {
        BrandForm validForm = createBrandForm("  Test Brand Name!@#$%^&*()_+= ","  Test Category Name  ");
        int id = dto.add(validForm);
        BrandData data = dto.get(id);
        assertEquals(id, data.getId());
        assertEquals("test brand name!@#$%^&*()_+=", data.getBrand());
        assertEquals("test category name", data.getCategory());
    }

    @Test(expected= ApiException.class)
    public void testAddEmpty() throws ApiException {
        BrandForm nullForm = new BrandForm();
        dto.add(nullForm);
    }

    @Test(expected = ApiException.class)
    public void testAddLong() throws ApiException{
        BrandForm longForm = createBrandForm("This is a very long brand name which exceeds 30 characters",
                "This is a very long category name which exceeds 30 characters");
        dto.add(longForm);
    }

    @Test(expected = ApiException.class)
    public void testAddInvalidCharacters() throws ApiException {
        BrandForm invalidCharacterForm = createBrandForm("\uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06",
                "\uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06");
        dto.add(invalidCharacterForm);
    }

    @Test(expected = ApiException.class)
    public void testAddDuplicate() throws ApiException{
        BrandForm validForm = createBrandForm("test brand", "test category");
        dto.add(validForm);
        dto.add(validForm);
    }

    @Test
    public void testGetValid() throws ApiException {
        BrandForm form = createBrandForm("test brand", "test category");
        int id = dto.add(form);
        try{
            BrandData data = dto.get(id);
            assertEquals("test brand", data.getBrand());
            assertEquals("test category", data.getCategory());

        }
        catch (Exception e){
            fail();
        }
    }

    @Test(expected = ApiException.class)
    public void testGetInvalid() throws ApiException{
        BrandForm form = createBrandForm("test brand", "test category");

        dto.add(form);
        BrandData data = dto.get(-1);
    }

    @Test
    public void testGetAll() throws ApiException {
        BrandForm form = new BrandForm();
        for(int i = 0; i < 10; i++){
            form.setBrand("Test Brand Name " + i);
            form.setCategory("Test Category Name " + i);
            dto.add(form);
        }

        List<BrandData> dataList = dto.getAll();
        assertEquals(10, dataList.size());
        for(int i = 0; i < 10 ; i++){
            assertEquals("test brand name "+i, dataList.get(i).getBrand());
            assertEquals("test category name "+i, dataList.get(i).getCategory());
        }
    }

    @Test
    public void testUpdateValid() throws ApiException{
        BrandForm form = new BrandForm();

        form.setBrand("test brand name 1");
        form.setCategory("test category name 1");
        int id = dto.add(form);

        form.setBrand("test brand name 2");
        form.setCategory("test category name 2");
        dto.update(id, form);

        BrandData data = dto.get(id);
        assertEquals("test brand name 2", data.getBrand());
        assertEquals("test category name 2", data.getCategory());
    }

    @Test(expected= ApiException.class)
    public void testUpdateDuplicate() throws ApiException{
        BrandForm form1 = new BrandForm();
        BrandForm form2 = new BrandForm();


        form1.setBrand("test brand name 1");
        form1.setCategory("test category name 1");
        dto.add(form1);

        form2.setBrand("test brand name 2");
        form2.setCategory("test category name 2");
        int id = dto.add(form2);

        form2.setBrand("test brand name 1");
        form2.setCategory("test category name 1");

        dto.update(id, form2);
    }

    @Test
    public void testTsvUploadValid() throws IOException, UploadException, ApiException {
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_brand_valid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        dto.tsvUpload(multipartFile);
        List<BrandData> brandDataList = dto.getAll();
        assertEquals(3, brandDataList.size());
    }

    @Test(expected = UploadException.class)
    public void testTsvUploadDuplicate() throws IOException, UploadException, ApiException {
        BrandForm form = createBrandForm("test brand", "test category");
        dto.add(form);

        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_brand_invalid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        dto.tsvUpload(multipartFile);
    }

    @Test(expected = UploadException.class)
    public void testTsvUploadInvalid() throws IOException, UploadException, ApiException {
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_invalid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        dto.tsvUpload(multipartFile);
    }

    @Test(expected = UploadException.class)
    public void testTsvUploadInvalidFormat() throws IOException, UploadException, ApiException {
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_invalid_format"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        dto.tsvUpload(multipartFile);
    }




}
