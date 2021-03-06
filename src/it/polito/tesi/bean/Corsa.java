package it.polito.tesi.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Corsa {
	
	private Linea linea ;
	private Servizio servizio ;
	private String tripId ;
	private String capolinea ;
	private List<Passaggio> passaggi ;
	
	public Corsa(Linea linea, Servizio servizio, String tripId, String capolinea) {
		this.linea = linea;
		this.servizio = servizio;
		this.tripId = tripId;
		this.capolinea = capolinea;
		this.passaggi = new ArrayList<>();
	}

	public List<Passaggio> getPassaggi() {
		return passaggi;
	}

	public void setPassaggi(List<Passaggio> passaggi) {
		this.passaggi = passaggi;
	}

	public void addPassaggio(Passaggio p){
		this.passaggi.add(p) ;
	}
	
	public void ordinaPassaggi(){
		Collections.sort(this.passaggi);
	}
	
	public Linea getLinea() {
		return linea;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

	public Servizio getServizio() {
		return servizio;
	}

	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getCapolinea() {
		return capolinea;
	}

	public void setCapolinea(String capolinea) {
		this.capolinea = capolinea;
	}

	@Override
	public String toString() {
		return linea + ", capolinea:" + capolinea ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
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
		Corsa other = (Corsa) obj;
		if (tripId == null) {
			if (other.tripId != null)
				return false;
		} else if (!tripId.equals(other.tripId))
			return false;
		return true;
	}

}