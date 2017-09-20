package it.polito.tesi.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tesi.bean.Agenzia;
import it.polito.tesi.bean.Corsa;
import it.polito.tesi.bean.Fermata;
import it.polito.tesi.bean.Linea;
import it.polito.tesi.bean.Passaggio;
import it.polito.tesi.bean.Servizio;
import it.polito.tesi.dao.GtfsDao;

public class Model {

	Map<String, Agenzia> agenzie ;
	Map<String, Linea> linee ; 	
	Map<String, Fermata> fermate ;
	Map<String, Servizio> servizi ;
	Map<String, Corsa> corse ;
	
	DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo ;
	
	Map<Fermata, Set<FermataSuLinea>> fermateSuLinea ;
	
	private List<DefaultWeightedEdge> pathEdgeList = null;
	private double pathTempoTotale = 0;
	
	List<PassaggioCorsa> passaggiSimulazione ; 
	
	public Model() {

		// caricamento dati in memoria
		
		GtfsDao dao = new GtfsDao();
		this.agenzie = dao.getAllAgencies(); // tutte le agenzie
		this.linee = dao.getAllRoutes();
		this.fermate = dao.getAllStops();	  // fermate
		this.servizi = dao.getAllServices();  // mi dice quando passa una linea
		this.corse = dao.getAllTrips(linee, servizi);		
		dao.loadPassaggi(corse, fermate); 
		
		System.out.println("fermate "+fermate.values().size());
		System.out.println("agenzie "+agenzie.values().size());
		System.out.println("servizi "+servizi.values().size());
		System.out.println("linee "+linee.values().size());
		System.out.println("corse "+corse.values().size());
		
		int j = 0 ;
		for(Corsa c : corse.values()){
			j+=c.getPassaggi().size(); 
		}
		System.out.println("passaggi "+j);

		this.fermateSuLinea = new HashMap<>();
		for(Fermata f : this.fermate.values()) 
			fermateSuLinea.put(f, new HashSet<FermataSuLinea>());
	} 
	
	public void creaGrafo(LocalDateTime dataOra, int tempoSimulazione){
		
		grafo = new DefaultDirectedWeightedGraph<FermataSuLinea,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		passaggiSimulazione = new ArrayList<>(); 

		for (Corsa c : this.corse.values()){
//			System.out.println(c.getServizio());
			
			int dayOfWeek = dataOra.getDayOfWeek().getValue()-1;
			if(c.getServizio().getSettimana()[dayOfWeek]==1){ // aggiungo l'arco solo se nel giorno indicato
			
					LocalTime lt1 = dataOra.toLocalTime() ;
				
					List<Passaggio> passaggi = c.getPassaggi() ;
					
					// aggiunta dei vertici
					for(Passaggio p : passaggi){
//						System.out.println(p.getOraPartenza().toSecondOfDay() + " / " + lt1.toSecondOfDay() + " / " + 
//								(p.getOraPartenza().toSecondOfDay()-lt1.toSecondOfDay())+ " / " + (tempoSimulazione*3600) ) ;
						int delta = p.getOraArrivo().toSecondOfDay()-lt1.toSecondOfDay() ;

						if(delta>=0 && delta <= (3600*tempoSimulazione)) passaggiSimulazione.add(new PassaggioCorsa(p,c)) ;
						
							FermataSuLinea fsl = new FermataSuLinea(p.getFermata(), c.getLinea()) ;
							this.fermateSuLinea.get(p.getFermata()).add(fsl);
							if(!grafo.containsVertex(fsl)){
								grafo.addVertex(fsl);
			//					System.out.println(fsl.toString());
							}
						
					}
					
					// aggiunta dei nodi
					for(int i = 0 ; i < passaggi.size()-1 ; i++){
						int delta = passaggi.get(i).getOraArrivo().toSecondOfDay()-lt1.toSecondOfDay() ;
						if ( delta <= tempoSimulazione*3600 && delta >= 0)
						{
							FermataSuLinea fslP = new FermataSuLinea(passaggi.get(i).getFermata(), c.getLinea()) ;
							FermataSuLinea fslA = new FermataSuLinea(passaggi.get(i+1).getFermata(), c.getLinea()) ;
							
							if (fslP != null && fslA != null && !fslP.equals(fslA)) {
								
								double velocita = 30;
								double distanza = LatLngTool.distance(fslP.getPosition(), fslA.getPosition(), LengthUnit.KILOMETER);
								double tempo = (distanza / velocita) * 60 * 60;
								System.out.println(fslP + " / " + fslA);
								// Aggiungo un arco pesato tra le due fermate
								DefaultWeightedEdge link = grafo.addEdge(fslP, fslA);
								if (link != null) {
									grafo.setEdgeWeight(link, tempo);
								}
							}
						}
					}
					
			}//end if day
		}
		System.out.println("Grafo creato: " + grafo.vertexSet().size() + " nodi, " + grafo.edgeSet().size() + " archi");

		// aggiungo i collegamenti da una parte all'altra della stazione
		for (Set<FermataSuLinea> fsl : fermateSuLinea.values()){
			
			for(FermataSuLinea fslA : fsl){
				for(FermataSuLinea fslB : fsl){
					
					if (fslA != null && fslB != null && !fslA.equals(fslB)) {
						
						DefaultWeightedEdge link = grafo.addEdge(fslA, fslB);
						if (link != null) {
							grafo.setEdgeWeight(link, 60 * 5 ); // 5 minuti per andare da una banchina all'altra
//							System.out.println(link.toString());
						}
					}
				}
			}
			
		}
		System.out.println("Grafo creato: " + grafo.vertexSet().size() + " nodi, " + grafo.edgeSet().size() + " archi");
		ConnectivityInspector<FermataSuLinea,DefaultWeightedEdge> ci = new ConnectivityInspector<FermataSuLinea,DefaultWeightedEdge>(grafo);
		System.out.println(ci.isGraphConnected());

	}
	
