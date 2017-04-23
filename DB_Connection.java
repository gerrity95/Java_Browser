
/**
 * Created by Mark on 17/04/2017.
 */

import java.sql.*;

public class DB_Connection {

    Browser_Methods browser_methods = new Browser_Methods();

    public void connect(Connection connection)
    {
        try {
            connection = DriverManager.
                    getConnection("jdbc:mysql://" + "ec2-54-71-144-122.us-west-2.compute.amazonaws.com" + ":" + "3306" + "/" + "javabrowser", "db_user", "aikenstreet_9941"); //User access is what is allowing access to the Database now
        } catch (SQLException e) {
            System.out.println("Connection Failed!:\n" + e.getMessage());
        }

        if (connection != null) {
            System.out.println("SUCCESS!!!! You made it, take control of your database now!");
        } else {
            System.out.println("FAILURE! Failed to make connection!");
        }

    }

    //Method for bookmarking the URL
    public void bookmarkUrl(Connection connection, String title, String url, String category)
    {

        try {
            connection = DriverManager.
                   getConnection("jdbc:mysql://" + "ec2-54-71-144-122.us-west-2.compute.amazonaws.com" + ":" + "3306" + "/" + "javabrowser", "db_user", "aikenstreet_9941"); //User access is what is allowing access to the Database now


            String insertQuery = "insert into homepage_links (title, url, category)" + " VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, url);
            preparedStatement.setString(3, category);
            //The above preparedStatement is used to assign the values in the insert query

            preparedStatement.execute();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Bookmarking error...");
            System.err.println(e.getMessage());
        }

        System.out.println("Successfully added to the database");
        System.out.println("DB connection closed.");
        browser_methods.alertBox("", "Success!", "The URL has been successfully added to your homepage");
    }
    
}


/*

<!-- Below code is just a main method used to test the database connection -->

public class DB_Main {

    static Connection connection = null;
    static DB_Connection db = new DB_Connection();

    public static void main(String[] args)
    {

        System.out.println("----MySQL JDBC Connection Testing -------");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");

        db.connect(connection);

    }


}

*/