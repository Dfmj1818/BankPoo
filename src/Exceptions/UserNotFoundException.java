package Exceptions;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {	
		super("El Usuario Digitado No Existe,Por favor vuelve a Intentarlo");
	}
}
