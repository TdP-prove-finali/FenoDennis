package it.polito.tesi.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.polito.tesi.bean.Fermata;

public class TestModel {

	public static void main(String[] args) {

		long t1 = System.nanoTime();
		Model m = new Model() ;
		long t2 = System.nanoTime();
		LocalDateTime ldt = LocalDateTime.now() ;
		m.creaGrafo(ldt, 1);
		
		List<Fermata> f = new ArrayList<>() ;
		f.addAll(m.getFermate().values()) ;
		System.out.println("size"+ f.size());
		System.out.println("ok: "+(t2-t1));
		m.Simula();
		
		
		
/*
		for(Fermata a : f){
			for(Fermata b : f){
				m.calcolaPercorso(a, b);
				//System.out.println(a.toString() + " - " + m.getPercorsoEdgeList() + " - " + b.toString());

			}
		}
	*/	
	}

}
