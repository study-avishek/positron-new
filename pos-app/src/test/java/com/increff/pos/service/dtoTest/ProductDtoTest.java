package com.increff.pos.service.dtoTest;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.service.AbstractUnitTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.increff.pos.service.util.ConstructorUtil.createBrandForm;
import static com.increff.pos.service.util.ConstructorUtil.createProductForm;
import static junit.framework.TestCase.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    private ProductDto productDto;

    @Autowired
    private BrandDto brandDto;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddUpdateValid() throws ApiException {
        brandDto.add(createBrandForm("test brand", "test category"));
        int id = productDto.add(createProductForm("  Test prOducT!@#$%^&*()_+=  ",
                "test brand",
                "test category",
                "testbarcode1",
                "1000.00"));

        ProductData data = productDto.get(id);
        assertEquals("test product!@#$%^&*()_+=", data.getName());
        assertEquals("testbarcode1", data.getBarcode());
        assertEquals("test brand", data.getBrand());
        assertEquals("test category", data.getCategory());
        assertEquals("1000.0", data.getMrp());
    }

    @Test(expected = ApiException.class)
    public void testAddDuplicateBarcode() throws ApiException{
        brandDto.add(createBrandForm("test brand", "test category"));
        productDto.add(createProductForm("test product",
                "test brand",
                "test category",
                "testbarcode",
                "1000.00"));
        productDto.add(createProductForm("test product 1",
                "test brand",
                "test category",
                "testbarcode",
                "1200.00"));

    }

    @Test(expected = ApiException.class)
    public void testAddInvalidBrandCategory() throws ApiException {
        ProductForm validProductForm = new ProductForm();
        productDto.add(createProductForm("  Test prOducT!@#$%^&*()_+=  ",
                        "test brand",
                        "test category",
                        "testbarcode1",
                        "1000.00"
        ));
    }

    @Test(expected = ApiException.class)
    public void testAddInvalidBarcode() throws ApiException{
        brandDto.add(createBrandForm("test brand", "test category"));

        productDto.add(createProductForm("test product",
                "test brand",
                "test category",
                "longBarCodeWithInvalidCharacters!@#$",
                "1000.00"));
    }

    @Test(expected = ApiException.class)
    public void testAddInvalidMrp() throws ApiException{
        brandDto.add(createBrandForm("test brand", "test category"));

        productDto.add(createProductForm("test product",
                "test brand",
                "test category",
                "testbarcode",
                "-1111.1111"));
    }

    @Test(expected = ApiException.class)
    public void testAddInvalidEmptyProductName() throws ApiException{
        brandDto.add(createBrandForm("test brand", "test category"));

        productDto.add(createProductForm("",
                "test brand",
                "test category",
                "test barcode",
                "1000.00"));
    }

    @Test(expected = ApiException.class)
    public void testGetInvalid() throws ApiException{
        brandDto.get(-1);
    }

    @Test
    public void testGetAll() throws ApiException{
        assertEquals(0, productDto.getAll().size());
        for(int i = 0; i < 10 ; i++){
            brandDto.add(createBrandForm("test brand "+i, "test category "+i));

            productDto.add(createProductForm("test product "+i,
                    "test brand "+i,
                    "test category "+i,
                    "testbarcode"+i,
                    ""+(1000.00 + i*10)));
        }

        List<ProductData> data = productDto.getAll();
        assertEquals(10, data.size());
        for(int i = 0 ; i < 10 ; i++){
            assertEquals("test product "+i, data.get(i).getName());
            assertEquals("testbarcode"+i, data.get(i).getBarcode());
            assertEquals("test brand "+i, data.get(i).getBrand());
            assertEquals("test category "+i, data.get(i).getCategory());
            assertEquals(""+(1000.00 + i*10), data.get(i).getMrp());

        }
    }


    @Test
    public void testUpdateValid() throws ApiException{
        brandDto.add(createBrandForm("test brand", "test category"));
        brandDto.add(createBrandForm("test brand 1", "test category 1"));
        int id = productDto.add(createProductForm("test product", "test brand", "test category","testbarcode", "1000.00"));
        productDto.update(id, createProductForm("test product 1", "test brand 1", "test category 1","testbarcode1", "1200.00"));
        ProductData data = productDto.get(id);
        assertEquals("test product 1", data.getName());
        assertEquals("test brand 1", data.getBrand());
        assertEquals("test category 1", data.getCategory());
        assertEquals("testbarcode1", data.getBarcode());
        assertEquals("1200.0", data.getMrp());
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalidId() throws ApiException{
        brandDto.add(createBrandForm("test brand", "test category"));
        brandDto.add(createBrandForm("test brand 1", "test category 1"));
        int id = productDto.add(createProductForm("test product", "test brand", "test category","testbarcode", "1000.00"));
        productDto.update(id+1, createProductForm("test product 1", "test brand 1", "test category 1","testbarcode1", "1200.00"));
    }

    @Test(expected = ApiException.class)
    public void testUpdateDuplicateBarcode() throws ApiException{
        brandDto.add(createBrandForm("test brand", "test category"));
        int id = productDto.add(createProductForm("test product", "test brand", "test category","testbarcode", "1000.00"));
        productDto.add(createProductForm("test product 1", "test brand", "test category","testbarcode1", "1200.00"));
        productDto.update(id, createProductForm("test product", "test brand", "test category","testbarcode1", "1000.00"));
    }

    @Test
    public void testTsvUploadValid() throws IOException, UploadException, ApiException {
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_brand_valid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        brandDto.tsvUpload(multipartFile);

        tsv = new FileInputStream(new File("src/test/resources/tsv/test_product_valid.tsv"));
        multipartFile = new MockMultipartFile("test.tsv", tsv);
        productDto.tsvUpload(multipartFile);


        List<ProductData> productDataList = productDto.getAll();
        assertEquals(3, productDataList.size());
    }

    @Test(expected = UploadException.class)
    public void testTsvUploadInvalid() throws IOException, UploadException, ApiException {
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_product_invalid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        brandDto.tsvUpload(multipartFile);

        tsv = new FileInputStream(new File("src/test/resources/tsv/test_invalid.tsv"));
        multipartFile = new MockMultipartFile("test.tsv", tsv);
        productDto.tsvUpload(multipartFile);
    }

    @Test(expected = UploadException.class)
    public void testTsvUploadInvalidType() throws IOException, UploadException, ApiException {
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_invalid_format"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        brandDto.tsvUpload(multipartFile);

        tsv = new FileInputStream(new File("src/test/resources/tsv/test_invalid.tsv"));
        multipartFile = new MockMultipartFile("test.tsv", tsv);
        productDto.tsvUpload(multipartFile);
    }

    @Test(expected = UploadException.class)
    public void testTesUploadDuplicate() throws UploadException, ApiException, IOException {
        brandDto.add(createBrandForm("test brand", "test category"));
        int id = productDto.add(createProductForm("test product",
                "test brand",
                "test category",
                "testbarcode1",
                "1000.00"));
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_product_invalid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        productDto.tsvUpload(multipartFile);
    }
}
