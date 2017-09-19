package it.polito.tesi.model;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tesi.bean.Corsa;
import it.polito.tesi.bean.Passaggio;

public class Simulatore {
		
//  parametri di simulazione
	 
	private int capienzaMezzo ;	// numero di persone
	private int tempoInStazione ; // in secondi
	
	private double probabilitaPermanenzaLinea;
	private double probabilitaPermanenzaLineaFlusso;
	
//  modello del mondo
	
	Map<FermataSuLinea, Treno> treni ; // forse meglio una mappa
	DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo ;
	Map<String, Corsa> corse ;
	List<Passaggio> passaggiSimulazione ;
	
//	misure in uscita
	
	int clientiSoddisfatti ; 
	int clientiParzialmenteSoddisfatti ;
	int clientiInsoddisfatti ;
	int clienti ;
	
	private PriorityQueue<Evento> coda ;
	
	public Simulatore() {
		this.coda = new PriorityQueue<Evento>();	
	}
	
	public Simulatore(DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo,
			Map<String, Corsa> corse, List<Passaggio> passaggiSimulazione) {
		
		this.grafo = grafo ;
		this.corse = corse ;
		this.passaggiSimulazione = passaggiSimulazione ;
		
		clientiSoddisfatti = 0; 
		clientiParzialmenteSoddisfatti = 0;
		clientiInsoddisfatti = 0;
		clienti = 0;
		
	}

	public void run(){
			
			while (!coda.isEmpty()){
				Evento e = coda.poll() ;
				
				// se è nella stazione capolinea creo un nuovo treno 
			}
	}
	
}
