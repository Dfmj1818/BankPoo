package Exceptions;

public class LowScoreException extends RuntimeException {

	public LowScoreException() {
		super("Lo Sentimos Tu Solicitud de Prestamo Ha Sido Rechazada Debido a que tu puntaje crediticio es muy Bajo");
	}
}
