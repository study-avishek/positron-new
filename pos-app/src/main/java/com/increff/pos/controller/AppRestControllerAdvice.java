package com.increff.pos.controller;

import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.MessageData;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
		data.setMessage("Oops!! Something went wrong on our end. Try again later or contact support "+e.getMessage());
		data.setDetailedMessage(Arrays.toString(e.getStackTrace()));
		logger.error(Arrays.asList(e.getStackTrace()));
		return data;
	}
}