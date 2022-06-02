package com.simplilearn.EMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Department {
	private static final Logger LOGGER = LoggerFactory.getLogger(Department.class);

	private Connection connection = null;

	public String addDepartment(String depName) {
		try {
			// add department.
			connection = JDBCConnector.dbConnector();

			String query = "INSERT INTO DEPARTMENT (department_id,department_nm) " + "select * from ("
					+ "Select case when  isnull(max(department_id+1)) then 1 "
					+ "else max(department_id+1) end as department_id, ? from DEPARTMENT as d) as temp";

			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, depName);
			//System.out.println(pst.toString());
			pst.execute();
			connection.close();
			return "Saved";

		} catch (Exception e) {
			// e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage());
		}
		return "Error";
	}

	public void printDepartment() {
		try {
			String query = "SELECT * from DEPARTMENT";
			connection = JDBCConnector.dbConnector();
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			System.out.printf("%5s %10s", "DEPARTMENT ID", "DEPARTMENT NAME");
			System.out.println();
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.format("%7s %14s ", rs.getInt("department_id"), rs.getString("department_nm"));
				System.out.println();
			}
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			connection.close();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}
	}
}
