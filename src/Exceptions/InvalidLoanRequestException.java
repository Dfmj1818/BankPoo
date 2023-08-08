package Exceptions;

public class InvalidLoanRequestException extends RuntimeException {

	public InvalidLoanRequestException() {
		super("La Cantidad Solicitada o el numero de Cuotas Solicitadas Exceden El Maximo Permitido");
	}
}
