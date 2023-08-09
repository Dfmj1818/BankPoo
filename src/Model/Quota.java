package Model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Quota {
	private int quotaId;	
	private long quotaAmount;
	private LocalDate startDate;
	private LocalDate dueDate;
	private boolean stateOfQuota;
	private LocalDate dayOfPayment;
	private LocalDate maxDueDate;
	public Quota(){
		
	}

	public void setQuotaId(int quotaId){
		this.quotaId=quotaId;
	}

	public int getQuotaID() {
		return quotaId;
	}

	public void setQuotaAmount(long quotaCost){
		this.quotaAmount=quotaCost;
	}

	public long getQuotaAmount() {
		return quotaAmount;
	}  

	public void setStartDate(LocalDate startDate ){
		this.startDate=startDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate=dueDate; 
	}

	public LocalDate getDueDate() {
		return dueDate;
	}
	
	public void setMaxDueDate(LocalDate maxDueDate){
		this.maxDueDate=maxDueDate;
	}
	
	public LocalDate getMaxDueDate() {
		return maxDueDate;
	}

	public void setStateOfQuota(boolean stateOfQuota) {
		this.stateOfQuota=stateOfQuota;
	}

	public boolean getStateOfQuota(){
		return stateOfQuota;
	}

	public void setDayOfPayment(LocalDate dateOfPayment) {
		this.dayOfPayment=dateOfPayment;
	}

	public LocalDate getDayOfPayment() {
		return dayOfPayment;
	}
	
}
