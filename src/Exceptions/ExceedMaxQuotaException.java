package Exceptions;

public class ExceedMaxQuotaException extends RuntimeException {

	public ExceedMaxQuotaException() {
		super("Ocurrio Un Error,Ingresa De nuevo un numero Que sea menor al numero de cuotas que debes");
	}
}
