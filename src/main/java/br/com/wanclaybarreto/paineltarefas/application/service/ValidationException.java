package br.com.wanclaybarreto.paineltarefas.application.service;

@SuppressWarnings("serial")
public class ValidationException extends Exception {
	
	public ValidationException(String message) {
		super(message);
	}
}
