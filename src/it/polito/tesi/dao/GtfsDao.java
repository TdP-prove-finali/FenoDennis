package it.polito.tesi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tesi.bean.Agenzia;
import it.polito.tesi.bean.Fermata;
import it.polito.tesi.bean.Linea;

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
	public Map<Integer,Linea> getAllRoutes() {

		final String sql = "SELECT * FROM gtfs_routes ORDER BY id ASC";
		
		Map<Integer,Linea> linee = new HashMap<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Linea l = new Linea(rs.getString("agencyId"), rs.getInt("id"), rs.getString("description"), 
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
	public Map<Integer,Fermata> getAllStops() {

		final String sql = "SELECT * FROM gtfs_stops ORDER BY id ASC";
		
		Map<Integer,Fermata> stops = new HashMap<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Fermata f = new Fermata(rs.getString("agencyId"), rs.getInt("id"), rs.getInt("code"), 
						rs.getString("name"), rs.getString("description"), 
						 rs.getString("direction"), new LatLng(rs.getDouble("lat"),rs.getDouble("lat")), 
						 rs.getInt("zoneId"), rs.getString("url"),rs.getInt("locationType"));
				stops.put((int) f.getId(), f) ;
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return stops ;
	}
	
	
	
}
