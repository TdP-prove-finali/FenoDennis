package it.polito.tesi.dao;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class DBConnectCP { // DB connect con connection pooling

	private static String jdbcURL = "jdbc:mysql://localhost/GTFS_sfm?user=root";
	private static DataSource ds;

	public static Connection getConnectionCP() {

		if (ds == null) {
			try {
				ds = DataSources.pooledDataSource(
						DataSources.unpooledDataSource(jdbcURL));
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		try {
			Connection c = ds.getConnection();
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
