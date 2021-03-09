package br.com.clients.microservices.domain.exception;

public class EntityBusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EntityBusinessException(String message) {
		super(message);
	}

}
