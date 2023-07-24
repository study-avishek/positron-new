package com.increff.pos.service.dtoTest;

import com.increff.pos.api.PosDaySaleApi;
import com.increff.pos.dao.PosDaySaleDao;
import com.increff.pos.dto.PosDaySaleDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.PosDaySaleData;
import com.increff.pos.pojo.PosDaySalePojo;
import com.increff.pos.service.AbstractUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@Service
public class PosDaySaleDtoTest extends AbstractUnitTest {

    @Autowired
    private PosDaySaleDto dto;

    @Autowired
    private PosDaySaleDao dao;

    @Test
    public void testGetAll() throws InterruptedException, ApiException {
        Thread.sleep(1000);
        List<PosDaySaleData> data = dto.getAll("01-01-1900","01-01-2100");
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-LLLL-yyyy")), data.get(0).getDate());
        assertEquals(0, data.get(0).getItemCount());
        assertEquals(0, data.get(0).getOrderCount());

    }
}
