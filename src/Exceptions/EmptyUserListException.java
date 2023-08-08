package Exceptions;

public class EmptyUserListException extends RuntimeException{

	public EmptyUserListException() {
		super("No hay Prestamos que hayas hecho hasta el momento ");
	}
}
