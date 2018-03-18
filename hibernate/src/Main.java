import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

	Session session;

	public static void main(String[] args) {
		Main main = new Main();
		//main.printSchools();
		//main.addNewData();
		main.getAndUpdate();
		main.close();
		
	}

	public Main() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public void close() {
		session.close();
		HibernateUtil.shutdown();
	}

	private void printSchools() {
		Criteria crit = session.createCriteria(School.class);
		List<School> schools = crit.list();

		System.out.println("### Schools and classes");
		for (School s : schools) {
			System.out.println(s);
			System.out.println("           KLASY:");
			for (SchoolClass schoolClass : s.getClasses()){
				System.out.println("      " +schoolClass);
				for (Student student : schoolClass.getStudents()){
					System.out.println(student);
				}
			}
			
		}
	
	}
	private void addNewData(){
		
		
		School newSchool = new School();
		newSchool.setName("AGH");
		newSchool.setAddress("Reymonta");
		//newSchool.setId(1);
		Set<SchoolClass> klasy = new HashSet<SchoolClass>();
		newSchool.setClasses(klasy);
		
		Transaction transaction = session.beginTransaction();
		session.save(newSchool); // gdzie newSchool to instancja nowej szko³y
		transaction.commit();
		//session.saveOrUpdate(newSchool); // mozna zasewoac i updatetowac metota ale w pliku xml trzeba usunac <generator class="native"></generator>
	}
	
	private void getAndUpdate() {
		
		Criteria crit = session.createCriteria(School.class);
		List<School> schools = crit.list();
		School AGH = schools.get(0);
		
	AGH.setAddress("DSfdsfdsf");
	
	Transaction transaction = session.beginTransaction();
	session.saveOrUpdate(AGH); // gdzie newSchool to instancja nowej szko³y
	transaction.commit();
	//session.saveOrUpdate(newSchool); 
	
	}
	

	private void jdbcTest() {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("org.sqlite.JDBC");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection("jdbc:sqlite:school.db", "", "");

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM schools";
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				String name = rs.getString("name");
				String address = rs.getString("address");

				// Display values
				System.out.println("Name: " + name);
				System.out.println(" address: " + address);
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}// end jdbcTest

}
