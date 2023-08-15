package Exceptions;

public class InsufficientsFundsException extends RuntimeException{

	public InsufficientsFundsException() {
		super("Fondos Insuficientes Para Pagar La Cuota");
	}
}
