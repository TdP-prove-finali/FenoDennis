package it.polito.tesi.dao;

import java.util.Map;

import it.polito.tesi.bean.Agenzia;
import it.polito.tesi.bean.Corsa;
import it.polito.tesi.bean.Fermata;
import it.polito.tesi.bean.Linea;
import it.polito.tesi.bean.Servizio;

public class TestGtfsDao {

	public static void main(String[] args) {
		
		GtfsDao dao = new GtfsDao(); 
		
		Map<String, Agenzia> agenzie = dao.getAllAgencies(); 
		
//		for(Agenzia a : agenzie.values()) System.out.println(a.toString());
		
		Map<String, Linea> linee = dao.getAllRoutes(); 
		
//		for(Linea l : linee.values()) System.out.println(l.toString());
		
		Map<String, Fermata> fermate = dao.getAllStops(); 
		
//		for(Fermata f : fermate.values()) System.out.println(f.toString());
		
		Map<String, Servizio> servizi = dao.getAllServices(); 
		
//		for(Servizio s : servizi.values()) System.out.println(s.toString());
		
		Map<String, Corsa> corse = dao.getAllTrips(linee, servizi);

//		for(Corsa c: corse.values()) System.out.println(c.toString());
		
		dao.loadPassaggi(corse, fermate); 
		
		for(Corsa c: corse.values()) c.ordinaPassaggi() ;
		
	}

}
