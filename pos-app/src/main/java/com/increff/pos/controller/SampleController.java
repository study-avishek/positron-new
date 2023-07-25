package com.increff.pos.controller;

import com.increff.pos.exception.ApiException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;

@Controller
public class SampleController {

	//Spring ignores . (dot) in the path. So we need fileName:.+
	//See https://stackoverflow.com/questions/16332092/spring-mvc-pathvariable-with-dot-is-getting-truncated
	@RequestMapping(value = "/api/resource/{filename:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String filename) throws ApiException {
		try {
			Resource resource = new ClassPathResource("/com/increff/pos/" + filename);

			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			if(Objects.equals(FilenameUtils.getExtension(filename), ".pdf")){
				headers.setContentType(MediaType.APPLICATION_PDF);
			}
			else if(Objects.equals(FilenameUtils.getExtension(filename), ".tsv")){
				headers.setContentType(MediaType.parseMediaType("text/tab-separated-values"));
			}
			return ResponseEntity.ok()
					.headers(headers)
					.body(resource);
		}
		catch (Exception e){
			return ResponseEntity.notFound().build();
		}
	}

}
