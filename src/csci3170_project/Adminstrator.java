/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci3170_project;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            {
                
            }
            else if (option == 4)
            {
                
            }
            else if (option == 5)
            {
                break;
            }
        }
    }
    private static void Create_Tables()
    {
        String query = "CREATE TABLE Employee( Employee_ID varchar(6) primary key, Name varchar(30) not null, Expected_Salary integer, Experience integer, Skills varchar(50) not null);";
        try 
        {
            PreparedStatement ps = SQL_Connector.Create_PS(query);
            SQL_Connector.Excute_NonReturnQuery(ps);
        } 
        catch (SQLException ex) {
            System.out.println("[Error] " + ex);
        }
    }
    private static void Delete_Tables()
    {
        String query = "DROP TABLE Employee;";
        try 
        {
            PreparedStatement ps = SQL_Connector.Create_PS(query);
            SQL_Connector.Excute_NonReturnQuery(ps);
        } 
        catch (SQLException ex) {
            System.out.println("[Error] " + ex);
        }
    }
}
