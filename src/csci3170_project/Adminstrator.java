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
public class Adminstrator 
{
    public static void DoWork()
    {
        String[] main_options = new String[] {"Create tables", "Delete tables", "Load data", "Check data", "Go back"};
        while (true)
        {
            int option = Options_Selector.Show_Options("Adminstrator, what would you like to do?", main_options);
            if (option == 1)
            {
                
            }
            else if (option == 2)
            {
                
            }
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
}
