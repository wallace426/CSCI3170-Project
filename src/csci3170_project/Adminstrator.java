/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci3170_project;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Tom
 */
public class Adminstrator 
{
    public static void DoWork()
    {
        String[] main_options = new String[] {"Create tables", "Delete tables", "Load data", "Check data", "Go back"};
        while (true)
        {
            int option = Options_Selector.Show_Options("Adminstrator, what would you like to do?", main_options);
            if (option == 1)
                Create_Tables();
            else if (option == 2)
                Delete_Tables();
            else if (option == 3)
                Load_Data();
            else if (option == 4)
                Check_Data();
            else if (option == 5)
                break;
        }
    }
    private static void Check_Data()
    {
        try
        {
            List<Object[]> sql_result = SQL_Connector.Excute_Query2(SQL_Connector.Create_PS("SELECT COUNT(*) FROM Company;"));
            System.out.println("Company: " + ((long)sql_result.get(0)[0]));
            sql_result = SQL_Connector.Excute_Query2(SQL_Connector.Create_PS("SELECT COUNT(*) FROM Employee;"));
            System.out.println("Employee: " + ((long)sql_result.get(0)[0]));
            sql_result = SQL_Connector.Excute_Query2(SQL_Connector.Create_PS("SELECT COUNT(*) FROM Employer;"));
            System.out.println("Employer: " + ((long)sql_result.get(0)[0]));
            sql_result = SQL_Connector.Excute_Query2(SQL_Connector.Create_PS("SELECT COUNT(*) FROM Employment_History;"));
            System.out.println("Employment_History: " + ((long)sql_result.get(0)[0]));
            sql_result = SQL_Connector.Excute_Query2(SQL_Connector.Create_PS("SELECT COUNT(*) FROM `Position`;"));
            System.out.println("Position: " + ((long)sql_result.get(0)[0]));
            sql_result = SQL_Connector.Excute_Query2(SQL_Connector.Create_PS("SELECT COUNT(*) FROM marked;"));
            System.out.println("marked: " + ((long)sql_result.get(0)[0]));
        }
        catch (Exception ex)
        {
             System.out.println("[Error] " + ex);
        }
    }
    private static void Load_Data()
    {
        System.out.println("Please enter the folder path.");
       
        Scanner read=new Scanner(System.in);
         String path=read.nextLine();

        System.out.print("Processing... ");
        try 
        {
            for (String line : Files.readAllLines(FileSystems.getDefault().getPath(path+"/company.csv")))
            {
                String[] values = line.split(",");
                //Company, Size, Founded
                String query = "INSERT INTO Company VALUES (?, ?, ?)";
                PreparedStatement ps = SQL_Connector.Create_PS(query);
                ps.setString(1, values[0]);
                ps.setInt(2, Integer.parseInt(values[1]));
                ps.setInt(3, Integer.parseInt(values[2]));
                SQL_Connector.Excute_NonReturnQuery(ps);
            }
            for (String line : Files.readAllLines(FileSystems.getDefault().getPath(path+"/employee.csv")))
            {
                String[] values = line.split(",");
                //Employee_ID, Name, Experience, Expected_Salary, Skills
                String query = "INSERT INTO Employee VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = SQL_Connector.Create_PS(query);
                ps.setString(1, values[0]);
                ps.setString(2, values[1]);
                ps.setInt(3, Integer.parseInt(values[2]));
                ps.setInt(4, Integer.parseInt(values[3]));
                ps.setString(5, values[4]);
                SQL_Connector.Excute_NonReturnQuery(ps);
            }
            for (String line : Files.readAllLines(FileSystems.getDefault().getPath(path+"/employer.csv")))
            {
                String[] values = line.split(",");
                //Employer_ID, Name, Company
                String query = "INSERT INTO Employer VALUES (?, ?, ?)";
                PreparedStatement ps = SQL_Connector.Create_PS(query);
                ps.setString(1, values[0]);
                ps.setString(2, values[1]);
                ps.setString(3, values[2]);
                SQL_Connector.Excute_NonReturnQuery(ps);
            }
            for (String line : Files.readAllLines(FileSystems.getDefault().getPath(path+"/history.csv")))
            {
                String[] values = line.split(",");
                //Position_ID, Employee_ID, Start, End
                String query = "SET FOREIGN_KEY_CHECKS=0;INSERT INTO Employment_History VALUES (?, ?, ?, ?);SET FOREIGN_KEY_CHECKS=1;";
                PreparedStatement ps = SQL_Connector.Create_PS(query);
                ps.setString(1, values[2]);
                ps.setString(2, values[0]);
                ps.setDate(3, Date.valueOf(values[3]));
                if (values[4].compareTo("NULL") == 0)
                    ps.setNull(4, Types.DATE);
                else
                    ps.setDate(4, Date.valueOf(values[4]));
                SQL_Connector.Excute_NonReturnQuery(ps);
            }
            for (String line : Files.readAllLines(FileSystems.getDefault().getPath(path+"/position.csv")))
            {
                String[] values = line.split(",");
                //Position_ID, Position_Title, Salary, Experience, Status, Employer_ID
                String query = "SET FOREIGN_KEY_CHECKS=0;INSERT INTO `Position` VALUES (?, ?, ?, ?, ?, ?);SET FOREIGN_KEY_CHECKS=1;";
                PreparedStatement ps = SQL_Connector.Create_PS(query);
                ps.setString(1, values[0]);
                ps.setString(2, values[1]);
                ps.setInt(3, Integer.parseInt(values[2]));
                ps.setInt(4, Integer.parseInt(values[3]));
                ps.setBoolean(5, Boolean.parseBoolean(values[5]));
                ps.setString(6, values[4]);
                SQL_Connector.Excute_NonReturnQuery(ps);
            }
        } 
        catch (Exception ex) 
        {
            System.out.println("[Error] " + ex);
            return;
        }
         System.out.println("Data are loaded!");
    }
    private static void Create_Tables()
    {
        System.out.print("Processing... ");
        String query = "";
        query += "CREATE TABLE Employee( Employee_ID varchar(6) primary key, Name varchar(30) not null, Expected_Salary integer CHECK (Expected_Salary>0), Experience integer CHECK (Experience>0), Skills varchar(50) not null);";
        query += "CREATE TABLE Company( Company varchar(30) primary key, Size integer CHECK (Size>0), Founded year );";
        query += "CREATE TABLE Employer( Employer_ID varchar(6) primary key, Name varchar(30) not null, Company varchar(30) not null);";
        query += "CREATE TABLE `Position`( Position_ID varchar(6) primary key, Position_Title varchar(30) not null, Salary integer CHECK (Salary>0), Experience integer CHECK (Experience>=0), Status bool, Employer_ID varchar(6) not null, FOREIGN KEY (Employer_ID) REFERENCES Employer (Employer_ID));";
        query += "CREATE TABLE Employment_History(  Position_ID varchar(6), Employee_ID varchar(6), Start DATE, End DATE,  primary key(Employee_ID, Start), FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID), FOREIGN KEY (Position_ID) REFERENCES Position (Position_ID));";
        query += "CREATE TABLE marked( Position_ID varchar(6), Employee_ID varchar(6), Status bool, primary key(Employee_ID, Position_ID), FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID), FOREIGN KEY (Position_ID) REFERENCES Position (Position_ID) );";
        try 
        {
            PreparedStatement ps = SQL_Connector.Create_PS(query);
            SQL_Connector.Excute_NonReturnQuery(ps);
        } 
        catch (SQLException ex) 
        {
            System.out.println("[Error] " + ex);
            return;
        }
        System.out.println("Tables are created!");
    }
    private static void Delete_Tables()
    {
         System.out.print("Processing... ");
        String query = "SET FOREIGN_KEY_CHECKS=0;";
        query += "DROP TABLE Employee;";
        query += "DROP TABLE Company;";
        query += "DROP TABLE Employer;";
        query += "DROP TABLE `Position`;";
        query += "DROP TABLE Employment_History;";
        query += "DROP TABLE marked;";
        query += "SET FOREIGN_KEY_CHECKS=1;";
        try 
        {
            PreparedStatement ps = SQL_Connector.Create_PS(query);
            SQL_Connector.Excute_NonReturnQuery(ps);
        } 
        catch (SQLException ex) 
        {
            System.out.println("[Error] " + ex);
            return;
        }
         System.out.println("Tables are deleted!");
    }
}
