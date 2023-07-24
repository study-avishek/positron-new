package com.increff.invoice.spring;


import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("com.increff.invoice")
@PropertySources({ //
		@PropertySource(value = "file:./invoice.properties", ignoreResourceNotFound = true) //
})
@EnableScheduling
public class SpringConfig {

}
