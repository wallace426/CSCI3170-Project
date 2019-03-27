/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci3170_project;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Tom
 */
public class Employer 
{
    public static void DoWork()
    {
        String[] main_options = new String[] {"Post Position Recruitment", "Check employees and arrange an interview", "Accept an employee", "Go back"};
        while (true)
        {
            int option = Options_Selector.Show_Options("Employer, what would you like to do?", main_options);
            if (option == 1)
                Post_Recruitment();
            else if (option == 2)
                Arrange_Interview();
            else if (option == 3)
            {}
            else if (option == 4)
                break;
        }
    }
    private static void Arrange_Interview()
    {
        String employer_id = "";
        List<String> list_positionID = new ArrayList<>();
        try
        {
            Scanner scan = new Scanner(System.in);
            System.out.println("Please enter your ID.");
            employer_id = scan.nextLine();
        }
        catch (Exception ex)
        {
            System.out.println("[ERROR] Input Error");
            return;
        }
        try
        {
            PreparedStatement ps = SQL_Connector.Create_PS("SELECT COUNT(*) FROM Employer WHERE Employer_ID = ?;");
            ps.setString(1, employer_id);
            List<Object[]> sql_result = SQL_Connector.Excute_Query2(ps);
            if ((long)sql_result.get(0)[0] == 0)
            {
                System.out.println("[Error] Employer ID does not exist!");
                return;
            }
            ps = SQL_Connector.Create_PS("SELECT Position_ID FROM Position WHERE Employer_ID = ? and Status = TRUE;");
            ps.setString(1, employer_id);
            sql_result = SQL_Connector.Excute_Query2(ps);
            if (sql_result.size() == 0)
            {
                System.out.println("[Error] No available recuriment position.");
                return;
            }
            for (Object[] obj : sql_result)
                list_positionID.add((String)obj[0]);
        }
        catch (Exception ex)
        {
            System.out.println("[Error] " + ex);
            return;
        }
        String selected_positionID = "";
        try
        {
            Scanner scan = new Scanner(System.in);
            System.out.println("Please pick one position ID.");
            selected_positionID = scan.nextLine();
            if (!list_positionID.contains(selected_positionID))
                throw new Exception();
        }
        catch (Exception ex)
        {
            System.out.println("[Error] Input Error" );
            return;
        }
        List<String> list_empolyeeID = new ArrayList<>();
        try
        {
            PreparedStatement ps = SQL_Connector.Create_PS("SELECT * FROM Employee NATURAL JOIN ( SELECT Employee_ID FROM marked WHERE Position_ID = ? AND Status = FALSE) AS T;");
            ps.setString(1, selected_positionID);
            List<HashMap<String,Object>> sql_result = SQL_Connector.Excute_Query(ps);
            if (sql_result.size() == 0)
            {
                System.out.println("[Error] No employee has marked this position as interested.");
                return;
            }
            System.out.println("Employee_ID,\tName,\tExpected_Salary,\tExperience,\tSkills");
            for (HashMap<String, Object> obj : sql_result)
            {
                list_empolyeeID.add((String) obj.get("Employee_ID"));
                System.out.println((String) obj.get("Employee_ID") + ",\t" + (String)obj.get("Name") + ",\t" + (String)obj.get("tExpected_Salary") + ",\t" + (String)obj.get("Experience") + ",\t" + (String)obj.get("Skills"));
            }
        }
        catch (Exception ex)
        {
            System.out.println("[Error] " + ex);
            return;
        }
        String select_employeeID = "";
        try
        {
            Scanner scan = new Scanner(System.in);
            System.out.println("Please pick one employee by Employee_ID.");
            select_employeeID = scan.nextLine();
            if (!list_empolyeeID.contains(select_employeeID))
                throw new Exception();
        }
        catch (Exception ex)
        {
            System.out.println("[Error] Input Error" );
            return;
        }
        try
        {
            PreparedStatement ps = SQL_Connector.Create_PS("UPDATE marked SET Status=TRUE WHERE Position_ID = ? AND Employee_ID = ?;");
            ps.setString(1, selected_positionID);
            ps.setString(2, select_employeeID);
            SQL_Connector.Excute_NonReturnQuery(ps);
        }
        catch (Exception ex)
        {
            System.out.println("[Error] " + ex);
            return;
        }
        System.out.println("An IMMEDIATE interview has done!");
    }
    private static void Post_Recruitment()
    {
        int salary, experience;
        String position_title, employer_id;
        try
        {
            Scanner scan = new Scanner(System.in);
            System.out.println("Please enter your ID.");
            employer_id = scan.nextLine();
            System.out.println("Please enter the position title.");
            position_title = scan.nextLine();
            System.out.println("Please enter an upper bound of salary.");
            salary = scan.nextInt();
            scan.nextLine();
            System.out.println("Please enter the required experience (press enter to skip).");
            String experience_tmp = scan.nextLine();
            if ("".equals(experience_tmp))
                experience = 0;
            else
                experience = Integer.parseInt(experience_tmp);
        }
        catch (Exception ex)
        {
            System.out.println("[Error] Input error!");
            return;
        }
        try
        {
            //Verifying Employer ID
            PreparedStatement ps = SQL_Connector.Create_PS("SELECT COUNT(*) FROM Employer WHERE Employer_ID = ?;");
            ps.setString(1, employer_id);
            List<Object[]> sql_result = SQL_Connector.Excute_Query2(ps);
            if ((long)sql_result.get(0)[0] == 0)
            {
                System.out.println("[Error] Employer ID does not exist!");
                return;
            }
            //Counting potential employee
            ps = SQL_Connector.Create_PS("SELECT COUNT(DISTINCT Employee_ID) FROM Employee NATURAL JOIN ( SELECT Employee_ID FROM Employment_History b WHERE b.Employee_ID NOT IN (SELECT Employee_ID FROM Employment_History a WHERE a.END IS NULL)) AS T WHERE Skills LIKE ? and Experience >= ? and Expected_Salary <= ?;");
            ps.setString(1, "%"+position_title+"%");
            ps.setInt(2, experience);
            ps.setInt(3, salary);
            sql_result = SQL_Connector.Excute_Query2(ps);
            long potential_count = (long)sql_result.get(0)[0];
            if (potential_count == 0)
            {
                System.out.println("[Error] No potential employee!");
                return;
            }
            String ID = Generate_PositionID();
            //Position_ID, Position_Title, Salary, Experience, Status, Employer_ID
            ps = SQL_Connector.Create_PS("INSERT INTO Position VALUES (?, ?, ?, ?, ?, ?);");
            ps.setString(1, ID);
            ps.setString(2, position_title);
            ps.setInt(3, salary);
            ps.setInt(4, experience);
            ps.setBoolean(5, true);
            ps.setString(6, employer_id);
            SQL_Connector.Excute_NonReturnQuery(ps);
            System.out.println(potential_count + " potential employees are found. The position recruitment is posted.");
        }
        catch (Exception ex)
        {
            System.out.println("[Error] " + ex );
            return;
        }
    }
    private static String Generate_PositionID() throws SQLException
    {
        Random rand = new Random();
        String ID = "";
        while (true)
        {
            ID = "pid";
            ID += (char)('a'+rand.nextInt(26));
            ID += (char)('a'+rand.nextInt(26));
            ID += (char)('a'+rand.nextInt(26));
            PreparedStatement ps = SQL_Connector.Create_PS("SELECT COUNT(*) FROM Position WHERE Position_ID = ?;");
            ps.setString(1, ID);
            List<Object[]> sql_result = SQL_Connector.Excute_Query2(ps);
            if ((long)sql_result.get(0)[0] == 0)
                break;
        }
        return ID;
    }
    
}
