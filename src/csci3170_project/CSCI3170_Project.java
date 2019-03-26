/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci3170_project;
/**
 *
 * @author Tom
 */
public class CSCI3170_Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        SQL_Connector.Connect("jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db40?allowMultiQueries=true", "Group40", "CSCI3170");
        String[] main_options = new String[] {"An adminstrator", "An employee", "An employer", "Exit"};
        while (true)
        {
            int option = Options_Selector.Show_Options("Welcome! Who are you?", main_options);
            if (option == 1)
                Adminstrator.DoWork();
            else if (option == 2)
            {
                Employee.DoWork();
            }
            else if (option == 3)
            {
                
            }
            else if (option == 4)
            {
                break;
            }
        }
    }
    
}
