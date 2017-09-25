package it.polito.tesi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tesi.bean.Agenzia;
import it.polito.tesi.bean.Corsa;
import it.polito.tesi.bean.Fermata;
import it.polito.tesi.bean.Linea;
import it.polito.tesi.bean.Passaggio;
import it.polito.tesi.bean.Servizio;

public class GtfsDao {

	/****
	 * Metodi per le agenzie.
	 ****/

	public Map<String, Agenzia> getAllAgencies() {
		String sql = "SELECT * FROM gtfs_agencies";

		Connection conn = DBConnect.getInstance().getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);

			Map<String, Agenzia> agenzie = new HashMap<>();

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Agenzia agency = new Agenzia(rs.getString("id"),rs.getString("fareUrl"),rs.getString("lang"),
						rs.getString("name"),rs.getString("phone"),rs.getString("timezone"),rs.getString("url"));
				agenzie.put(agency.getId(), agency);

			}

			st.close();
			conn.close();

			return agenzie;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error", e);
		}
	}

	public Agenzia getAgencyForId(String id) {
		String sql = "SELECT * FROM gtfs_agencies WHERE id=?";

		Connection conn = DBConnect.getInstance().getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, id);
			ResultSet rs = st.executeQuery();

			Agenzia agency = null;
			if (rs.next()) {

				agency = new Agenzia(rs.getString("id"),rs.getString("fareUrl"),rs.getString("lang"),
						rs.getString("name"),rs.getString("phone"),rs.getString("timezone"),rs.getString("url"));
			}
			st.close();
			conn.close();

			return agency;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error", e);
		}
	}
	/**
	 * metodi per le linee
	 */
	public Map<String,Linea> getAllRoutes() {

		final String sql = "SELECT * FROM gtfs_routes ORDER BY id ASC";
		
		Map<String,Linea> linee = new HashMap<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Linea l = new Linea(rs.getString("agencyId"), rs.getString("id"), rs.getString("description"), 
						rs.getString("shortName"), rs.getString("longName"),
						rs.getInt("type"), rs.getString("url"),rs.getString("color"),rs.getString("textColor"));
				linee.put(l.getId(), l) ;
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return linee ;
	}
	
	/**
	 * metodi per le linee
	 */
	public Map<String,Fermata> getAllStops() {

		final String sql = "SELECT * FROM gtfs_stops ORDER BY id ASC";
		
		Map<String,Fermata> stops = new HashMap<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Fermata f = new Fermata(rs.getString("agencyId"), rs.getString("id"), rs.getInt("code"), 
						rs.getString("name"), rs.getString("description"), 
						 rs.getString("direction"), new LatLng(rs.getDouble("lat"),rs.getDouble("lon")), 
						 rs.getInt("zoneId"), rs.getString("url"),rs.getInt("locationType"));
				stops.put(f.getId(), f) ;
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return stops ;
	}
	
	/**
	 * metodi per il calendario
	 */
	public Map<String, Servizio> getAllServices() {

		final String sql = "SELECT * FROM gtfs_calendars ORDER BY serviceId_id ASC";
		
		Map<String,Servizio> servizi = new HashMap<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				int[] settimana = new int[7];
				settimana[0]= rs.getInt("monday");
				settimana[1]= rs.getInt("tuesday");
				settimana[2]= rs.getInt("wednesday");
				settimana[3]= rs.getInt("thursday");
				settimana[4]= rs.getInt("friday");
				settimana[5]= rs.getInt("saturday");
				settimana[6]= rs.getInt("sunday");
				
				Servizio s = new Servizio(rs.getString("serviceId_id"), settimana);
				servizi.put(s.getServiceId(), s) ;
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return servizi ;
	}

	public Map<String, Corsa> getAllTrips(Map<String, Linea> linee, Map<String, Servizio> servizi) {
		final String sql = "SELECT * FROM gtfs_trips ORDER BY id ASC";
		
		Map<String,Corsa> corse = new HashMap<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Corsa c = new Corsa(
						linee.get(rs.getString("route_id")),
						servizi.get(rs.getString("serviceId_id")),
						rs.getString("id"),
						rs.getString("tripHeadsign")
				);
				
				corse.put(c.getTripId(), c) ;
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}
		return corse ;
	}

	public void loadPassaggi(Map<String, Corsa> corse, Map<String, Fermata> fermate) {
		final String sql = "SELECT * FROM gtfs_stop_times ORDER BY trip_id ASC";
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				long arr = rs.getLong("arrivalTime") ;
				long par = rs.getLong("departureTime") ;
				
				arr = arr > 86399 ? 86340 : arr ;
				par = par > 86399 ? 86340 : par ;
				
				LocalTime arrivo = LocalTime.ofSecondOfDay(arr);
				LocalTime partenza = LocalTime.ofSecondOfDay(par);
				
				Passaggio p = new Passaggio(
						fermate.get(rs.getString("stop_id")),
						arrivo,
						partenza,
						rs.getInt("stopSequence")
				);
//				System.out.println(rs.getString("trip_id") + p);
				corse.get(rs.getString("trip_id")).addPassaggio(p);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}
	}

}
