package it.polito.tesi.model;

import java.time.LocalTime;

public class Evento implements Comparable<Evento>{

	public enum EventType{
		ARRIVO_FERMATA, // arriva gente alla fermata
		SALITA_MEZZO,	// gente sale sul treno 
		DISCESA_MEZZO
	};
	
	private LocalTime dataOra ;
	private PassaggioCorsa passaggio ; 
	private EventType tipo ; 
	
	public Evento(LocalTime dataOra, PassaggioCorsa passaggio, EventType tipo) {
		this.dataOra = dataOra;
		this.passaggio = passaggio;
		this.setTipo(tipo) ;
	}

	public LocalTime getDataOra() {
		return dataOra;
	}

	public void setOra(LocalTime dataOra) {
		this.dataOra = dataOra;
	}
	
	public PassaggioCorsa getPassaggio() {
		return passaggio;
	}

	public void setPassaggio(PassaggioCorsa passaggio) {
		this.passaggio = passaggio;
	}

	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Evento o) {
		if(this.dataOra.isBefore(o.dataOra))
			return -1 ;
		else if (this.dataOra.isAfter(o.dataOra))
			return 1 ; 
		else return 0 ;
		
	}

	@Override
	public String toString() {
		return "Evento [dataOra=" + dataOra + ", passaggio=" + passaggio + "]";
	}

}
