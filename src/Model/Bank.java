
package Model;


import java.time.LocalDate;
import java.time.Period;
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
import View.View;

public class Bank {
	private ArrayList<Loan>loansListHistory;
	private ArrayList<User>usersDataBase;
	private View view;

	public Bank() {
		loansListHistory=new ArrayList<Loan>();
		usersDataBase=new ArrayList<User>();
		view=new View();
	}


	public void addLoanToBankList(Loan loan){
		loansListHistory.add(loan);
	}


	public void removeLoanBank(){
		for(int i=0;i<loansListHistory.size();i++) {
			if(loansListHistory.get(i).getStateOfLoan()==true){
				loansListHistory.remove(i);
				break;
			}
		}
	}



	public void addUserToDataBase(User user) {
		usersDataBase.add(user);
	}


	public int checkUserAge(LocalDate birthDay){
		LocalDate currentDate=LocalDate.now();
		Period userBirthday=Period.between(birthDay,currentDate);    
		if(userBirthday.getYears()>=18&&userBirthday.getYears()<=65) {
			return userBirthday.getYears();
		} 
		else{
			throw new BreachOfAgeException();
		}

	}

	public User getUserOfDataBase(String mail,String password){
		for(int i=0;i<usersDataBase.size();i++){
			if(usersDataBase.get(i).getUserMail().equals(mail)&&usersDataBase.get(i).getPassword().equals(password)) {
				User foundUser=usersDataBase.get(i);
				return foundUser;
			}	
		}		
		throw new UserNotFoundException();
	}
	
	public long calculateUserAvaiableMoneyForCredit(int numberOfChildren,long userMontlyIncome,long montlyExpenses) {
		long totalUserExpenses=(numberOfChildren*200000)+montlyExpenses; 
		long userAvaiableMoney=userMontlyIncome-totalUserExpenses;
		if(userAvaiableMoney<300000){
			throw new LowScoreException();
		}
		return userAvaiableMoney;
	}

	public int calculateLoanRisk(User user,int contractType,long userMontlyIncome,int numberOfChildren,long montlyExpenses,long userAvaiableMoney) {
		//Verificacion De Contrato
		if(contractType==1 || contractType==2){
			user.setScore(user.getScore()+100);
		}
		else if(contractType==3){
			user.setScore(user.getScore()+50);
		}
		else{
			user.setScore(user.getScore()+0);
		}

		//Verificacion De Salario
		if(userAvaiableMoney>3000000){
			user.setScore(user.getScore()+100);
		}
		else if(userAvaiableMoney>=2000000&&userAvaiableMoney<=3000000){
			user.setScore(user.getScore()+75);
		}
		else if(userAvaiableMoney>=1000000&&userAvaiableMoney<=2000000){
			user.setScore(user.getScore()+50);
		}
		else if(userAvaiableMoney<=1000000&&userAvaiableMoney>=800000){
			user.setScore(user.getScore()+25);
		}
		else {
			user.setScore(user.getScore()+0);
		}
		//Verificacion De hijos

		if(numberOfChildren>0){
			user.setScore(user.getScore()-25*numberOfChildren);		
		}

		else {
			user.setScore(user.getScore()+25);
		}	
		return user.getScore();      
	}

	public int verifyCreditHistory(User user){	
		int creditHistoryScore=0;

		for(Loan loan:loansListHistory){
			if(loan.getUserLoan().equals(user)){
				Loan userLoan=loansListHistory.get(creditHistoryScore);
				if(userLoan.getStateOfLoan()){
					creditHistoryScore+=50;
				}
				else if(!userLoan.getStateOfLoan()){
					view.showMessage("Tienes un Prestamo Pendiente,debes pagarlo antes de volver a Solicitar Otro"); 
					break;
				}
			}
		}
		return creditHistoryScore;

	}

