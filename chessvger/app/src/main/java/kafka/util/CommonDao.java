package kafka.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class CommonDao {
  private static CommonDao _instance=null;
  private Connection connection;
  public static CommonDao getInstance(){
    if (_instance==null){
      _instance=new CommonDao();
    }
    return _instance;
  }
  private CommonDao(){
    try {
      Class.forName("org.postgresql.Driver");
       connection =
          DriverManager.getConnection("jdbc:postgresql://localhost/chessvger", "chessvger",
              "chessvger");
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
  public  Connection getConnection(){
return connection;
  }
}
