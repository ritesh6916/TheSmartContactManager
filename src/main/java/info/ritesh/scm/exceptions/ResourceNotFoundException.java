package info.ritesh.scm.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	// add constructor
	public ResourceNotFoundException(String message) {
		super(message);
	}

	// no-arg constructor
	public ResourceNotFoundException() {
		super();
	}
}