	public ArrayList<Number>calculateMaxAmountToLoan(int totalScore,long userAvaiableMoney){
		ArrayList<Number>loanInformation=new ArrayList<Number>();
		long maxAmountToLoan=0;
		double interestQuota=0;
		int maxNumberOfQuotas=0;
		long minimumAmountToLoan=0;
		final long userAvaiableMoneyFormula=(long) (userAvaiableMoney*0.3);

		if(totalScore>=275){
			interestQuota=0.3;
			maxNumberOfQuotas=72;
			maxAmountToLoan=userAvaiableMoneyFormula*maxNumberOfQuotas;
			minimumAmountToLoan=(long) (maxAmountToLoan*0.10);
		}
		else if(totalScore>=250){
			interestQuota=0.4;
			maxNumberOfQuotas=36;
			maxAmountToLoan=userAvaiableMoneyFormula*maxNumberOfQuotas; 
			minimumAmountToLoan=(long) (maxAmountToLoan*0.10);
		}
		else if(totalScore>=225){
			interestQuota=0.45;
			maxNumberOfQuotas=36;
			maxAmountToLoan= userAvaiableMoneyFormula*maxNumberOfQuotas;
			minimumAmountToLoan=(long) (maxAmountToLoan*0.10);
		}
		else if(totalScore>=175){
			interestQuota=0.5;
			maxNumberOfQuotas=24;
			maxAmountToLoan= userAvaiableMoneyFormula*maxNumberOfQuotas;
			minimumAmountToLoan=(long) (maxAmountToLoan*0.10);
		}
		else if(totalScore>=125){
			interestQuota=0.55;
			maxNumberOfQuotas=24;
			maxAmountToLoan= userAvaiableMoneyFormula*maxNumberOfQuotas;
			minimumAmountToLoan= (long) (maxAmountToLoan*0.10);
		}
		else if(totalScore>=100){
			interestQuota=0.6;
			maxNumberOfQuotas=12;
			maxAmountToLoan=userAvaiableMoneyFormula*maxNumberOfQuotas;
			minimumAmountToLoan=(long) (maxAmountToLoan*0.10);
		}
		else if(totalScore>=75){
			interestQuota=0.6;
			maxNumberOfQuotas=12;
			maxAmountToLoan=userAvaiableMoneyFormula*maxNumberOfQuotas;
			minimumAmountToLoan=(long) (maxAmountToLoan*0.10);
		}
		else {
			throw new LowScoreException();
		}
		loanInformation.add(interestQuota);
		loanInformation.add(maxNumberOfQuotas);
		loanInformation.add(maxAmountToLoan);
		loanInformation.add(minimumAmountToLoan);

		return loanInformation;
	}

	public Loan createUserLoan(User user,ArrayList<Number>loanInformation,long digitedAmount){
		long maxAmountToLoan=(long) loanInformation.get(2).longValue();	
		int maxNumberOfQuotas=(int) loanInformation.get(1).intValue();
		double interestQuota=(double) loanInformation.get(0).doubleValue();
		long minimumAmountToLoan=(long)loanInformation.get(3).longValue();
		if(digitedAmount<=maxAmountToLoan&&digitedAmount>=minimumAmountToLoan){
			Loan currentLoan=new Loan();
			currentLoan.setLoanId();
			currentLoan.setLoanAmount(digitedAmount);
			currentLoan.setInterestQuota(interestQuota);
			currentLoan.setStartDate(LocalDate.now());
			currentLoan.setDueDate(LocalDate.now().plusMonths(maxNumberOfQuotas));
			currentLoan.setNumberOfQuotas(maxNumberOfQuotas);
			currentLoan.setStateOfLoan(false);
			currentLoan.setUserLoan(user);
			return currentLoan;

		}
		throw new InvalidLoanRequestException();

	}

	public ArrayList<Quota>generateLoanQuotas(Loan loan) {
		ArrayList<Quota>currentQuotasList=new ArrayList<Quota>();
		long quotaCost=loan.getLoanAmount()/loan.getNumberOfQuotas();	
		for(int i=0;i<loan.getNumberOfQuotas();i++){
			int quotasCounter=i+1;
			Quota currentQuota=new Quota();
			currentQuota.setQuotaAmount(quotaCost);
			currentQuota.setQuotaId(quotasCounter);
			currentQuota.setStartDate(loan.getStartDate().plusMonths(i));
			currentQuota.setDueDate(loan.getStartDate().plusMonths(i+1));
			currentQuota.setMaxDueDate(loan.getDueDate().plusDays(5));
			currentQuotasList.add(currentQuota);	
		}
		return currentQuotasList;
	}


	//aca es 
	public Loan getUserLoanPending(User user){	
		for(int i=0;i<user.getLoansList().size();i++) {
			for(int j=0;j<user.getLoansList().get(i).getQuotasList().size();j++){
				if(!user.getLoansList().get(i).getQuotasList().get(j).getStateOfQuota()){
					return user.getLoansList().get(i);
				}
			}
		}
		return null;
	}

	public void verifyUserLoanPending(User user) {
		for(int i=0;i<user.getLoansList().size();i++){
			if(!user.getLoansList().get(i).getStateOfLoan()){
				throw new LoanInProgressException();
			}
		}

	}

