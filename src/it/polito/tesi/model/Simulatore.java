package it.polito.tesi.model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tesi.bean.Corsa;
import it.polito.tesi.bean.Fermata;
import it.polito.tesi.model.Evento.EventType;

public class Simulatore {
		
//  parametri di simulazione
	 
	private int capienzaMezzo = 190 ;	// numero di persone
	private int intervalloTempoArrivo = 1 ; // in secondi
	private int numeroPersoneFermata = 10 ;
	
	private double probabilitaPermanenzaLinea = 0.5 ;
	private double probabilitaPermanenzaLineaFlusso = 0.8 ;
	private double probabilitaCambioLinea = 0.7 ; // la probabilità che venga cambiata la linea piuttosto che arrivo a destinazione, in una fermata di intercambio
	
//  modello del mondo
	
	Map<FermataSuLinea, Treno> treni ; 
	Map<FermataSuLinea, Integer> personeFermata ; 
	Map<FermataSuLinea, Integer> personeParzIns ; 
	DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo ;
	Map<String, Corsa> corse ;
	List<PassaggioCorsa> passaggiSimulazione ;
	List<FermataSuLinea> fermateSuLinea;
	Map<Fermata, Set<FermataSuLinea>> fermateSullaLinea ;

//	misure in uscita
	
	int clientiSoddisfatti ; 
	int clientiParzialmenteSoddisfatti ; // se vedono passare un treno aspettano quello dopo 
	int clientiInsoddisfatti ;			 // al secondo treno vanno via
	int clienti ;
	
	private PriorityQueue<Evento> coda ;
	
	public Simulatore() {
		this.coda = new PriorityQueue<Evento>();	
	}
	
	public Simulatore(DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo,
			Map<String, Corsa> corse, List<PassaggioCorsa> passaggiSimulazione, Map<Fermata, Set<FermataSuLinea>> fermateSulaLinea) {
		
		this.grafo = grafo ;
		this.corse = corse ;
		this.passaggiSimulazione = passaggiSimulazione ;
		this.fermateSullaLinea = fermateSullaLinea ;
		
		this.coda = new PriorityQueue<Evento>();	
		
		clientiSoddisfatti = 0; 
		clientiParzialmenteSoddisfatti = 0;
		clientiInsoddisfatti = 0;
		
		treni = new HashMap<>();
		personeFermata = new HashMap<>();
		personeParzIns = new HashMap<>();
		
		fermateSuLinea = new ArrayList<>() ;
		
		creaEventi();
		
	}

	private void creaEventi() {
		LocalTime min = LocalTime.MAX ;
		LocalTime max = LocalTime.MIN ;

		for(PassaggioCorsa p : passaggiSimulazione){
			
			fermateSuLinea.add(new FermataSuLinea(p.getFermata(), p.getC().getLinea())); 
			
			coda.add(new Evento(p.getOraArrivo(),p, EventType.DISCESA_MEZZO)) ;
			coda.add(new Evento(LocalTime.ofSecondOfDay( p.getOraPartenza().toSecondOfDay()+5) ,p, EventType.SALITA_MEZZO)) ;
			
			if(p.getOraArrivo().isBefore(min)){
				min = p.getOraArrivo() ;
			}
			if(p.getOraArrivo().isAfter(max)){
				max = p.getOraArrivo() ;
			}
			
		}
		
		for (int i = min.toSecondOfDay() ; i <= max.toSecondOfDay() ; i+= intervalloTempoArrivo){
			coda.add(new Evento(LocalTime.ofSecondOfDay(i), null , EventType.ARRIVO_FERMATA)) ;
		}
		
		for(FermataSuLinea fsl : fermateSuLinea){
			personeFermata.put(fsl, 0) ;
			personeParzIns.put(fsl, 0) ;
		}

	}

	public void run(){
		
		Random r = new Random () ;
		
			while (!coda.isEmpty()){
				Evento e = coda.poll() ;
//				System.out.println(e);
				
				switch(e.getTipo()){
				
				case SALITA_MEZZO :
					
					// se è nella stazione capolinea creo un nuovo treno
					PassaggioCorsa pc = e.getPassaggio() ;
					FermataSuLinea fsl = new FermataSuLinea(pc.getFermata(), pc.getC().getLinea()) ; // fermata di riferimento
					
					if(treni.get(fsl)==null){
						treni.put(fsl, new Treno(0, capienzaMezzo)) ;
					}
					
					int cap = treni.get(fsl).getCapienza() ;
					int now = treni.get(fsl).getPasseggeriPresenti() ; 
					
					int ppi = personeParzIns.get(fsl) ;
					
					if(ppi>0){ // c'è già qualcuno di insoddisfatto
						// faccio avere loro la precedenza sul treno
						
						if(ppi <= (cap-now)){
							treni.get(fsl).setPasseggeriPresenti(now+ppi);
							personeParzIns.replace(fsl, 0) ; 
							clientiParzialmenteSoddisfatti += ppi ;
						}
						else{
							int fuori = ppi - (cap-now) ; 
							treni.get(fsl).setPasseggeriPresenti(cap);
							personeParzIns.replace(fsl, 0) ; // se ne vanno via
							clientiParzialmenteSoddisfatti+= (cap-now) ;
							clientiInsoddisfatti+=fuori ;
						}
						
					}
					
					int pf = personeFermata.get(fsl) ; // persone attualmente alla fermata ancora soddisfatte
					
					if(pf <= (cap-now)){
						treni.get(fsl).setPasseggeriPresenti(now+pf);
						personeFermata.replace(fsl, 0) ; 
						clientiSoddisfatti += pf ;
					}
					else{
						int fuori = pf - (cap-now) ; 
						treni.get(fsl).setPasseggeriPresenti(cap);
						personeFermata.replace(fsl, 0) ;
						personeParzIns.replace(fsl, fuori) ;
						clientiSoddisfatti+=(cap-now) ;
//						clientiParzialmenteSoddisfatti+=fuori ; li gestisco dopo 
					}
					
					// devo gestire lo spostamento del treno
					
					
				break ;
				
				case DISCESA_MEZZO :
					
					PassaggioCorsa pc2 = e.getPassaggio() ;
					FermataSuLinea fsl2 = new FermataSuLinea(pc2.getFermata(), pc2.getC().getLinea()) ;
					
					if(treni.get(fsl2)==null){
						treni.put(fsl2, new Treno(0, capienzaMezzo)) ;
					}
					
//					int cap2 = treni.get(fsl2).getCapienza() ;
					int now2 = treni.get(fsl2).getPasseggeriPresenti() ; 
					
					int dentro = (int) Math.round( now2 * this.probabilitaCambioLinea ) ;
					
					Set<FermataSuLinea> corrispondenze = fermateSullaLinea.get(fsl2) ; 
					treni.get(fsl2).setPasseggeriPresenti(dentro) ; // i passeggeri scendono dal mezzo

					if(corrispondenze.size()>1){ // fermata di scambio
						corrispondenze.remove(fsl2) ;
						
						for(FermataSuLinea f : corrispondenze){
							
						}
						
					}else{ // i passeggeri escono dalla rete di trasporti
					}
					
					
				break ;
				
				case ARRIVO_FERMATA :
					
					int rand = r.nextInt( fermateSuLinea.size() );
					System.out.println(rand);
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

	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}

	public void setClientiInsoddisfatti(int clientiInsoddisfatti) {
		this.clientiInsoddisfatti = clientiInsoddisfatti;
	}
	
}
