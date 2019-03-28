/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci3170_project;

import java.util.Date;
import java.sql.PreparedStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author wong
 */
public class Employee {
     public static void DoWork()
    {
        String[] main_options = new String[] {"Show Available Positions", "Mark Interested Position", "Check Average Working Time","Go back"};
        while (true)
        {
            int option = Options_Selector.Show_Options("Employee, what would you like to do?", main_options);
            if (option == 1)
                Show_Avail_Pos();
            else if (option == 2)
                 Mark_Interest_Pos();
            else if (option == 3)
            {
                 Check_Average_Work_Time();
            }
            else if (option == 4)
            {
                break;
            }
  
        }
    }
    
     private static void Show_Avail_Pos()
    {
         String employee_id=Get_Employee_ID();
         String query=Get_Skills(employee_id);
         //System.out.print(query);
        try
        {
            PreparedStatement ps = SQL_Connector.Create_PS("SELECT P.Position_ID,P.Position_Title,P.Salary,C.* FROM `Position` P, Employee E,Employer R, Company C where Status=TRUE AND Employee_ID=? AND Salary>=Expected_Salary AND E.Experience >= P.Experience AND R.Employer_ID=P.Employer_ID AND R.Company=C.Company AND ("+query+")");
            ps.setString(1, employee_id);
            List<HashMap<String,Object>> sql_result = SQL_Connector.Excute_Query(ps);
            if (sql_result.size() == 0)
            {
                System.out.println("No suitable position for you! Loser!");
                return;
            }
                if (sql_result.size() == 1)
                    System.out.println("Your available position is:");
                else System.out.println("Your available positions are:");
            
            System.out.println("Position_ID,   Position_Title,  Salary,  Company,  Size,  Founded");
            
            for (HashMap<String, Object> obj : sql_result)
            {
                System.out.println((String)obj.get("Position_ID")+"\t\t"+obj.get("Position_Title")+"\t\t  "+obj.get("Salary")+"  \t"+obj.get("Company")+"\t"+obj.get("Size")+"\t"+obj.get("Founded"));
            }
        }
        catch(Exception ex){
          System.out.println("[Error] " + ex);
          return;
        }
        
    }
     
    private static void Mark_Interest_Pos()
    {
      String employee_id=Get_Employee_ID();
      String query=Get_Skills(employee_id);
      String position_id;
      //System.out.print(query);
      
       try
        {
            PreparedStatement ps = SQL_Connector.Create_PS("SELECT P.Position_ID,P.Position_Title,P.Salary,C.* FROM `Position` P, Employee E,Employer R, Company C where Status=TRUE AND Employee_ID=? AND Salary>=Expected_Salary AND E.Experience >= P.Experience AND R.Employer_ID=P.Employer_ID AND R.Company=C.Company AND (" +query+") AND P.Position_ID NOT IN (SELECT Position_ID FROM Employment_History WHERE Employee_ID=?) AND 0=(SELECT COUNT(*) FROM marked M WHERE M.Position_ID=P.Position_ID AND M.Employee_ID=?);");
            ps.setString(1, employee_id);
            ps.setString(2, employee_id);
            ps.setString(3, employee_id);
            List<HashMap<String,Object>> sql_result = SQL_Connector.Excute_Query(ps);
            if (sql_result.size() == 0)
            {
                System.out.println("No suitable position for you! Loser!");
                return;
            }
                if (sql_result.size() == 1)
                    System.out.println("Your interested position is:");
                else System.out.println("Your interested positions are:");
            
            System.out.println("Position_ID,   Position_Title,  Salary,  Company,  Size,  Founded");
            
            for (HashMap<String, Object> obj : sql_result)
            {
                System.out.println((String)obj.get("Position_ID")+"\t\t"+obj.get("Position_Title")+"\t\t  "+obj.get("Salary")+"  \t"+obj.get("Company")+"\t"+obj.get("Size")+"\t"+obj.get("Founded"));
            }
            position_id =Get_Position_ID();
            boolean contains=false;
            while (!contains){
               
                for (HashMap<String, Object> obj : sql_result)
                {
                if (obj.get("Position_ID").equals(position_id))
                    contains=true;
                }
                if (!contains){
                    System.out.println("ERROR! Please check the position ID.");
                     position_id =Get_Position_ID();
                }
            }

            
            ps = SQL_Connector.Create_PS("INSERT INTO marked VALUES (?, ?, ?);");
            ps.setString(1, position_id);
            ps.setString(2, employee_id);
            ps.setInt(3, 1);
            SQL_Connector.Excute_NonReturnQuery(ps);
            System.out.println("Done.");
            
        }
        catch(Exception ex){
          System.out.println("[Error] " + ex);
          return;
        }
    }
         
    private static void Check_Average_Work_Time()
    {
         String employee_id=Get_Employee_ID();
         try
        {
            PreparedStatement ps = SQL_Connector.Create_PS("SELECT Start,End FROM Employment_History WHERE Employee_ID=? ORDER BY Start DESC LIMIT 3;");
            ps.setString(1, employee_id);
            List<HashMap<String,Object>> sql_result = SQL_Connector.Excute_Query(ps);
            if (sql_result.size() <3)
            {
                System.out.println("Less than 3 records.");
                return;
            }

            int sum=0;
            for (HashMap<String, Object> obj : sql_result)
            {
               
                Date start=(Date)obj.get("Start");
                Date end =(Date)obj.get("End");
                if (end==null)
                    end=new Date();
               long diff =end.getTime()-start.getTime();
               int diffDays=(int)(diff/(24*60*60*1000));
               sum+=diffDays;
               //System.out.println(diffDays);
               
            }
            System.out.println("Your average working time is: "+sum/3+" days.");
        }
        catch(Exception ex){
          System.out.println("[Error] " + ex);
          return;
        }
         
         
    }
     
    private static String Get_Employee_ID(){
        System.out.println("Please enter your ID.");
        Scanner scanner = new Scanner(System.in);
        String Employee_ID=scanner.nextLine();
        while (Employee_ID.length()!=6){
            System.out.println("[ERROR] Length of employee ID shoule be 6. Please enter your ID.");
            Employee_ID=scanner.nextLine();
        }
        return Employee_ID;
    } 
    
    private static String Get_Position_ID(){
            System.out.println("Please enter one interested Position_ID:");
            Scanner scanner = new Scanner(System.in);
            String position_id=scanner.nextLine();
            while (position_id.length()!=6){
                System.out.println("[ERROR] Length of employee ID shoule be 6. Please enter your ID.");
                position_id=scanner.nextLine();
            }
            
            return position_id;
    }
    private static String Get_Skills(String employee_id){
         String skills="",query="";
         String[] split_skill;
         try{
         
             PreparedStatement ps = SQL_Connector.Create_PS("SELECT * FROM Employee WHERE Employee_ID = ?");
             ps.setString(1, employee_id);
             List<Object[]> sql_result = SQL_Connector.Excute_Query2(ps);
             if (sql_result.isEmpty())
            {
                System.out.println("[Error] Employee ID does not exist!");
                return null;
            }
            skills = (String)sql_result.get(0)[4];
            split_skill=skills.split(";");
            for (int i=0;i<split_skill.length;i++){
                if (i==split_skill.length-1)
                    query+= "Position_Title=\""+split_skill[i]+"\"";
                else query+= "Position_Title=\""+split_skill[i]+"\" OR ";
            }
           // System.out.println(query);
        }
        catch(Exception ex){
          System.out.println("[Error] " + ex);
          return null;
        }
    
        return query;
    }
    
}
