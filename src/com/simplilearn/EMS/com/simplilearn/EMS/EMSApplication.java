package com.simplilearn.EMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EMSApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMSApplication.class);

	private static enum COD_ENUM {
		BR002, BR003, BR004, BR005, BR006, BR007, BR008, BR009, BR0010, BR0011, BR0012, LOGOUT
	};

	private static Scanner sc = new Scanner(System.in);

	private static boolean Logged_In = false;
	private static String role = "";
	private static int userId;
	private static Connection connection = null;

	public static void main(String[] args) {
		run_app();
	}

	private static void printHeader() {
		System.out.print(" ################################### ");
		System.out.print("Employee Management Console App");
		System.out.println(" ################################### ");
		System.out.println("     User Id:" + userId + "    Role: " + role);
	}

	private static void run_app() {
		printHeader();
		if (Logged_In) {
			Employees em = new Employees();
			Department d = new Department();
			Compliance c = new Compliance();
			Statusreport s = new Statusreport();
			System.out.println("Select below one action.");
			for (COD_ENUM action : COD_ENUM.values()) {
				System.out.print(action + " ");
			}
			System.out.println();
			String actionText = sc.next();
			try {
				COD_ENUM action = COD_ENUM.valueOf(actionText.toUpperCase());
				switch (action) {
				case LOGOUT:
					Logged_In = false;
					break;
				case BR002:
					if (role.equalsIgnoreCase("admin")) {
						System.out.println("Please enter First name.*");
						String fName = sc.next();
						System.out.println("Please enter Last name.*");
						sc.nextLine();
						String lname = sc.next();
						System.out.println("Please enter Email.*");
						sc.nextLine();
						String email = sc.next();
						System.out.println("Please enter DOB(dd/MM/yyyy).*");
						sc.nextLine();
						String dob = sc.next();
						System.out.println("Please enter Department.*");
						sc.nextLine();
						int dep = sc.nextInt();
						if ("".equalsIgnoreCase(fName) || "".equalsIgnoreCase(lname) || "".equalsIgnoreCase(email)
								|| "".equalsIgnoreCase(dob) || !(dep > 0)) {
							LOGGER.error("please provide all mandatory details.");
						} else {
							java.sql.Date db;
							try {
								db = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(dob).getTime());
								String res = em.addEmployee(fName, lname, db, email, dep);
								if (!res.contains("Error")) {
									System.out.println("Employee added successfully.");
								} else {
									System.out.println("Error while adding Employee.");
								}
							} catch (ParseException e) {
								LOGGER.error(e.getLocalizedMessage());
							}

						}
					} else {
						LOGGER.error("Only admin can perform this action.");
					}
					break;
				case BR003:
					// list of employees by admin.
					if (role.equalsIgnoreCase("admin")) {
						em.printEmployeeDetails();
					} else {
						LOGGER.error("Only admin can perform this action.");
					}
					break;
				case BR004:
					System.out.println(
							"Please provide below details to update an employee. Note: Leave blank if you dont want to update the filed.");
					System.out.println("*mandatory, Enter Employee Id.");
					sc.nextLine();
					int empid = sc.nextInt();
					String fName, lName, email;
					int dep;
					if (!(empid > 0)) {
						System.out.println("Invalid empid.*");
					} else {
						System.out.println("Please enter first name.*");
						sc.nextLine();
						fName = sc.next();
						System.out.println("Please enter last name.*");
						sc.nextLine();
						lName = sc.next();
						System.out.println("Please enter email.*");
						sc.nextLine();
						email = sc.next();
						System.out.println("Please enter dob(dd/MM/yyyy).*");
						sc.nextLine();
						String dobS = sc.next();
						try {
							System.out.println("please enter department.*");
							dep = sc.nextInt();
							if (dep > 0) {
								java.sql.Date dobNew = new java.sql.Date(
										new SimpleDateFormat("dd/MM/yyyy").parse(dobS).getTime());
								String res = em.editEmployee(fName, lName, dobNew, email, dep, empid);
								if (!res.contains("Error")) {
									System.out.println("Employee details updated successfully.");
								}
							} else {
								LOGGER.error("Invalid department.");
							}
						} catch (Exception e) {
							LOGGER.error(e.getMessage());
						}

					}

					break;
				case BR005:
					if (role.equalsIgnoreCase("admin")) {
						System.out.println("Please enter Employee id to be deleted.*");
						int empid1 = sc.nextInt();
						if (Objects.nonNull(empid1) && empid1 > 0) {
							System.out.println("Please confirm to delete the Empoyee: y/n");
							sc.nextLine();
							String confirm = sc.next();
							if (confirm.equalsIgnoreCase("y")) {
								String res = em.deleteEmployee(empid1);
								if (!res.contains("Error")) {
									System.out.println("Successfully deleted the employee with ID: " + empid1);
								} else {
									LOGGER.error("Error deleting the employee.");
								}
							} else {
								System.out.println("Cancelled employee deletion.");
							}
						} else {
							LOGGER.error("Invalid employee Id.");
						}
					} else {
						LOGGER.error("Only admin can perform this action.");
					}
					break;
				case BR006:
					if (role.equalsIgnoreCase("admin")) {
						System.out.println("Please enter department name to add.*");
						String depname = sc.next();
						String res = d.addDepartment(depname);
						System.out.println(res);
					} else {
						LOGGER.error("Only admin can access this action.");
					}
					break;
				case BR007:
					d.printDepartment();
					break;
				case BR008:
					if (role.equalsIgnoreCase("admin")) {
						System.out.println("Please enter RL type.*");
						String rlType = sc.next();
						sc.nextLine();
						System.out.println("Please enter RL details.*");
						String details = sc.next();

						System.out.println("Please enter department.*");
						sc.nextLine();
						int dep1 = sc.nextInt();
						if (!"".equalsIgnoreCase(rlType) && !"".equalsIgnoreCase(details) && Objects.nonNull(dep1)) {
							String res = c.addCompliance(rlType, details, dep1);
							if (!res.contains("Error")) {
								System.out.println("RL details saved successfully.");
							} else {
								LOGGER.error("Error saving Compliance.");
							}
						} else {
							LOGGER.error("Invalid input details.");
						}
					} else {
						LOGGER.error("Only admin can access this action.");
					}
					break;
				case BR009:
					System.out.println("All RL('s).");
					c.printRLStatus(0);
					break;
				case BR0010:
					System.out.println("RL Assigned to me.");
					c.printRLStatus(userId);
					break;
				case BR0011:
					System.out.println("Please enter compliance.*");
					int cId = sc.nextInt();
					System.out.println("Please enter comments.*");
					sc.nextLine();
					String comments = sc.next();
					System.out.println("Please enter department.*");
					sc.nextLine();
					int dep1 = sc.nextInt();
					if (!"".equalsIgnoreCase(comments)) {
						String res = s.addStatus(cId, userId, comments, dep1);
						if (!res.contains("Error")) {
							System.out.println("Comments updated successfully.");
						} else {
							LOGGER.error("Error while saving the comments.");
						}
					} else {
						LOGGER.error("Invalid inputs.");
					}
					break;
				case BR0012:
					s.printComments(userId);
					break;

				default:
					System.out.println("Invalid input. Please select correct option.");
					break;
				}
				if (Logged_In) {
					run_app();
				} else {
					stop_app();
				}
			} catch (Exception e) {
				LOGGER.error("Invalid action input.");
				run_app();
			}

		} else {

			try {
				// BR001 - login with different roles.
				System.out.println("Please Login to EMS system.");
				System.out.println("Enter user id.");
				userId = sc.nextInt();
				System.out.println("Enter password.");
				sc.nextLine();
				String password = sc.next();
				connection = JDBCConnector.dbConnector();
				String query = "SELECT * from LOGIN_MASTER where userid=? and password=?";
				PreparedStatement pst = connection.prepareStatement(query);
				pst.setInt(1, userId);
				pst.setString(2, password);

				ResultSet rs = pst.executeQuery();
				Logged_In = false;
				if (rs.next()) {
					Logged_In = true;
					role = rs.getString("role").toUpperCase();
				} else {
					System.out.println("userId not found.");
				}
				connection.close();
				if (Logged_In) {

					System.out.println(userId + " Logged in successfully as " + role);
					run_app();
				}

			} catch (Exception e) {
				// e.printStackTrace();
				LOGGER.error(e.toString());
			}
		}

	}

	private static void stop_app() {
		System.out.println(
				"---------------------------------------------------------------------------------------------");

		System.out.println("Thanks for using EMS console App. ");
		System.out.println(
				"---------------------------------------------------------------------------------------------");

	}

}
