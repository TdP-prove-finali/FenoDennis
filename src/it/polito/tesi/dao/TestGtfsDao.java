package it.polito.tesi.dao;

import java.util.Map;

import it.polito.tesi.model.Linea;

public class TestGtfsDao {

	public static void main(String[] args) {
		
		GtfsDao dao = new GtfsDao(); 
		
		Map<Integer, Linea> mappa = dao.getAllRivers(); 
		
		for(Linea l : mappa.values()) System.out.println(l.toString());
	}

}
