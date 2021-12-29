package br.com.wanclaybarreto.paineltarefas.application.service;

@SuppressWarnings("serial")
public class UploadServiceException extends RuntimeException {
	
	public UploadServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadServiceException(String message) {
		super(message);
	}

	public UploadServiceException(Throwable cause) {
		super(cause);
	}
	
}
