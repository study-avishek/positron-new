package com.increff.pos.exception;

import lombok.Getter;

import java.util.List;


@Getter
public class ApiException extends Exception {
	private static final long serialVersionUID = 1L;
	private List<String> errorList;

	public ApiException(String string) {
		super(string);
	}

	public ApiException( String string, List<String> errorlist) {
		super(string);
		this.errorList = errorlist;
	}

}
