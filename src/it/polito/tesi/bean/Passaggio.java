package it.polito.tesi.bean;

import java.time.LocalTime;

public class Passaggio implements Comparable<Passaggio>{

	private Fermata fermata;	
	private LocalTime oraArrivo; 
	private LocalTime oraPartenza;
	private int sequenza ;
	
	public Passaggio(Fermata fermata, LocalTime oraArrivo, LocalTime oraPartenza, int sequenza) {
		super();
		this.fermata = fermata;
		this.oraArrivo = oraArrivo;
		this.oraPartenza = oraPartenza;
		this.sequenza = sequenza;
	}

	public Fermata getFermata() {
		return fermata;
	}

	public void setFermata(Fermata fermata) {
		this.fermata = fermata;
	}

	public LocalTime getOraArrivo() {
		return oraArrivo;
	}

	public void setOraArrivo(LocalTime oraArrivo) {
		this.oraArrivo = oraArrivo;
	}

	public LocalTime getOraPartenza() {
		return oraPartenza;
	}

	public void setOraPartenza(LocalTime oraPartenza) {
		this.oraPartenza = oraPartenza;
	}

	public int getSequenza() {
		return sequenza;
	}

	public void setSequenza(int sequenza) {
		this.sequenza = sequenza;
	}

	@Override
	public int compareTo(Passaggio o) {
		return this.sequenza - o.getSequenza();
	}

	@Override
	public String toString() {
		return  fermata + ": " + oraArrivo + " - " + oraPartenza
				+ " / " + sequenza ;
	}
	
}
