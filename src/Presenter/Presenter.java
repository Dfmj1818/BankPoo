package Presenter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import Exceptions.BreachOfAgeException;
import Exceptions.EmptyUserListException;
import Exceptions.ExceedMaxQuotaException;
import Exceptions.InsufficientsFundsException;
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
						}catch(DateTimeParseException e) { 
							view.showMessage("Ocurrio un error con el formato digitado,vuelve a intentarlo");
						}catch(BreachOfAgeException e) {
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
		boolean exitForSecondMenu=false;
		int digitedQuotasToPay=0;
		boolean correctFormat=false;
		int creditHistoryScore=0;
		int totalScore=0;
		int paidQuotas=0;
		Loan userLoan;
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
					view.showMessage(e.getMessage()+"\nVolviendo Al Menu Principal");
					this.loginUser();
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
					long userAvaiableMoney=bank.calculateUserAvaiableMoneyForCredit(numberOfChildren, userMontlyIncome, montlyExpenses);
					userScore=bank.calculateLoanRisk(user,contractType,userMontlyIncome, numberOfChildren, montlyExpenses,userAvaiableMoney);
					creditHistoryScore=bank.verifyCreditHistory(user);
					totalScore=userScore+creditHistoryScore;
					ArrayList<Number>loanInformation=bank.calculateMaxAmountToLoan(totalScore,userMontlyIncome);
					view.showMessage("Puede Solicitar Un Prestamo Hasta Por una Cantidad de "+loanInformation.get(2)+" Cop.\nA 36 Cuotas Mensuales "+loanInformation.get(1)+" Cuotas."+"\nCon Un Interes Del "+loanInformation.get(0));
					view.showMessage("Lo Minimo Que puedes Solicitar es "+loanInformation.get(3)+" \n(10% De Tu Maximo)");
					view.showMessage("Digita La Cantidad Que Deseas Solicitar.");
					digitedLoanAmount=view.readLong();
					userLoan=bank.createUserLoan(user,loanInformation, digitedLoanAmount);
					ArrayList<Quota>quotasList=bank.generateLoanQuotas(userLoan);
					userLoan.setQuotasList(quotasList);
					view.showMessage("Prestamo Adquirido Exitosamente");
					user.addLoanToList(userLoan);
					bank.addLoanToBankList(userLoan);
					String loanDetails=bank.showUserLoans(userLoan);
					view.showMessage(loanDetails);

				}catch(LowScoreException e){
					view.showMessage(e.getMessage());
				}catch(InvalidLoanRequestException e){
					view.showMessage(e.getMessage()+"\nVuelve A Intentarlo");
				}		

				break;

			case 2:
				view.showMessage("Bienvenido "+user.getName()+"\nA Continuacion Le mostraremos sus Prestamos Pendientes");
				try {
					bank.verifyUserLoansListIsEmpty(user);
				}catch(EmptyUserListException e) {
					view.showMessage(e.getMessage()+"\nVolviendo al menu principal...");
					this.loginUser();
				}
				Loan userPendingLoan=bank.getUserLoanPending(user);
				String pendingLoanInformation=bank.showUserLoans(userPendingLoan);
				view.showMessage(pendingLoanInformation);
				while(!exitForSecondMenu){
					view.showMessage("Selecciona para ver\n1.Cuotas Pagadas\n2.Cuotas Por Pagar\n3.Volver Al Menu Principal");
					paidOrUnpaidQuotas=view.readInt();
					switch(paidOrUnpaidQuotas){		
					case 1:	
						try {
							bank.verifyUserLoansListIsEmpty(user);
							ArrayList<Quota>userPaidQuotas=bank.getUserPaidQuotas(userPendingLoan);
							String quotasInformation=bank.showQuotasInformation(userPaidQuotas);	
							view.showMessage(quotasInformation);
						}catch(EmptyUserListException e){
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
							view.showMessage("¿Deseas Pagar Alguna Cuota?\nEscribe Si Para Confirmar");
							yesOrNotAnswer=view.readString();
							if(yesOrNotAnswer.equals("si")){			
								view.showMessage("Digita La cantidad de Cuotas Que deseas Pagar");
								digitedQuotasToPay=view.readInt();
								view.showMessage("Digita El monto monetario De las Cuotas Que deseas Pagar");
								digitedQuotasAmount=view.readLong();
								paidQuotas=bank.paidQuota(userPendingQuotas,digitedQuotasAmount, digitedQuotasToPay, currentDate);
								view.showMessage("Haz Pagado Un Total "+paidQuotas+" Cuota(S)");
								view.showMessage("----------------------------------------------------");
							}
							else {
								exitForSecondMenu=false;
							}
						}catch(ExceedMaxQuotaException e){
							view.showMessage(e.getMessage()+"\nVerifica El Numero de Cuotas Digitado Y Vuelve a Intentarlo");
						}catch(InsufficientsFundsException e){
							view.showMessage(e.getMessage()+"\nVerifica el Monto Digitado Y Vuelve a Intentarlo");
						}catch(OverPaidDateException e){
							view.showMessage(e.getMessage());
							exit=true;
						}catch(EmptyUserListException e){
							view.showMessage(e.getMessage());
							exit=true;
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
				}

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
	public static void main(String[]args) {
		Presenter presenter=new Presenter();
		presenter.loginUser();
	}

}
