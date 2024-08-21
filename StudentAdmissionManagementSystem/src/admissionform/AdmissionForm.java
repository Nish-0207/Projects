package admissionform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;


import connection.MyConnection;

public class AdmissionForm {

	static Connection connection;
	static PreparedStatement pStatement;
	static ResultSet resultSet;
	
	// opening connection
	public AdmissionForm() throws ClassNotFoundException, SQLException 
	{
		connection = MyConnection.getMyConnection();
		System.out.println("Connection Opened");
	}		
	 
	 //auto increment of registration number
	 public static int registrationNo() throws SQLException 
	 {
		 int regNo = 0;
		 pStatement= connection.prepareStatement("Select max(RegNo) as lastRegNo from studentRecords ");
		resultSet = pStatement.executeQuery();
		if(resultSet.next())
		{
		 int a = resultSet.getInt("lastRegNo");
		  regNo= a+1;			
		}
		return regNo;		
	 }
	 
	 
	//inserting data
	public  void insertStudentRecords(int reg,String studentName, String dateOfBirth, String phoneNo, String emailId, String address,String bloodGroup,float sscPercent, float hscPercent, java.sql.Date addmisionDate  ) throws SQLException 
	{
		pStatement = connection.prepareStatement("insert into studentRecords value(?,?,?,?,?,?,?,?,?,?)");
		pStatement.setInt(1, reg);
		pStatement.setString(2, studentName);
		pStatement.setString(3, dateOfBirth);
		pStatement.setString(4, phoneNo);
		pStatement.setString(5, emailId);
		pStatement.setString(6, address);
		pStatement.setString(7, bloodGroup);
		pStatement.setFloat(8, sscPercent);
		pStatement.setFloat(9, hscPercent);
		pStatement.setDate(10, addmisionDate);
		pStatement.executeUpdate();
		System.out.println("Record is inserted successfully");		
	}

	
	//separator function
	public static void separator(int numColumns) 
	{
        for (int i = 1; i <= numColumns; i++) 
        {
            for (int j = 0; j < 22; j++)
            { 
                System.out.print("-");
            }
        }
        System.out.println();
	}
	
	
	//to view all the student records in database
	public void viewAllRecords() throws SQLException 
	{
		pStatement = connection.prepareStatement("Select * from studentRecords");
		resultSet = pStatement.executeQuery();
		// Get metadata (to extract column names)
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numColumns = metaData.getColumnCount();
        
        // Print column headers
        for (int i = 1; i <= numColumns; i++) 
        {
            System.out.printf("%-22s", metaData.getColumnName(i)); 
        }
        System.out.println();
               
        separator(numColumns);	// using it to separate data
        
        // Print table data
        while (resultSet.next()) 
        {
            for (int i = 1; i <= numColumns; i++) 
            {
                System.out.printf("%-22s", resultSet.getString(i)); 
            }
            System.out.println();
            separator(numColumns);	// using it to separate data
        }      		
	}
	
	
	// search student by registration number (regNo)
	
