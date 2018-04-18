package hw_jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class HW1 {
	static Connection conn;
	static Scanner sc = new Scanner(System.in);
	static int count = 0;
	
	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub

		String url = "jdbc:mysql://localhost:3306/peaples";
		String user = "root";
		String password = "root";
		conn = DriverManager.getConnection(url,user,password);
		System.out.println("Connected& " + !conn.isClosed());
		createDataBase();

		
		while (true) {
	        menuPrint();
	        String choise = sc.next();
	        switch (choise) {
	            case "1":
	            	addPerson();
	                break;
	            case "2":
	            	System.out.println("Input person id that you want to remove from the database: ");
	    	        int id = sc.nextInt();
	            	delPersonForId(id);
	                break;
	            case "3":
	            	printPerson();
	                break;
	            case "4":
	            	printPerson();
	            	System.out.println("Input person id that you want select from the database: ");
	    	        int selectId = sc.nextInt();
	            	printPersonForId(selectId);
	                break;
	            case "5":
	            	ramdomAddData();
	                break;
	            case "6":
	            	System.out.println("Input person id that you want update data from the database: ");
	    	        int selectIdFromUpdate = sc.nextInt();
	            	updateDataForIdPerson(selectIdFromUpdate);
	            	break;
	            case "7":
	            	return;

	        }
	      }
		}
		
		static void menuPrint() {
			System.out.println();
			System.out.println("1. Add person to database");
			System.out.println("2. Delete person from database by ID");
			System.out.println("3. Print list all person from database");
			System.out.println("4. Show person by ID");
			System.out.println("5. Fill the tables with data from the file");
			System.out.println("6. Update data by persom via ID");
			System.out.println("7. Exit program");
			System.out.println("*******************************");
			System.out.println("Make your choice");
		}	
		
		static void	createDataBase() throws SQLException {
			String droupTablePeaple = "DROP TABLE IF EXISTS peaple;";
			String createTablePeaple = "CREATE TABLE peaple("
					+ "id int primary key auto_increment,"
					+ "First_name varchar(30),"
					+ "Last_name varchar(30),"
					+ "age int,"
					+ "address_id int"
					+ ");";
			
			String droupTableAddresPeaple = "DROP TABLE IF EXISTS address;";
			String createTableAddresPeaple = "CREATE TABLE address ("
					+ "id int primary key auto_increment,"
					+ "sity varchar(100),"
					+ "street varchar(250),"
					+ "house int"
					+ ");";
			String alterQuery  = "ALTER TABLE peaple ADD FOREIGN key (address_id) "
					+ "references address(id);";
			Statement stmt = conn.createStatement();
			stmt.execute(droupTablePeaple);
			stmt.execute(createTablePeaple);
			stmt.execute(droupTableAddresPeaple);
			stmt.execute(createTableAddresPeaple);
			stmt.execute(alterQuery);
			stmt.close();
		}
		
		static void addPerson() throws SQLException {
			System.out.println("Input first name peaple");
	        String firstname = new Scanner(System.in).next();
			System.out.println("Input last name peaple");
	        String lastname = new Scanner(System.in).next();
	        System.out.println("Input age peaple");
	        int age = new Scanner(System.in).nextInt();
	        System.out.println("Input sity peaple");
	        String sity = new Scanner(System.in).next();
			System.out.println("Input street peaple");
	        String street = new Scanner(System.in).next();
	        System.out.println("Input number house peaple");
	        int house = new Scanner(System.in).nextInt();
	        
	        
	        addaddress(sity, street, house);
	        count +=1;
	        addpeaple(firstname, lastname, age);
		}
		
		static void addaddress(String sity, String street, int house) throws SQLException {
	        String address = "INSERT INTO address(sity, street, house) values (?, ?, ?);";
			PreparedStatement pstmt1 = conn.prepareStatement(address);
			pstmt1.setString(1, sity);
			pstmt1.setString(2, street);
			pstmt1.setInt(3, house);
			pstmt1.executeUpdate();
			pstmt1.close();
		}
		static void addpeaple(String firstname, String lastname, int age) throws SQLException {
			String querypeaple = "INSERT INTO peaple(First_name, Last_name, age, address_id	) values (?, ?, ?, ?);";
			PreparedStatement pstmt2 = conn.prepareStatement(querypeaple);
			pstmt2.setString(1, firstname);
			pstmt2.setString(2, lastname);
			pstmt2.setInt(3, age);
			pstmt2.setInt(4, count);
			pstmt2.executeUpdate();
			pstmt2.close();
		}
		
		static void delPersonForId(int selectIdFromRemove) throws SQLException {
			String query = "delete from peaple where id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, selectIdFromRemove);
			pstmt.executeUpdate();
			pstmt.close();
			
		}

		static void printPerson () throws SQLException {
			String query = "SELECT * FROM peaple p JOIN address a ON a.id=p.address_id;";
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			List<String> Peaples = new ArrayList<>();
			while (rs.next()) {
				Peaples.add(
						"Id: "+ rs.getInt("id") + "\t|" 
								+ "First Name: " + rs.getString("first_name") + "\t|" 
								+ "Last Name: " + rs.getString("last_name") + "\t|" 
								+ "Age: " + rs.getInt("age")+  "\t|"
								+ "Sity: "+ rs.getString("sity")+  "\t|"
								+ "Street: " + rs.getString("street")+  "\t|"
								+ "House: " + rs.getInt("house"));
			}
			Peaples.forEach(System.out::println);
		}

		static void printPersonForId(int selectId) throws SQLException {
			String query = "SELECT * FROM peaple p JOIN address a ON a.id=p.address_id;";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, selectId);
			ResultSet rs = pstmt.executeQuery();
			
			List<String> Peaples = new ArrayList<>();
			while (rs.next()) {
				Peaples.add(
						"Id: "+ rs.getInt("id") + "\t|" 
								+ "First Name: " + rs.getString("first_name") + "\t|" 
								+ "Last Name: " + rs.getString("last_name") + "\t|" 
								+ "Age: " + rs.getInt("age")+  "\t|"
								+ "Sity: "+ rs.getString("sity")+  "\t|"
								+ "Street: " + rs.getString("street")+  "\t|"
								+ "House: " + rs.getInt("house"));
				}
			Peaples.forEach(System.out::println);
			
		}
		
		static void ramdomAddData() throws SQLException, IOException {
			int k = new Random().nextInt(100) + 1;
			int count=0;
	        File f = new File("list.txt");
	        if (f.exists()) {

	            FileReader fr = new FileReader(f);
	            BufferedReader br = new BufferedReader(fr);
	            try {
	                String line;
	                String[] arr = new String[6];
	                while ((line = br.readLine()) != null) {
	                    int i = 0;
	                    for (String temp : line.split("\t")) {
	                        arr[i++] = temp;
	                    }

	        	        String address = "INSERT INTO address(sity, street, house) values (?, ?, ?);";
	        			PreparedStatement pstmt1 = conn.prepareStatement(address);
	        			pstmt1.setString(1, arr[3]);
	        			pstmt1.setString(2, arr[4]);
	        			pstmt1.setInt(3, Integer.valueOf(arr[5]));
	        			pstmt1.executeUpdate();
	        			pstmt1.close();
	        			
	        			count +=1;
	        			String querypeaple = "INSERT INTO peaple(First_name, Last_name, age, address_id) values (?, ?, ?, ?);";
	        			PreparedStatement pstmt = conn.prepareStatement(querypeaple);
	        			pstmt.setString(1, arr[0]);
	        			pstmt.setString(2, arr[1]);
	        			pstmt.setInt(3, Integer.valueOf(arr[2]));
	        			pstmt.setInt(4, count);
	        			pstmt.executeUpdate();
	        			pstmt.close();
	        			

	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {
	                fr.close();
	                br.close();
	            }

	        } else {
	            System.out.println("File not found....");
	            return;
	        }
			
		} 
		
		static void updateDataForIdPerson(int selectIdFromUpdate) throws SQLException {
			String query = "UPDATE peaple SET first_name = ? , last_name = ?, age = ? WHERE id = ?;";
			
        	System.out.println("Input new peaple First Name: ");
	        String newFirstName = sc.next();
        	System.out.println("Input new peaple Last Name: ");
	        String newLasttName = sc.next();
        	System.out.println("Input new peaple age: ");
	        int newAge = sc.nextInt();
	        
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, newFirstName);
			pstmt.setString(2, newLasttName);
			pstmt.setInt(3, newAge);
			pstmt.setInt(4, selectIdFromUpdate);
			
			pstmt.executeUpdate();
			pstmt.close();
		}
}

