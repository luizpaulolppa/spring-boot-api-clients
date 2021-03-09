package br.com.clients.microservices.domain.exception;

public class EntityConflictException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EntityConflictException(String message) {
		super(message);
	}

}
