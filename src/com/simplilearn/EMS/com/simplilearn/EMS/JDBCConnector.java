package com.simplilearn.EMS;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class JDBCConnector {
	private static final Logger logger = LoggerFactory.getLogger(JDBCConnector.class);
	Connection con = null;
	public static Connection dbConnector(){
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems_db?user=root&password=Trust@24");
			//JOptionPane.showMessageDialog(null, "Connection Successful!");
			return con;
		}catch(Exception e){
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}
}