	public void searchByRegNo(int regNo) throws SQLException 
	{
		pStatement = connection.prepareStatement("Select * from studentRecords where RegNo = ?");
		pStatement.setInt(1, regNo);
		resultSet= pStatement.executeQuery();
		//getting table headings
		ResultSetMetaData metaData = resultSet.getMetaData();
	    int numColumns = metaData.getColumnCount();
		// Print column headers
        for (int i = 1; i <= numColumns; i++) 
        {
            System.out.printf("%-22s", metaData.getColumnName(i)); 
        }
        System.out.println();
               
        separator(numColumns);	// using it to separate data
        
		// Print table data
        while (resultSet.next()) 
        {
            for (int i = 1; i <= numColumns; i++) 
            {
                System.out.printf("%-22s", resultSet.getString(i)); 
            }
            System.out.println();
        }
	}
	
	
	//delete Student Record using RegNo	
	public void deleteRecord(int regNo) throws SQLException 
	{		
		pStatement = connection.prepareStatement("delete from studentRecords where RegNo = ?");
		pStatement.setInt(1, regNo);
		pStatement.execute();
		System.out.println("Deleted Record "+regNo);		
	}
	
	
	// update Student details
	public void updateDetails(String head,String info, int RegNo) throws SQLException 
	{
		pStatement= connection.prepareStatement("UPDATE studentRecords SET "+head+  "  =  ? WHERE RegNo = ?");
		pStatement.setString(1,info);
		pStatement.setInt(2,RegNo);
		int rowsUpdated =  pStatement.executeUpdate();
		if (rowsUpdated > 0) 
		{
            System.out.println("Updated Successfully");
        } 
		else 
        {
            System.out.println("No records updated. Check registration number.");
        }
	}
	
	
	//sorting
	public void sortingDetails(String recordName) throws SQLException 
	{	
		System.out.println("Sorting By "+recordName);
		pStatement = connection.prepareStatement("select * from studentRecords order by "+recordName );
		resultSet= pStatement.executeQuery();
		// getting table headings
		ResultSetMetaData metaData = resultSet.getMetaData();
		int numColumns = metaData.getColumnCount();
		// Print column headers
		for (int i = 1; i <= numColumns; i++) 
		{
			System.out.printf("%-22s", metaData.getColumnName(i));
		}
		System.out.println();

		separator(numColumns); // using it to separate data

		// Print table data
		while (resultSet.next()) 
		{
			for (int i = 1; i <= numColumns; i++) 
			{
				System.out.printf("%-22s", resultSet.getString(i));
			}
			System.out.println();
			separator(numColumns);
		} 		
	}
	
	
	public void closeConnection() throws SQLException 
	{
		connection.close();
		pStatement.close();
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException 
	{	
		AdmissionForm obj = new AdmissionForm();		
		String studentName, dateOfBirth, phoneNo, emailId, address,bloodGroup;
		float sscPercent, hscPercent;
		int n;
		Scanner sc = new Scanner(System.in);
	do {
		System.out.println("1.New Admission\n"
							+"2.View All Student Details\n"
							+"3.Search Student by Registration Number\n"
							+"4.Delete Student Record\n"
							+"5.Update Details\n"
							+"6.Sort\n"
							+"7.Exit\n");
		 n = sc.nextInt();	//input for switch case
		
		switch (n) {
		case 1: 	// inserting records in database
			int j = registrationNo(); //calling new regNo
			System.out.println("Enter Student Name : ");
			sc.nextLine();
			studentName = sc.nextLine();	
			System.out.println("Enter Date Of Birth (YYYY/MM/DD) : ");
			dateOfBirth = sc.next();
			System.out.println("Enter Phone Number : ");
			phoneNo = sc.next();
			System.out.println("Enter Email ID : ");
			emailId = sc.next();
			System.out.println("Enter address : ");
			sc.nextLine();
			address = sc.nextLine();
			System.out.println("Enter Blood Group : ");
			bloodGroup = sc.next();
			System.out.println("Enter 10th Percentage : ");
			sscPercent = sc.nextFloat();
			System.out.println("Enter 12th Percentage");
			hscPercent = sc.nextFloat();
			Date date = new Date();
			java.sql.Date addmisioDate  =new java.sql.Date(date.getTime());
			obj.insertStudentRecords(j, studentName, dateOfBirth, phoneNo, emailId, address, bloodGroup, sscPercent, hscPercent, addmisioDate);
			break;
			
		case 2:	// view all student records 
			obj.viewAllRecords();
			break;
			
		case 3://search student by registration no
			System.out.println("Enter Registration Number (RegNo) : ");
			int a=0;
			try 
			{
				a =sc.nextInt();
			} 
			catch (Exception e) 
			{
				System.err.println("Wrong Input Type");
				System.exit(0);
			}
			obj.searchByRegNo(a);
			break;
			
		case 4: //delete student record
			System.out.println("Enter Registration Number (RegNo) : ");
			int d=0;
			try 
			{
				d= sc.nextInt();
			} 
			catch (Exception e) 
			{
				System.err.println("Wrong Input Type");
				System.exit(0);
			}
			obj.deleteRecord(d);
			
		break;
		
		case 5: // update Details
			System.out.println("Enter Registration Number to update details : ");
			int rn = 0;
			try 
			{ 
				rn = sc.nextInt();
			} 
			catch (Exception e) 
			{
				System.err.println("Wrong Input Type");
				System.exit(0);
			}
			System.out.println("Select Option to Update Details : \n"
					+ "1.Student Name\r\n"
					+ "2.Date Of Birth\r\n"
					+ "3.Phone Number\r\n"
					+ "4.Email Id\r\n"
					+ "5.Address\r\n"
					+ "6.Blood Group\r\n"
					+ "7.10th Percentage\r\n"
					+ "8.12th Percentage\r\n");
			int k= sc.nextInt();
			String head = "";
			switch (k) 
			{
			case 1: 
				head = "StudentName";
				break;
				
			case 2:	
				head = "dateOfBirth";
				break;
				
			case 3: 
				head = "phone";
				break;
				
			case 4:
				head = "emailId";
				break;
				
			case 5:
				head = "address";
				break;
			
			case 6:
				head = "bloodGroup";
				break;
				
			case 7:
				head = "10thPercent";
				break;
		
			case 8:
				head = "12thPercent";
				break;

			default: System.err.println("Invalid Input");
				break;
			}
			sc.nextLine();
			System.out.println("Enter info to be updated : ");
			String info = sc.nextLine();
			
			obj.updateDetails(head,info,rn);
			break;
		
		case 6: //sorting 
			System.out.println("Select Option to Sort Details in Format : \n"
					+ "1.Sort By Registration Number \r\n"
					+ "2.Sort By Name\r\n"
					+ "3.Sort By Admission Date");
			int r = sc.nextInt();
			String recordName = "";
			switch (r) {
			case 1: // sort by RegNo
				recordName = "RegNo";
				break;
				
			case 2:// sort by name
				recordName="StudentName";
				break;
				
			case 3: //sort by admission
				recordName= "admissionDate";
				break;

			default:
				break;
			}			
			obj.sortingDetails(recordName);			
			break;
			
		case 7: System.out.println("Exiting.......");
				System.exit(0);
		break;

		default:	System.err.println("Invalid Input");
			break;
		}
	} while (n!=7);	
	
		sc.close();
	}
}
