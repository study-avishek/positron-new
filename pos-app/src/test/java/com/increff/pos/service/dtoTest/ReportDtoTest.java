package com.increff.pos.service.dtoTest;

import com.increff.pos.dto.OutwardOrderDto;
import com.increff.pos.dto.ReportDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OutwardOrderData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.util.OrderUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static com.increff.pos.service.util.ConstructorUtil.createSalesReportForm;
import static junit.framework.TestCase.assertEquals;

public class ReportDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderUtil orderUtil;

    @Autowired
    private OutwardOrderDto outwardOrderDto;

    @Autowired
    private ReportDto reportDto;

    @Test
    public void testGetAll() throws ApiException, IOException {
        orderUtil.createProduct(5);
        orderUtil.createOrder(5);
        List<OutwardOrderData> ordersDataList =  outwardOrderDto.getAll();
        orderUtil.completeOrder(ordersDataList);

        List<SalesReportData> salesReportData = reportDto.getAllSalesData(createSalesReportForm("","","",""));
        assertEquals(5, salesReportData.size());
    }

}