	public void calcolaPercorso(Fermata partenza, Fermata arrivo){

		DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge> dijkstra;

		// Usati per salvare i valori temporanei
		double pathTempoTotaleTemp;

		// Usati per salvare i valori migliori
		List<DefaultWeightedEdge> bestPathEdgeList = null;
		double bestPathTempoTotale = Double.MAX_VALUE;

		for (FermataSuLinea fslP : fermateSuLinea.get(partenza)) {
			for (FermataSuLinea fslA : fermateSuLinea.get(arrivo)) {
				dijkstra = new DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge>(grafo, fslP, fslA);

				pathTempoTotaleTemp = dijkstra.getPathLength();

				if (pathTempoTotaleTemp < bestPathTempoTotale) {
					bestPathTempoTotale = pathTempoTotaleTemp;
					bestPathEdgeList = dijkstra.getPathEdgeList();
				}
			}
		}

		pathEdgeList = bestPathEdgeList;
		pathTempoTotale = bestPathTempoTotale;

		if (pathEdgeList == null || pathEdgeList.size()==0)
			System.out.println("Non è stato creato un percorso.");

	}

	public String getPercorsoEdgeList() {
		StringBuilder risultato = new StringBuilder();

		if (pathEdgeList == null || pathEdgeList.size()==0)
			;//risultato.append("Non è stato creato un percorso.");
		else{
			
			risultato.append("Percorso:\n\n");
	
			Linea lineaTemp = grafo.getEdgeSource(pathEdgeList.get(0)).getLinea();
			risultato.append("Prendo Linea: " + lineaTemp.getShortName() + "\n[");
	
			for (DefaultWeightedEdge edge : pathEdgeList) {
				risultato.append(grafo.getEdgeTarget(edge).getName());
	
				if (!grafo.getEdgeTarget(edge).getLinea().equals(lineaTemp)) {
					risultato.append("]\n\nCambio su Linea: " + grafo.getEdgeTarget(edge).getLinea() + "\n[");
					lineaTemp = grafo.getEdgeTarget(edge).getLinea();
	
				} else {
					risultato.append(", ");
				}
			}
			risultato.setLength(risultato.length() - 2);
			risultato.append("]");
		}
			return risultato.toString();
	}

	public Map<String, Fermata> getFermate() {
		if(this.fermate==null){
			GtfsDao dao = new GtfsDao();
			this.fermate=dao.getAllStops();
			}
		return fermate;
	}

	public double getPercorsoTempoTotale() {

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		return pathTempoTotale;
	}

	public Map<String, Linea> getLinee() {
		if(this.linee==null){
			GtfsDao dao = new GtfsDao();
			this.linee=dao.getAllRoutes();
			}
		return this.linee;
	}

	public Map<String, Agenzia> getAgenzie() {
		if(this.agenzie==null){
			GtfsDao dao = new GtfsDao();
			this.agenzie=dao.getAllAgencies();
			}
		return this.agenzie;
	}
	
	public void Simula(){
		Simulatore sim = new Simulatore(grafo, corse, passaggiSimulazione) ;
		sim.run();
		System.out.println("sod " + sim.getClientiSoddisfatti()) ;
		System.out.println("parz " + sim.getClientiParzialmenteSoddisfatti()) ;
		
	}
	
}
