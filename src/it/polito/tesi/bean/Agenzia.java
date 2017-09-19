package it.polito.tesi.bean;

public class Agenzia implements Comparable<Agenzia>{

	  private String id;
	  private String fareUrl;
	  private String lang;
	  private String name;
	  private String phone;
	  private String timezone;
	  private String url;

	public Agenzia(String id, String fareUrl, String lang, String name, String phone, String timezone, String url) {
		super();
		this.id = id;
		this.fareUrl = fareUrl;
		this.lang = lang;
		this.name = name;
		this.phone = phone;
		this.timezone = timezone;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFareUrl() {
		return fareUrl;
	}

	public void setFareUrl(String fareUrl) {
		this.fareUrl = fareUrl;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		Agenzia other = (Agenzia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  id + "- " + name ;
	}

	@Override
	public int compareTo(Agenzia o) {
		return this.name.compareTo(o.name);
	}
	
	
	
}
