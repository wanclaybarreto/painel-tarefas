package br.com.wanclaybarreto.paineltarefas.application.service;

@SuppressWarnings("serial")
public class ApplicationServiceException extends Exception {
	
	public ApplicationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationServiceException(String message) {
		super(message);
	}

	public ApplicationServiceException(Throwable cause) {
		super(cause);
	}
	
}
