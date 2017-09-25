package it.polito.tesi;

import it.polito.tesi.bean.Linea;

public class LineaNumero implements Comparable<LineaNumero>{

	private Linea linea ;
	private int numero ;
	
	public LineaNumero(Linea linea, int numero) {
		super();
		this.linea = linea;
		this.numero = numero;
	}

	public Linea getLinea() {
		return linea;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public int compareTo(LineaNumero o) {
		return o.getNumero() - this.numero ;
	}
	
	@Override
	public String toString() {
		return linea + ": " + numero ;
	}
}
