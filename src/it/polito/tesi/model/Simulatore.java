package it.polito.tesi.model;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tesi.bean.Corsa;
import it.polito.tesi.bean.Passaggio;
import it.polito.tesi.model.Evento.EventType;

public class Simulatore {
		
//  parametri di simulazione
	 
	private int capienzaMezzo = 1700 ;	// numero di persone
	private int tempoInStazione = 30 ; // in secondi
	
	private int numeroPersoneFermata = 1 ;
	
	private double probabilitaPermanenzaLinea;
	private double probabilitaPermanenzaLineaFlusso;
	
//  modello del mondo
	
	Map<FermataSuLinea, Treno> treni ; 
	Map<FermataSuLinea, Integer> personeFermata ; 
	DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo ;
	Map<String, Corsa> corse ;
	List<PassaggioCorsa> passaggiSimulazione ;
	List<FermataSuLinea> fermateSuLinea;

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
			Map<String, Corsa> corse, List<PassaggioCorsa> passaggiSimulazione) {
		
		this.grafo = grafo ;
		this.corse = corse ;
		this.passaggiSimulazione = passaggiSimulazione ;
		
		this.coda = new PriorityQueue<Evento>();	
		
		clientiSoddisfatti = 0; 
		clientiParzialmenteSoddisfatti = 0;
		clientiInsoddisfatti = 0;
		
		treni = new HashMap<>();
		personeFermata = new HashMap<>();
		
		fermateSuLinea = new ArrayList<>() ;
		
		creaEventi();
		
	}

	private void creaEventi() {
		LocalTime min = LocalTime.MAX ;
		LocalTime max = LocalTime.MIN ;

		for(PassaggioCorsa p : passaggiSimulazione){
			
			fermateSuLinea.add(new FermataSuLinea(p.getFermata(), p.getC().getLinea())); 
			
			coda.add(new Evento(p.getOraArrivo(),p, EventType.SALITA_MEZZO)) ;
			
			if(p.getOraArrivo().isBefore(min)){
				min = p.getOraArrivo() ;
			}
			if(p.getOraArrivo().isAfter(max)){
				max = p.getOraArrivo() ;
			}
			
		}
		
		for (int i = min.toSecondOfDay() ; i <= max.toSecondOfDay() ; i++){
			coda.add(new Evento(LocalTime.ofSecondOfDay(i), null , EventType.ARRIVO_FERMATA)) ;
		}
		
		for(FermataSuLinea fsl : fermateSuLinea){
			personeFermata.put(fsl, 0) ;
		}

	}

	public void run(){
			
			while (!coda.isEmpty()){
				Evento e = coda.poll() ;
//				System.out.println(e);
				
				switch(e.getTipo()){
				
				case SALITA_MEZZO :
					
					// se è nella stazione capolinea creo un nuovo treno
					PassaggioCorsa pc = e.getPassaggio() ;
					FermataSuLinea fsl = new FermataSuLinea(pc.getFermata(), pc.getC().getLinea()) ;
					
					if(treni.get(fsl)==null){
						treni.put(fsl, new Treno(0, capienzaMezzo)) ;
					}
					
					int cap = treni.get(fsl).getCapienza() ;
					int now = treni.get(fsl).getPasseggeriPresenti() ; 
					
					int pf = personeFermata.get(fsl) ; // persone attualmente alla fermata
					
					if(pf <= (cap-now)){
						treni.get(fsl).setPasseggeriPresenti(now+pf);
						personeFermata.remove(fsl) ;
						personeFermata.put(fsl, 0) ;
						clientiSoddisfatti += pf ;
					}
					else{
						int fuori = pf - (cap-now) ; 
						treni.get(fsl).setPasseggeriPresenti(cap);
						personeFermata.remove(fsl) ;
						personeFermata.put(fsl, fuori) ;
						clientiSoddisfatti+=(cap-now) ;
						clientiParzialmenteSoddisfatti+=fuori ;
					}
					
				break ;
					
				case ARRIVO_FERMATA :
					
					int rand = (int) Math.random() * fermateSuLinea.size() ;
					int k = personeFermata.get(fermateSuLinea.get(rand)) ;
					k += numeroPersoneFermata ;
					personeFermata.remove(fermateSuLinea.get(rand)) ;
					personeFermata.put(fermateSuLinea.get(rand), k) ;
						
				break;
					
				default:break;
				}				
				
			}
	}
	
	public int getClientiSoddisfatti() {
		return clientiSoddisfatti;
	}

	public void setClientiSoddisfatti(int clientiSoddisfatti) {
		this.clientiSoddisfatti = clientiSoddisfatti;
	}

	public int getClientiParzialmenteSoddisfatti() {
		return clientiParzialmenteSoddisfatti;
	}

	public void setClientiParzialmenteSoddisfatti(int clientiParzialmenteSoddisfatti) {
		this.clientiParzialmenteSoddisfatti = clientiParzialmenteSoddisfatti;
	}
	
}
