package lab.connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Connections {
    static Connection connection;
    public static Connection getConnection()
    {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url="jdbc:oracle:thin:@10.1.50.198:1535/nsbt19c";
            String user="ncspdb";
            String password="ncspdb";
            connection= DriverManager.getConnection(url,user,password);
            System.out.println("Successful connection");
        }
        catch(ClassNotFoundException c) {
            System.out.println(c);
        }
        catch (SQLException s){
            System.out.println(s);
        }
        return connection;
    }
}
