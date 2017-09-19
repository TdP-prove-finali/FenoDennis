package it.polito.tesi.model;

public class Treno {

	private int passeggeriPresenti ;
	private int capienza ;
	private FermataSuLinea posizione;
	
	public Treno(int passeggeriPresenti, int capienza) {
		this.passeggeriPresenti = passeggeriPresenti;
		this.capienza = capienza;
	}

	public int getPasseggeriPresenti() {
		return passeggeriPresenti;
	}

	public void setPasseggeriPresenti(int passeggeriPresenti) {
		this.passeggeriPresenti = passeggeriPresenti;
	}

	public int getCapienza() {
		return capienza;
	}

	public void setCapienza(int capienza) {
		this.capienza = capienza;
	}

	public FermataSuLinea getPosizione() {
		return posizione;
	}

	public void setPosizione(FermataSuLinea posizione) {
		this.posizione = posizione;
	}
	
	
	
	
}
