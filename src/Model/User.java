package Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class User {
	private String mail;
	private String password;
	private String name;
	private String lastname;
	private LocalDate birthDate;
	private ArrayList<Loan>loansList;
	private int age;
	private long identificationCard;
    private int score;
    private boolean stateOfUser;
	public User(String mail,String password,String name,String lastName,int age,long identificationCard){
		this.mail=mail;
		this.password=password;
		this.name=name;
		this.lastname=lastName;
		this.loansList=new ArrayList<Loan>();
		this.age=age;
		this.identificationCard=identificationCard;
		
	}

	public void addLoanToList(Loan loan){
		loansList.add(loan);
	}


	public void setUserMail(String mail){
		this.mail=mail;
	}

	public String getUserMail() {
		return mail;
	}

	public void setPassword(String password) {
		this.password=password;
	}

	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setLastName(String lastName) {
		this.lastname=lastName;
	}

	public String getLastName() {
		return lastname;
	}

	public String viewUserLoansList() {
		StringBuilder loansInformation=new StringBuilder();

		for(int i=0;i<loansList.size();i++){
			loansInformation.append("Nombre del Prestamista: ");
			loansInformation.append("\n");
			loansInformation.append(loansList.get(i).getUserLoan().getName());
			loansInformation.append(" ");
			loansInformation.append(loansList.get(i).getUserLoan().getLastName());
			loansInformation.append("\n");
			loansInformation.append("Cantidad Prestada:");
			loansInformation.append(loansList.get(i).getLoanAmount());
			loansInformation.append("\n");
			loansInformation.append("Fecha De Inicio Del Prestamo: ");
			loansInformation.append(loansList.get(i).getStartDate());
			loansInformation.append("\n");
			loansInformation.append("Fecha De Vencimiento Del Prestamo: ");
			loansInformation.append(loansList.get(i).getDueDate());
			loansInformation.append("\n");
			loansInformation.append("Numero De Cuotas: ");
			loansInformation.append(loansList.get(i).getNumberOfQuotas());
			loansInformation.append("\n");
			loansInformation.append("Interes Del Prestamo: ");
			loansInformation.append(loansList.get(i).getInterestQuota());
		}
		return loansInformation.toString();
	}

	public void setBirthDay(LocalDate birthDay) {
		this.birthDate=birthDay;
	}
	
	public LocalDate getBirthDay() {
		return birthDate;
	}
	
	public void setAge(int age) {
		this.age=age;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setIdentificationCard(long identificationCard) {
		this.identificationCard=identificationCard;
	}
	
	public void setUserLoanList(ArrayList<Loan>loansList) {
		this.loansList=loansList;
	}
	
	public ArrayList<Loan>getLoansList(){
		return loansList;
	}
	
	public long getIdentificationCard() {
		return identificationCard;
	}
    
	
	public void setScore(int score) {
		this.score=score;
	}
	
	public int getScore() {
		return score;
	}
	
    public void setStateOfUser(boolean stateOfUser) {
        this.stateOfUser=stateOfUser;    	
    }
    
    public boolean getStateOfUser() {
    	return stateOfUser;
    }
    
    
    
	
	
	

}
