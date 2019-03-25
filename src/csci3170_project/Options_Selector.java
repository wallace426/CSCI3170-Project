/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci3170_project;

import java.util.Scanner;

/**
 *
 * @author Tom
 */
public class Options_Selector 
{
    public static int Show_Options(String title, String[] options)
    {  
        Scanner scan = new Scanner(System.in);
        int selected = 0;  
        int option_count = 0; 
        System.out.println(title);
        selected = 0;  
        option_count = 0;
        for (String option : options)
            System.out.println((++option_count) + ". " + option);
        while (true)
        {
            System.out.println("Please enter [1-"+option_count+"].");
            if (!scan.hasNextInt())
            {
                System.out.println("[ERROR] Invalid input.");
                scan.nextLine();
                continue;
            }
            selected = scan.nextInt();
            if (selected < 1 || selected > option_count)
                System.out.println("[ERROR] Invalid input.");
            else
                break;
        }
        return selected;
    }
}
