package com.simplilearn.EMS;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Employees {
	private static final Logger LOGGER = LoggerFactory.getLogger(Employees.class);

	private Connection connection = null;

	public String addEmployee(String fName, String lname, Date dob, String email, int dep) {
		try {
			String query = "INSERT INTO employees(empid, FIRSTNAME, LASTNAME, DOB, EMAIL,DEPARTMENT_ID)"
					+ " SELECT * FROM ( "
					+ "SELECT case when isnull(max(empid+1)) then 1 else max(empid+1) end as empid,"
					+ " ? as FIRST_NAME,? as LAST_NAME,? as D_O_B,? as EMAIL_ID,? as DEPID  from employees as e) as tmp ";
			connection = JDBCConnector.dbConnector();
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, fName);
			pst.setString(2, lname);
			pst.setDate(3, dob);
			pst.setString(4, email);
			pst.setInt(5, dep);
			pst.execute();
			//add in login master.
			/*query = "INSERT INTO LOGIN_MASTER (userid, password, role) "
					+ "SELECT * from (select max(em.empid) as userid, ? as password, dep.department_nm as role "
					+ " from employees as em inner join department as dep ON dep.department_id = em.department_id) as tmp";
			pst = connection.prepareStatement(query);
			pst.setString(1, "password");
			System.out.println(pst.toString());
			pst.execute();*/
			connection.close();
			return "Saved";
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}
		return "Error";
	}

	public void printEmployeeDetails() {
		try {
			connection = JDBCConnector.dbConnector();
			String query = "SELECT  empid,firstname,lastname,dob,email,department_nm" + " FROM  employees emp "
					+ " LEFT OUTER JOIN  department depart "
					+ " ON emp.department_id = depart.department_id order by empid";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			System.out.printf("%5s %15s %15s %12s %15s %27s", "empid", "firstname", "lastname", "dob", "email",
					"department_nm");
			System.out.println();
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.format("%5s %15s %15s %15s %23s %15s", rs.getInt("empid"), rs.getString("firstname"),
						rs.getString("lastname"), rs.getDate("dob"), rs.getString("email"),
						rs.getString("department_nm"));
				System.out.println();
			}
			System.out.println(
					"---------------------------------------------------------------------------------------------");
			connection.close();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}
	}

	public String editEmployee(String fName, String lName, Date dob, String email, int dep, int empid) {
		try {
			connection = JDBCConnector.dbConnector();
			String query = "UPDATE employees set FIRSTNAME= ? , LASTNAME = ? ,"
					+ " dob = ?, email = ?, department_id = ? WHERE empid = ? ";
			

			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, fName);
			pst.setString(2, lName);
			pst.setDate(3, dob);
			pst.setString(4, email);
			pst.setInt(5, dep);
			pst.setInt(6, empid);
			//LOGGER.info(pst.toString());
			pst.execute();
			connection.close();
			return "Updated";
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return "Error";

	}

	public String deleteEmployee(int empid) {
		try {
			connection = JDBCConnector.dbConnector();
			//check user.
			String query = "DELETE FROm employees where empid = ?";
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setInt(1, empid);
			pst.execute();
			return "Deleted.";

		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}
		return "Error";

	}
}
