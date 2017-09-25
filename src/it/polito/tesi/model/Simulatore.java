package it.polito.tesi.model;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tesi.bean.Fermata;
import it.polito.tesi.bean.Linea;
import it.polito.tesi.model.Evento.EventType;

public class Simulatore {
		
//  parametri di simulazione
	 
	private int capienzaMezzo ;	// numero di persone
	private int intervalloTempoArrivo ; // in secondi
	private int numeroPersoneFermata ;
	
	private double probabilitaPermanenzaLinea ;
	private double probabilitaPermanenzaLineaFlusso ;
	private double probabilitaCambioLinea ; // la probabilità che venga cambiata la linea piuttosto che arrivo a destinazione, in una fermata di intercambio
	private double coefficienteFlusso ;
//  modello del mondo
	
	private Map<FermataSuLinea, Treno> treni ;
	
	private Map<FermataSuLinea, Integer> personeFermata ; 
	private Map<FermataSuLinea, Integer> personeParzIns ; 
	private DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo ;
	private Map<String, Linea> linee ;
	private List<PassaggioCorsa> passaggiSimulazione ;
	private List<FermataSuLinea> fermateSuLinea;
	private Map<Fermata, Set<FermataSuLinea>> fermateSullaLinea ;

	private Set<FermataSuLinea> flusso ; 
	
//	misure in uscita
	
	private int clientiSoddisfatti ; 
	private int clientiParzialmenteSoddisfatti ; // se vedono passare un treno aspettano quello dopo 
	private int clientiInsoddisfatti ;			 // al secondo treno vanno via
	
	private Map<Fermata, Integer> soddFermata ; 
	private Map<Linea, Integer> soddLinea ; 

	
	private PriorityQueue<Evento> coda ;
	
	public Simulatore() {
		this.coda = new PriorityQueue<Evento>();	
	}
	
	public Simulatore(DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo,
			Map<String, Linea> linee, List<PassaggioCorsa> passaggiSimulazione, 
			Map<Fermata, Set<FermataSuLinea>> fermateSullaLinea, List<DefaultWeightedEdge> pathEdgeList, 
			int capienza, int nPersFermata, int intervallo, 
			double probLinea, double probFlusso, double probCambio, double coefficiente) {
		
		this.capienzaMezzo = capienza ;
		this.numeroPersoneFermata = nPersFermata ; 
		this.intervalloTempoArrivo = intervallo ; 
		this.probabilitaPermanenzaLinea = probLinea ;
		this.probabilitaPermanenzaLineaFlusso = probFlusso ;
		this.probabilitaCambioLinea = probCambio ;
		this.coefficienteFlusso = coefficiente ;
		
		this.grafo = grafo ;
		this.linee = linee ;
		this.passaggiSimulazione = passaggiSimulazione ;
		this.fermateSullaLinea = fermateSullaLinea ;
		
		this.coda = new PriorityQueue<Evento>();	
		
		clientiSoddisfatti = 0; 
		clientiParzialmenteSoddisfatti = 0;
		clientiInsoddisfatti = 0;
		
		treni = new HashMap<>();
		personeFermata = new HashMap<>();
		personeParzIns = new HashMap<>();
		
		// per i grafici
		soddFermata = new HashMap<>();; 
		soddLinea = new HashMap<>();; 
		
		fermateSuLinea = new ArrayList<>() ;
		
		if(pathEdgeList != null){
			
			flusso = new HashSet<FermataSuLinea>() ;
			for(DefaultWeightedEdge dwe : pathEdgeList){
				if(flusso.contains(grafo.getEdgeSource(dwe)))
					flusso.add(grafo.getEdgeSource(dwe));
				if(flusso.contains(grafo.getEdgeTarget(dwe)))
					flusso.add(grafo.getEdgeTarget(dwe));
			}
			
		}
		
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
		
		for(Linea l : linee.values()){
			soddLinea.put(l, 0) ;
		}
		for(Fermata f : fermateSullaLinea.keySet()){
			soddFermata.put(f, 0) ;
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
						
						Fermata f = new Fermata(fsl.getId() ) ;
						
						// sulla fermata
						soddFermata.replace(f, soddFermata.get(f) +ppi ); 
						// sulla linea
						soddLinea.replace(fsl.getLinea(), soddLinea.get(fsl.getLinea())+ppi) ;
						
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
					
					List<FermataSuLinea> vicini = Graphs.successorListOf(this.grafo, fsl) ; 
					// se fanno parte della stessa linea.. teoricamente dovrebbe essercene solo 1, però faccio il controllo
//					System.out.println(vicini.size());
					
					for(FermataSuLinea fslSuccessiva : vicini){
						if(fslSuccessiva.getLinea().equals(fsl)){
							treni.put(fslSuccessiva, new Treno(treni.get(fsl).getPasseggeriPresenti(), capienzaMezzo)) ;
							treni.remove(fsl) ;
						}
					}
					
					
				break ;
				
				case DISCESA_MEZZO :
					
					PassaggioCorsa pc2 = e.getPassaggio() ;
					FermataSuLinea fsl2 = new FermataSuLinea(pc2.getFermata(), pc2.getC().getLinea()) ;
					
					if(treni.get(fsl2)==null){
						treni.put(fsl2, new Treno(0, capienzaMezzo)) ;
					}
					
//					int cap2 = treni.get(fsl2).getCapienza() ;
					int now2 = treni.get(fsl2).getPasseggeriPresenti() ; 
					
					int dentro = 0 ;
					if(flusso!=null && flusso.contains(fsl2)){
						 dentro = (int) Math.round( now2 * this.probabilitaPermanenzaLineaFlusso ) ;
					}else{
						dentro = (int) Math.round( now2 * this.probabilitaPermanenzaLinea ) ;
					}

//					System.out.println(fermateSullaLinea.get(pc2.getFermata()));

					Set<FermataSuLinea> corrispondenze = fermateSullaLinea.get(pc2.getFermata()) ; 
					treni.get(fsl2).setPasseggeriPresenti(dentro) ; // i passeggeri scendono dal mezzo
					
					if(corrispondenze.size()>1){ // fermata di scambio
						corrispondenze.remove(fsl2) ;
						int smista = (int) Math.round( (now2-dentro) * this.probabilitaCambioLinea );
						
						for(FermataSuLinea f : corrispondenze){
							if(personeFermata.get(f)!=null) 
								personeFermata.replace(f, (int) (personeFermata.get(f)-(smista/corrispondenze.size())) ) ;
						}
					}
					
				break ;
				
				case ARRIVO_FERMATA :
					
					int rand = r.nextInt( fermateSuLinea.size() );
//					System.out.println(rand);
					FermataSuLinea fsl3 = fermateSuLinea.get(rand) ;
					int k = personeFermata.get(fsl3) ;
					
					if(flusso!=null && flusso.contains(fsl3)){
						k+= (int)Math.round(numeroPersoneFermata*coefficienteFlusso) ;
					}else{
						k += numeroPersoneFermata ;
					}
					
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

	public int getClientiParzialmenteSoddisfatti() {
		return clientiParzialmenteSoddisfatti;
	}

	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}
	
	public Map<Fermata, Integer> getSoddFermata() {
		return soddFermata;
	}

	public Map<Linea, Integer> getSoddLinea() {
		return soddLinea;
	}
	
}
