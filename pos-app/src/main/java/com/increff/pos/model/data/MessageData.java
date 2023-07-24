package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class MessageData {
	private String message;
	private String detailedMessage;
	private List<String> errorList;
}
