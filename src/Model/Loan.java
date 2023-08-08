package Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Loan {
	 private Quota quota;
	 private User user;
	 private int loanId;
	 private long loanAmount;
	 private double loanInterestQuota;
     private LocalDate startDate;
     private LocalDate dueDate;
     private boolean stateOfLoan;
     private int numberOfQuotas;
     private ArrayList<Quota>loanQuotas;
      
	public Loan(){
		this.loanQuotas=new ArrayList<Quota>();
		this.user=user;
		this.loanId=loanId;
		this.loanAmount=loanAmount;
		this.loanInterestQuota=loanInterestQuota;
		this.stateOfLoan=stateOfLoan;
		
	}
	
	public void setQuota(Quota quota){
		this.quota=quota;
	}
	
	public Quota getQuota() {
		return quota;
	}
	
	 public void setUserLoan(User user){
		 this.user=user;
	 }
	 
	 public User getUserLoan() {
		 return user;
	 }
	
	 public void setLoanId() {
		 this.loanId++;
	 }
	 
	 public int getLoanId() {
		 return loanId;
	 }
	 
	 public void setLoanAmount(long loanAmount) {
		 this.loanAmount=loanAmount;
	 }
	 
	 public long getLoanAmount() {
		 return loanAmount;
	 }
	 
	 public void setInterestQuota(double interestQuota) {
		 this.loanInterestQuota=interestQuota;
	 }
	 
	 public double getInterestQuota() {
		 return loanInterestQuota;
	 }
	 
	 public void setStartDate(LocalDate startDate) {
		 this.startDate=startDate;
	 }
	 
	 public LocalDate getStartDate() {
		 return startDate;
	 }
	 
	 public void setDueDate(LocalDate dueDate){
		 this.dueDate=dueDate;
	 }
	 
	 public LocalDate getDueDate() {
		 return dueDate;
	 }
	 
	 public void setStateOfLoan(boolean stateOfLoan) {
		 this.stateOfLoan=stateOfLoan;
	 }
	 
	 public boolean getStateOfLoan() {
		 return stateOfLoan;
	 }
	 
	 public void setNumberOfQuotas(int numberOfQuotas) {
		 this.numberOfQuotas=numberOfQuotas;
	 }
	 
	 public int getNumberOfQuotas() {
		 return numberOfQuotas;
	 }
	 
	 public void setQuotasList(ArrayList<Quota>loanQuotas) {
		 this.loanQuotas=loanQuotas;
	 }
	 
	 public ArrayList<Quota>getQuotasList(){
       	 return loanQuotas;
	 }
	 
	 
	 
	 
}
