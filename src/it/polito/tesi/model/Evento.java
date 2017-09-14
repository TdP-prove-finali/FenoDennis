package it.polito.tesi.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{

	private LocalDateTime dataOra ;
	
	public LocalDateTime getDataOra() {
		return dataOra;
	}

	public void setDataOra(LocalDateTime dataOra) {
		this.dataOra = dataOra;
	}

	@Override
	public int compareTo(Evento o) {
		if(this.dataOra.isBefore(o.dataOra))
			return -1 ;
		else if (this.dataOra.isAfter(o.dataOra))
			return 1 ; 
		else return 0 ;
		
	}

}
