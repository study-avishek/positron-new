package com.increff.pos.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.increff.pos.exception.UploadException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.increff.pos.model.data.MessageData;
import com.increff.pos.exception.ApiException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class AppRestControllerAdvice {

	public static Logger logger = Logger.getLogger("AppRestControllerAdvice.class");

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handle(ApiException e) {
		MessageData data = new MessageData();
		data.setMessage(e.getMessage());
		data.setErrorList(e.getErrorList());
		return data;
	}

	@ExceptionHandler(IOException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handle(IOException e) {
		MessageData data = new MessageData();
		data.setMessage("Can't be casted, please enter appropriate value");
		return data;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public List<MessageData> handleValidationException(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		List<MessageData> errorMessages = new ArrayList<>();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			MessageData md = new MessageData();
			md.setMessage(fieldError.getDefaultMessage());
			errorMessages.add(md);
		}

		return errorMessages;
	}


	@ExceptionHandler(UploadException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public UploadException handleUploadExeption(UploadException exception){
		return exception;
	}


	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageData handle(Throwable e) {
		MessageData data = new MessageData();
		data.setMessage("Oops!! Something went wrong on our end. Try again later or contact support");
		logger.error(Arrays.asList(e.getStackTrace()));
		return data;
	}
}