package com.simplilearn.EMS;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Compliance {

	private static final Logger LOGGER = LoggerFactory.getLogger(Compliance.class);
	private static Connection connection = null;

	public String addCompliance(String rltype, String rldetails, int dep) {
		try {
			Date createddate = new Date(new java.util.Date().getTime());
			String query = "INSERT INTO compliance (complianceid, rlType, details, createDate, department_id) "
					+ "SELECT * from (SELECT case when isNull(max(complianceid+1)) "
					+ "then 1 else max(complianceid+1) end as complianceid,? as a,? as b,? as c,? as d FROM compliance) as tmp";
			connection = JDBCConnector.dbConnector();
			// System.out.println(query);
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, rltype);
			pst.setString(2, rldetails);
			pst.setDate(3, createddate);
			pst.setInt(4, dep);
			// System.out.println(pst.toString());
			pst.execute();
			connection.close();
			return "Saved";
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}
		return "Error";
	}

	public void printRLStatus(int empid) {
		try {
			connection = JDBCConnector.dbConnector();
			String query = "SELECT  com.complianceid as complianceid, com.department_id, dept.department_nm,rlType,details, "
					+ "                    com.createDate, count(distinct emp.empid) AS empcount ,  "
					+ "                    count(distinct sts.empid) as statuscount  "
					+ " FROM department dept,employees emp,compliance com  " + " LEFT OUTER JOIN statusreport sts  "
					+ " ON  sts.department_id = com.department_id" + " AND sts.complianceid = com.complianceid  "
					+ " WHERE com.department_id = emp.department_id "
					+ " AND com.department_id = dept.department_id -e-"
					+ " GROUP BY com.complianceid,com.department_id,dept.department_nm,rlType,details,com.createDate";
			if (empid != 0) {
				query = query.replace("-e-", " AND emp.empid = ? ");
			} else {
				query = query.replace("-e-", "");
			}
			PreparedStatement pst = connection.prepareStatement(query);
			if (empid != 0) {
				pst.setInt(1, empid);
			}
			ResultSet rs = pst.executeQuery();
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s", "complianceid", "department_id",
					"department_nm", "rlType", "details", "createDate", "empcount", "statuscount");
			System.out.println();
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.format("%10s %10s %10s %10s %10s %10s %10s %10s", rs.getInt("complianceid"),
						rs.getInt("department_id"), rs.getString("department_nm"), rs.getString("rlType"),
						rs.getString("details"), rs.getDate("createDate"), rs.getInt("empcount"),
						rs.getInt("statuscount"));
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
