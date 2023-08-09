package Exceptions;

public class LoanInProgressException extends RuntimeException {

	public LoanInProgressException() {
		super("Ocurrio Un Error,Parece Que ya Tienes Un Prestamo en Proceso\nPor Favor Completa el pago de este Para realizar Otro.");
	}
}
