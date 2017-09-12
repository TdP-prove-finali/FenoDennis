package it.polito.tesi.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

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
	
	WeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo ;
	
	public Model() {

		// caricamento dati in memoria
		
		GtfsDao dao = new GtfsDao();
		this.agenzie = dao.getAllAgencies(); // tutte le agenzie
		this.linee = dao.getAllRoutes();
		this.fermate = dao.getAllStops();	  // fermate
		this.servizi = dao.getAllServices();  // mi dice quando passa una linea
		this.corse = dao.getAllTrips(linee, servizi);		
		dao.loadPassaggi(corse, fermate); 
		
	} 
	
	public void creaGrafo(LocalDateTime dataOra){
		
		grafo = new DefaultDirectedWeightedGraph<FermataSuLinea,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		for (Corsa c : this.corse.values()){
				
			List<Passaggio> passaggi = c.getPassaggi() ;
			
			for(Passaggio p : passaggi){
				FermataSuLinea fsl = new FermataSuLinea(p.getFermata(), c.getLinea()) ;
				if(!grafo.containsVertex(fsl)) grafo.addVertex(fsl);
			}
			
			for(int i = 0 ; i < passaggi.size()-1 ; i++){
				
				FermataSuLinea fslP = new FermataSuLinea(passaggi.get(i).getFermata(), c.getLinea()) ;
				FermataSuLinea fslA = new FermataSuLinea(passaggi.get(i+1).getFermata(), c.getLinea()) ;
				
				if (fslP != null && fslA != null) {
					// Aggiungo un arco pesato tra le due fermate
					DefaultWeightedEdge link = grafo.addEdge(fslP, fslA);
					if (link != null) {
						grafo.setEdgeWeight(link, 3);
					}
				}
				
			}		
			
		}
		System.out.println(grafo);

	}
	
}
