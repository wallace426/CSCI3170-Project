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
    private Connection connection = null;
    public SQL_Connector(String address, String username, String password)
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
    public PreparedStatement Create_PS(String sql) throws SQLException
    {
        return connection.prepareStatement(sql);
    }
    public List<HashMap<String,Object>> Excute_Query(PreparedStatement ps) throws SQLException
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
}
