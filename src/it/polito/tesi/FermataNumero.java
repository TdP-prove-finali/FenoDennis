package it.polito.tesi;

import it.polito.tesi.bean.Fermata;

public class FermataNumero implements Comparable<FermataNumero>{

	private Fermata fermata ; 
	private int numero ;
	
	public FermataNumero(Fermata fermata, int numero) {
		super();
		this.fermata = fermata;
		this.numero = numero;
	}

	@Override
	public int compareTo(FermataNumero o) {
		return o.getNumero() - this.numero;
	}

	public Fermata getFermata() {
		return fermata;
	}

	public void setFermata(Fermata fermata) {
		this.fermata = fermata;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return fermata + ": " + numero ;
	}

	
	
}
