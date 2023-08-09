package Exceptions;

public class BreachOfAgeException extends RuntimeException {
	public BreachOfAgeException() {
		super("Para registrarte debes tener entre 18 y 65 años");
	}

}
