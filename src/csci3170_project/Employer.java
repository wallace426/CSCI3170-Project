/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci3170_project;

import java.sql.PreparedStatement;
import java.util.List;
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
            {}
            else if (option == 3)
            {}
            else if (option == 4)
                break;
        }
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
            
        }
        catch (Exception ex)
        {
            System.out.println("[Error] " + ex );
            return;
        }
    }
}
