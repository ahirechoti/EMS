package com.simplilearn.EMS;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Statusreport {

	private static final Logger LOGGER = LoggerFactory.getLogger(Statusreport.class);

	private static Connection connection = null;

	public String addStatus(int compId, int empid, String comments, int dep) {
		try {
			Date createddate = new Date(new java.util.Date().getTime());
			String query = "INSERT INTO statusreport (complianceid, statusrptid, empid, comments, createDate, department_id)"
					+ "  SELECT * FROM ( SELECT ? as complianceid, CASE WHEN isnull(max(statusrptid+1)) then 1 else"
					+ " max(statusrptid+1) end as statusrptid , ? as empid,? as comments,? as createDate,? as department_id"
					+ " from statusreport as s) as tmp";
			connection = JDBCConnector.dbConnector();
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setInt(1, compId);
			pst.setInt(2, empid);
			pst.setString(3, comments);
			pst.setDate(4, createddate);
			pst.setInt(5, dep);
			pst.execute();
			connection.close();
			return "Saved.";
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}
		return "Error";
	}

	public void printComments(int empid) {
		try {
			String query = "SELECT * FROM statusreport where empid = ?";
			connection = JDBCConnector.dbConnector();
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setInt(1, empid);
			ResultSet rs = pst.executeQuery();
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			System.out.printf("%5s %15s %15s", "Status Id", "Comments", "Created date");
			System.out.println();
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.format("%7s %14s %17s", rs.getInt("statusrptid"), rs.getString("comments"),
						rs.getDate("createDate"));
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
