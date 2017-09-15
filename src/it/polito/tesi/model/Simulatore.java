package it.polito.tesi.model;
import java.util.PriorityQueue;

public class Simulatore {
	
//  parametri di simulazione
//  modello del mondo
//	misure in uscita
	
	private PriorityQueue<Evento> coda ;

	public Simulatore() {
		this.coda = new PriorityQueue<Evento>();	
	}
	
	public void run(){
			
			while (!coda.isEmpty()){
				Evento e = coda.poll() ;
			}
	}
	
}
