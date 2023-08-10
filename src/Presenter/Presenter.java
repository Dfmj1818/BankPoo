package Presenter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import Exceptions.BreachOfAgeException;
import Exceptions.EmptyUserListException;
import Exceptions.InvalidLoanRequestException;
import Exceptions.LoanInProgressException;
import Exceptions.LowScoreException;
import Exceptions.OverPaidDateException;
import Exceptions.UserNotFoundException;
import Model.Bank;
import Model.Loan;
import Model.Quota;
import Model.User;
import View.View;

public class Presenter {	
	private View view;
	private Bank bank;


	public Presenter() {
		view=new View();
		bank=new Bank();	
	}

	public void loginUser() {
		LocalDate currentDate=LocalDate.now();
		LocalTime currentTime=LocalTime.now();
		String mail;
		String password;
		String name;
		String lastName;
		int age = 0;
		long identificationCard;
		long montlyIncome;
		int digitedOption;
		long wifeMontlyIncome;
		String userBirthDateAsString;
		DateTimeFormatter birthDayFormat;
		LocalDate digitedBirthDay = null;
		boolean correctFormat = false;
		//Metodo Para verificar la hora de ingreso
		if(currentTime.isAfter(LocalTime.of(17,0))){
			view.showMessage("Vuelve Mañana,Nuestro Horario de Servicios Es hasta Las 5:00 Pm");
		}
		else {
			do {
				view.showMessage("Bienvenido al Banco de Bogota\n¿Que Deseas Hacer hoy? ");
				view.showMessage("1.Registrarte\n2.Iniciar Sesion\n3.Salir De la Aplicacion");
				digitedOption=view.readInt();

				switch(digitedOption){
				case 1:
					view.showMessage("Ingresa Tu Correo Electronico");
					mail=view.readString();
					view.showMessage("Ingresa Tu Contraseña");
					password=view.readString();
					view.showMessage("Ingresa Tu nombre");
					name=view.readString();
					view.showMessage("Ingresa Tu apellido");
					lastName=view.readString();
					//Pendiente Mejorar
					view.showMessage("Ingresa tu Documento de ciudadania");
					identificationCard=view.readLong();

					while(!correctFormat) {
						try {
							view.showMessage("Digita tu fecha de Nacimiento en formato Dia/mes/año ");
							userBirthDateAsString=view.readString();
							birthDayFormat=DateTimeFormatter.ofPattern("dd/MM/yyyy");
							digitedBirthDay=LocalDate.parse(userBirthDateAsString, birthDayFormat);							
							age=bank.checkUserAge(digitedBirthDay);
							correctFormat=true;
						}
						catch(DateTimeParseException e) { 
							view.showMessage("Ocurrio un error con el formato digitado,vuelve a intentarlo");
						}
						catch(BreachOfAgeException e) {
							view.showMessage(" Error:"+e.getMessage());
							this.loginUser();
						}


					}

					User user=new User(mail,password,name,lastName,age,identificationCard);
					user.setBirthDay(digitedBirthDay);
					bank.addUserToDataBase(user);
					runServices(user);
					break;

				case 2:
					view.showMessage("Ingresa Tu correo Electronico");
					mail=view.readString();
					view.showMessage("Ingresa Tu Contraseña");
					password=view.readString();
					try {
						User currentUser=bank.getUserOfDataBase(mail, password);	  
						runServices(currentUser);
					}
					catch(UserNotFoundException e){
						view.showMessage("Error:"+e.getMessage());
						this.loginUser();
					}

					break;

				case 3:
					view.showMessage("Hasta Luego\nSaliendo de la aplicacion.....");
					System.exit(0);
					break;

				default:
					view.showMessage("La opcion "+digitedOption +" no existe,Vuelve a intentarlo");
					break;
				}

			}while(digitedOption!=3);

		}

	}

