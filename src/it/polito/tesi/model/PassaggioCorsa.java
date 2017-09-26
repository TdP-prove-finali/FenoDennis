package it.polito.tesi.model;

import it.polito.tesi.bean.Corsa;
import it.polito.tesi.bean.Passaggio;

public class PassaggioCorsa extends Passaggio{

	private Corsa c ; 
	public PassaggioCorsa(Passaggio p, Corsa c) {
		super(p.getFermata(), p.getOraArrivo(), p.getOraPartenza(), p.getSequenza());
		this.c=c ;
	}
	public Corsa getC() {
		return c;
	}
	public void setC(Corsa c) {
		this.c = c;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PassaggioCorsa other = (PassaggioCorsa) obj;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return super.toString() + " - " + c.getLinea().getId();
	}
	
	
	
}
