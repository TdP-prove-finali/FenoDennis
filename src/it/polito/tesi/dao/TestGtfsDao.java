package it.polito.tesi.dao;

import java.util.Map;

import it.polito.tesi.bean.Agenzia;
import it.polito.tesi.bean.Fermata;
import it.polito.tesi.bean.Linea;

public class TestGtfsDao {

	public static void main(String[] args) {
		
		GtfsDao dao = new GtfsDao(); 
		
		Map<String, Agenzia> agenzie = dao.getAllAgencies(); 
		
		for(Agenzia a : agenzie.values()) System.out.println(a.toString());
		
		Map<Integer, Linea> linee = dao.getAllRoutes(); 
		
		for(Linea l : linee.values()) System.out.println(l.toString());
		
		Map<Integer, Fermata> fermate = dao.getAllStops(); 
		
		for(Fermata f : fermate.values()) System.out.println(f.toString());
		
		
	}

}
