package Exceptions;

public class OverPaidDateException extends RuntimeException{

	public OverPaidDateException() {
		super("Haz Excedido La Fecha de Pago Maxima,Por lo Tanto haz Sido Reportado en nuestro Sistema");
	}
}
