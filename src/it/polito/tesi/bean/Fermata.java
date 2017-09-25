package it.polito.tesi.bean;

import com.javadocmd.simplelatlng.LatLng;

public class Fermata implements Comparable<Fermata>{

	private String agencyId ;
	private String id ;
	private int code ;
	private String name ;
	private String description ;
	private String direction ;
	private LatLng position ;
	private int zoneId ;
	private String url ;
	private int locationType ;

	public Fermata(String agencyId, String id, int code, String name, String description, String direction,
			LatLng position, int zoneId, String url, int locationType) {
		this.agencyId = agencyId;
		this.id = id;
		this.code = code;
		this.name = name;
		this.description = description;
		this.direction = direction;
		this.position = position;
		this.zoneId = zoneId;
		this.url = url;
		this.locationType = locationType;
	}

	public Fermata(String id2) {
		this.id = id2;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getLocationType() {
		return locationType;
	}

	public void setLocationType(int locationType) {
		this.locationType = locationType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fermata other = (Fermata) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Fermata " + id + " - " + name ;
	}

	@Override
	public int compareTo(Fermata o) {
		return this.name.compareTo(o.name);
	}

}
