package com.increff.pos.service.dtoTest;


import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.service.AbstractUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.service.util.ConstructorUtil.createBrandForm;
import static com.increff.pos.service.util.ConstructorUtil.createProductForm;
import static junit.framework.TestCase.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductDto productDto;


    @Test
    public void testUpdateValid() throws ApiException {
        brandDto.add(createBrandForm("test brand", "test category"));
        int prodId = productDto.add(createProductForm("test product",
                "test brand",
                "test category",
                "testbarcode",
                "1000.00"));
        List<InventoryData> data = inventoryDto.getAll();
        assertEquals(prodId, data.get(0).getId());
        assertEquals("testbarcode", data.get(0).getBarcode());
        assertEquals("test product", data.get(0).getProd());
        assertEquals("test brand", data.get(0).getBrand());
        assertEquals("test category", data.get(0).getCategory());

        assertEquals("0", data.get(0).getQuantity());

        InventoryForm form = new InventoryForm();
        form.setQuantity("1000");
        inventoryDto.update(prodId, form);
        data = inventoryDto.getAll();
        assertEquals("1000", data.get(0).getQuantity());
    }

    @Test
    public void testGetValid() throws ApiException {
        brandDto.add(createBrandForm("test brand", "test category"));
        int prodId = productDto.add(createProductForm("test product",
                "test brand",
                "test category",
                "testbarcode",
                "1000.00"));
        InventoryForm form = new InventoryForm();
        form.setQuantity("1000");
        inventoryDto.update(prodId, form);
        InventoryData data = inventoryDto.get(prodId);
        assertEquals("1000", data.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalidQuantity() throws ApiException{
        brandDto.add(createBrandForm("test brand", "test category"));
        int prodId = productDto.add(createProductForm("test product",
                "test brand",
                "test category",
                "testbarcode",
                "1000.00"));

        InventoryForm form = new InventoryForm();
        form.setQuantity("-1000");
        inventoryDto.update(prodId, form);
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalidId() throws ApiException{
        brandDto.add(createBrandForm("test brand", "test category"));
        int prodId = productDto.add(createProductForm("test product",
                "test brand",
                "test category",
                "testbarcode",
                "1000.00"));

        InventoryForm form = new InventoryForm();
        form.setQuantity("1000");
        inventoryDto.update(-1, form);
    }

    @Test
    public void testTsvUploadValid() throws ApiException, IOException, UploadException {
        brandDto.add(createBrandForm("test brand", "test category"));
        for(int i = 0 ; i < 5 ; i++){
            productDto.add(createProductForm("test product",
                    "test brand",
                    "test category",
                    "testbarcode"+i,
                    "1000.00"));
        }
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_inventory_valid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        inventoryDto.tsvUpload(multipartFile);
        List<InventoryData> dataList = inventoryDto.getAll();
        int sumQuantity = 0;
        for(InventoryData data: dataList){
            sumQuantity += Integer.parseInt(data.getQuantity());
        }
        assertEquals(1500, sumQuantity);
    }

    @Test(expected = UploadException.class)
    public  void testTsvUploadInvalid() throws IOException, UploadException, ApiException {
        brandDto.add(createBrandForm("test brand", "test category"));
        for(int i = 0 ; i < 5 ; i++){
            productDto.add(createProductForm("test product",
                    "test brand",
                    "test category",
                    "testbarcode"+i,
                    "1000.00"));
        }
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_inventory_invalid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        inventoryDto.tsvUpload(multipartFile);
    }

    @Test(expected = UploadException.class)
    public  void testTsvUploadInvalidFile() throws IOException, UploadException, ApiException {
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_invalid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        inventoryDto.tsvUpload(multipartFile);
    }

    @Test(expected = UploadException.class)
    public  void testTsvUploadInvalidFormat() throws IOException, UploadException, ApiException {
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_invalid_format"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        inventoryDto.tsvUpload(multipartFile);
    }


}
