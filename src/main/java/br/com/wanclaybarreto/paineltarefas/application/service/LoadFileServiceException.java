package br.com.wanclaybarreto.paineltarefas.application.service;

@SuppressWarnings("serial")
public class LoadFileServiceException extends RuntimeException {
	
	public LoadFileServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoadFileServiceException(String message) {
		super(message);
	}

	public LoadFileServiceException(Throwable cause) {
		super(cause);
	}
	
}
