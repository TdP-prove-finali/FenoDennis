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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fermata == null) ? 0 : fermata.hashCode());
		result = prime * result + ((oraArrivo == null) ? 0 : oraArrivo.hashCode());
		result = prime * result + sequenza;
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
		Passaggio other = (Passaggio) obj;
		if (fermata == null) {
			if (other.fermata != null)
				return false;
		} else if (!fermata.equals(other.fermata))
			return false;
		if (oraArrivo == null) {
			if (other.oraArrivo != null)
				return false;
		} else if (!oraArrivo.equals(other.oraArrivo))
			return false;
		if (sequenza != other.sequenza)
			return false;
		return true;
	}
	
}
