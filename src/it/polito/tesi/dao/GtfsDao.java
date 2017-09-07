package it.polito.tesi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import it.polito.tesi.model.Linea;

public class GtfsDao {

	public Map<Integer,Linea> getAllRivers() {

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
	
}