	public void runServices(User user) {
		int digitedOption=0;
		int paidOrUnpaidQuotas=0;
		long userMontlyIncome;
		int numberOfChildren = 0;
		int contractType= 0;
		long montlyExpenses=0;
		long digitedLoanAmount=0;
		int digitedNumberOfQuotas=0;
		long digitedQuotasAmount=0;
		int userScore=0;
		String yesOrNotAnswer="";
		LocalDate currentDate=LocalDate.now();
		boolean exit=false;
		int digitedQuotasToPay=0;
		while(!exit) {
			view.showMessage("Hemos Identificado Tu identidad,Bienvenido "+user.getName());
			view.showMessage("¿Que Deseas Hacer Hoy?"+"\n1.Solicitar Un Prestamo"+"\n2.Pagar Cuota De Un Credito\n3.Volver al Menu Principal");
			digitedOption=view.readInt();
			switch(digitedOption){
			case 1:
				try {
					bank.verifyUserLoanPending(user);
				}
				catch(LoanInProgressException e){
					view.showMessage(e.getMessage());

				}
				view.showMessage("Digita el tipo de Contrato Actual que tienes");
				view.showMessage("1.Formal(Termino Indefinido)\n2.Formal-(Termino Fijo)\n3.Informal\n4.Desempleado");
				contractType=view.readInt();
				view.showMessage("Digita Tu Salario Mensual");
				userMontlyIncome=view.readLong();
				view.showMessage("¿Cuantos Hijos Tiene?");
				numberOfChildren=view.readInt();
				view.showMessage("Digite sus Gastos Mensuales");
				montlyExpenses=view.readLong();
				try {
					userScore=bank.calculateLoanRisk(user,contractType,userMontlyIncome, numberOfChildren, montlyExpenses);
				}
				catch(LowScoreException e){
					view.showMessage(e.getMessage());
				}		
				int creditHistoryScore=bank.verifyCreditHistory(user);
				int totalScore=userScore+creditHistoryScore;
				ArrayList<Number>loanInformation=bank.calculateMaxAmountToLoan(totalScore,userMontlyIncome);
				view.showMessage("Puede Solicitar Un Prestamo Hasta Por una Cantidad de "+loanInformation.get(2)+"\nA 36 Cuotas Mensuales "+loanInformation.get(1)+" Cuotas."+"\nCon Un Interes Del "+loanInformation.get(0)+"\nPara Solicitar el Prestamo Debes Solicitar Al Menos:"+loanInformation.get(3)+" Cop.");
				view.showMessage("Digita La Cantidad Que Deseas Solicitar.");
				digitedLoanAmount=view.readLong();
				try {
					Loan userLoan=bank.createUserLoan(user,loanInformation, digitedLoanAmount);
					ArrayList<Quota>quotasList=bank.generateLoanQuotas(userLoan, digitedNumberOfQuotas);
					userLoan.setQuotasList(quotasList);
					view.showMessage("Prestamo Adquirido Exitosamente");
					user.addLoanToList(userLoan);
					String loanDetails=bank.showUserLoans(userLoan);
					view.showMessage(loanDetails);
				}
				catch(InvalidLoanRequestException e){
					view.showMessage(e.getMessage()+"\nVuelve A Intentarlo");

				}
				break;			

			case 2:
				view.showMessage("Bienvenido "+user.getName()+"\nA Continuacion Le mostraremos sus Prestamos Pendientes");
				try {
					bank.verifyUserLoansListIsEmpty(user);
				} 
				catch(EmptyUserListException e) {
					view.showMessage(e.getMessage()+"\nVolviendo al menu principal...");
					this.loginUser();
				}
				Loan userPendingLoan=bank.getUserLoanPending(user);
				String pendingLoanInformation=bank.showUserLoans(userPendingLoan);
				view.showMessage(pendingLoanInformation);
				view.showMessage("Selecciona para ver\n1.Cuotas Pagadas\n2.Cuotas Por Pagar\n3.Volver al Menu Principal");
				paidOrUnpaidQuotas=view.readInt();
				switch(paidOrUnpaidQuotas){
				case 1:
					try {
						ArrayList<Quota>userPaidQuotas=bank.getUserPaidQuotas(userPendingLoan);
						bank.verifyUserLoansListIsEmpty(user);
						bank.showQuotasInformation(userPaidQuotas);					
					}
					catch(EmptyUserListException e){
						view.showMessage(e.getMessage());
					}

					break;
				case 2:
					try {
						bank.verifyUserLoansListIsEmpty(user);
						ArrayList<Quota>userPendingQuotas=bank.getUserPendingQuotas(userPendingLoan);
						String pendingQuotasInformation=bank.showQuotasInformation(userPendingQuotas);
						view.showMessage(pendingQuotasInformation);
						bank.checkPaymentDate(user,userPendingQuotas,currentDate);	
						view.showMessage("¿Deseas Pagar Alguna Cuota?");
						yesOrNotAnswer=view.readString();
						if(yesOrNotAnswer.equalsIgnoreCase("yes")){
							view.showMessage("Digita La cantidad de Cuotas Que deseas Pagar");
							digitedQuotasToPay=view.readInt();
							view.showMessage("Digita El monto De las Cuotas Que deseas Pagar");
							digitedQuotasAmount=view.readLong();
							int paidQuotas=bank.paidQuota(userPendingQuotas,digitedQuotasAmount, digitedQuotasToPay, currentDate);
							view.showMessage("Haz Pagado Un Total "+paidQuotas+" Cuotas");
							bank.showQuotasInformation(userPendingQuotas);
						}
						
						else {
							exit=true;
						}
						
					}catch(OverPaidDateException e){
						view.showMessage(e.getMessage());

					}catch(EmptyUserListException e){
						view.showMessage(e.getMessage());
					}

					break;

				case 3:
					view.showMessage("Saliendo al Menu Principal");
					exit=true;
					this.loginUser();
					break;

				default:
					view.showMessage("La Opcion Digitada No existe,Por Favor Vuelve A Intentarlo");
					break;					
				}

				break;

			case 3:
				view.showMessage("Volviendo al Menu Principal....");
				this.loginUser();
				break;

			default:
				view.showMessage("La opcion numero"+digitedOption+" No Existe Por Favor Vuelva A Intentarlo");
				break;
			}

		}	
	}

	public void runPaidQuotaServices(int digited) {

	}


	public static void main(String[]args) {
		Presenter presenter=new Presenter();
		presenter.loginUser();
	}

}
