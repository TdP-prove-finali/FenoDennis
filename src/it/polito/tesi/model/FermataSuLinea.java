package it.polito.tesi.model;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tesi.bean.Fermata;
import it.polito.tesi.bean.Linea;

public class FermataSuLinea extends Fermata{
	
	public FermataSuLinea(String agencyId, String id, int code, String name, String description, String direction,
			LatLng position, int zoneId, String url, int locationType) {
		super(agencyId, id, code, name, description, direction, position, zoneId, url, locationType);
	}

	private Linea linea ; 
	
	public FermataSuLinea(Fermata f, Linea l) {
		super(f.getAgencyId(), f.getId(), f.getCode(), f.getName(), f.getDescription(), 
				f.getDirection(), f.getPosition(), f.getZoneId(), f.getUrl(), f.getLocationType());
		this.setLinea(l); 
	}

	public Linea getLinea() {
		return linea;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((linea == null) ? 0 : linea.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FermataSuLinea other = (FermataSuLinea) obj;
		if (linea == null) {
			if (other.linea != null)
				return false;
		} else if (!linea.equals(other.linea))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString() +" linea " +  linea ;
	}
	
}
