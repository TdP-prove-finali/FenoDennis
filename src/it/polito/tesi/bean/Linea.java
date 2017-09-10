package it.polito.tesi.bean;

public class Linea {

	private String agencyID;
	private int id; 
	private String description ;
	private String shortName; 
	private String longName; 
	private int type; 
	private String url; 
	private String color;
	private String textColor;
	
	public String getAgencyID() {
		return agencyID;
	}
	public void setAgencyID(String agencyID) {
		this.agencyID = agencyID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public Linea(String agencyID, int id, String description, String shortName, String longName, int type, String url,
			String color, String textColor) {
		super();
		this.agencyID = agencyID;
		this.id = id;
		this.description = description;
		this.shortName = shortName;
		this.longName = longName;
		this.type = type;
		this.url = url;
		this.color = color;
		this.textColor = textColor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Linea other = (Linea) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return  agencyID + ": linea " + shortName ;
	}
	
}
