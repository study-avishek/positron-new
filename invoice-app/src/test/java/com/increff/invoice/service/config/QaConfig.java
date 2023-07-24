package com.increff.invoice.service.config;

import com.increff.invoice.spring.SpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(//
		basePackages = { "com.increff.invoice" }, //
		excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
		@PropertySource(value = "classpath:./com/increff/invoice/test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