	public ArrayList<Quota>getUserPendingQuotas(Loan loan){
		ArrayList<Quota>pendingQuotas=new ArrayList<Quota>();		
		for(int i=0;i<loan.getQuotasList().size();i++){
			if(!loan.getQuotasList().get(i).getStateOfQuota()){ 
				pendingQuotas.add(loan.getQuotasList().get(i));	  
			}       	
		}

		return pendingQuotas;
	}

	public ArrayList<Quota>getUserPaidQuotas(Loan loan){
		ArrayList<Quota>paidQuotas=new ArrayList<Quota>();
		for(int i=0;i<paidQuotas.size();i++){
			if(loan.getQuotasList().get(i).getStateOfQuota()){
				paidQuotas.add(loan.getQuotasList().get(i));
			}      
		}
		if(paidQuotas.isEmpty()){
			throw new EmptyUserListException();
		}
		return paidQuotas;
	}

	public void checkPaymentDate(User user,ArrayList<Quota>pendingQuotas,LocalDate currentDate){
		for(Quota pendingQuota :pendingQuotas){
			if(currentDate.isAfter(pendingQuota.getMaxDueDate())){
				user.setStateOfUser(false);
				throw new OverPaidDateException();
			}

		}
	}

	public int paidQuota(ArrayList<Quota>pendingQuotas,long digitedAmount,int digitedQuotasToPay,LocalDate currentDate){
		int paidQuotas=0;

		if(digitedQuotasToPay>pendingQuotas.size()){
			throw new ExceedMaxQuotaException();
		}
		else {
			for(Quota pendingQuota :pendingQuotas){
				if(paidQuotas>=digitedQuotasToPay){
					break;
				}
				if(digitedAmount>=pendingQuota.getQuotaAmount()){
					paidQuotas++;
					pendingQuota.setStateOfQuota(true);
					pendingQuota.setDayOfPayment(currentDate);
					digitedAmount-=pendingQuota.getQuotaAmount();	
				}
				else {
					view.showMessage("No tienes Fondos Suficientes para pagar esta cuota");
					break;
				}

			}
			return paidQuotas;
		}
	}


	public void verifyEmail(String mail) {

	}

	public void verifyPassword() {

	}

	public void verifyUserLoansListIsEmpty(User user){
		if(user.getLoansList().isEmpty()){
			throw new EmptyUserListException();
		}
	}

	public String showUserLoans(Loan pendingLoan){
		StringBuilder userPendingLoansInformation=new StringBuilder();

		userPendingLoansInformation.append("ID :").append(pendingLoan.getLoanId()).append("\n");
		userPendingLoansInformation.append("Nombre Del Prestamista: ").append(pendingLoan.getUserLoan().getName()).append(" ").append(pendingLoan.getUserLoan().getLastName()).append("\n");
		userPendingLoansInformation.append("Cantidad Solicitada: ").append(pendingLoan.getLoanAmount()).append(" Cop").append("\n");
		userPendingLoansInformation.append("Fecha De Inicio Del Prestamo: ").append(pendingLoan.getStartDate()).append("\n");
		userPendingLoansInformation.append("Fecha De Terminacion Del Prestamo: ").append(pendingLoan.getDueDate()).append("\n");
		userPendingLoansInformation.append("Numero De Cuotas Del Prestamo: ").append(pendingLoan.getQuotasList().size()).append("\n");
		return userPendingLoansInformation.toString();	
	}


	public String showQuotasInformation(ArrayList<Quota>quotasList){
		StringBuilder quotaInfromation=new StringBuilder();

		for(int i=0;i<quotasList.size();i++){  
			Quota currentQuota=quotasList.get(i);
			quotaInfromation.append("Id: ").append(currentQuota.getQuotaID()).append("\n");;
			quotaInfromation.append("Valor de la Cuota: ").append(currentQuota.getQuotaAmount()).append(" Cop").append("\n");		
			quotaInfromation.append("Fecha de Inicio De la Cuota: ").append(currentQuota.getStartDate()).append("\n");
			quotaInfromation.append("Fecha De Vencimiento De la Cuota: ").append(currentQuota.getDueDate()).append("\n");
			if(currentQuota.getStateOfQuota()){
				quotaInfromation.append("Estado de la cuota: Pagado el dia "+currentQuota.getDayOfPayment()).append("\n");
				quotaInfromation.append("-----------------------------------------------------------------------------------").append("\n");
			}
			else if(!currentQuota.getStateOfQuota()){
				quotaInfromation.append("Estado de la cuota: Sin Pagar").append("\n");
				quotaInfromation.append("-----------------------------------------------------------------------------------").append("\n");
			}
		}
		return quotaInfromation.toString();
	}

}
