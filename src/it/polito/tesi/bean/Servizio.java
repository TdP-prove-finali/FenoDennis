package it.polito.tesi.bean;

import java.util.Arrays;

public class Servizio {

	private String serviceId;
	private int[] settimana ;

	public Servizio(String serviceId, int[] settimana) {
		super();
		this.serviceId = serviceId;
		this.settimana = settimana;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int[] getSettimana() {
		return settimana;
	}

	public void setSettimana(int[] settimana) {
		this.settimana = settimana;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servizio other = (Servizio) obj;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  serviceId + ": " + Arrays.toString(settimana) ;
	}
	
	public boolean isService(int i){
		
		if (i>=0 && i<=6){
			if(this.settimana[i]==1)
				return true;
			else return false;
		}
		else return false; 
	}
	
}
