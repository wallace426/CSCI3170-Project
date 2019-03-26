/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci3170_project;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author Tom
 */
public class SQL_Connector 
{
    private static Connection connection = null;
    public static void Connect(String address, String username, String password)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(address, username, password);
        }
        catch (Exception e)
        {
             System.out.println("Error");
             System.out.println(e.toString());
             System.exit(0);
        }
    }
    public static PreparedStatement Create_PS(String sql) throws SQLException
    {
        return connection.prepareStatement(sql);
    }
    //Example: result.get(0)["fieldname"]; Get the value in "fieldname" of the first result 
    public static List<HashMap<String,Object>> Excute_Query(PreparedStatement ps) throws SQLException
    {
        ResultSet results = ps.executeQuery();
        ResultSetMetaData md = results.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String,Object>> list = new ArrayList<>();
        while (results.next()) 
        {
            HashMap<String,Object> row = new HashMap<>(columns);
            for(int i=1; i<=columns; ++i) 
                row.put(md.getColumnName(i), results.getObject(i));
            list.add(row);
        }
        return list;
    }
    //Example: result[0][field index (an integer starting from 0)]; Get the value in first field of the first result 
    public static List<Object[]> Excute_Query2(PreparedStatement ps) throws SQLException
    {
        ResultSet results = ps.executeQuery();
        ResultSetMetaData md = results.getMetaData();
        int columns = md.getColumnCount();
        List<Object[]> list = new ArrayList<>();
        while (results.next()) 
        {
            Object[] row = new Object[columns];
            for(int i=1; i<=columns; ++i) 
                row[i-1] = results.getObject(i);
            list.add(row);
        }
        return list;
    }
    public static void Excute_NonReturnQuery(PreparedStatement ps) throws SQLException
    {
        ps.executeUpdate();
    }
}
