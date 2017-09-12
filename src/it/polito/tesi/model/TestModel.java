package it.polito.tesi.model;

import java.time.LocalDateTime;

public class TestModel {

	public static void main(String[] args) {

		long t1 = System.nanoTime();
		Model m = new Model() ;
		long t2 = System.nanoTime();
		LocalDateTime ldt = LocalDateTime.now() ;
		m.creaGrafo(ldt);
		System.out.println("ok: "+(t2-t1));
	}

}
