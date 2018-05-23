package org.sterl.education.jobs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcHelper {

    public static void printQueryResult(String query, Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();) {
            ResultSet executeQuery = statement.executeQuery(query);
            
            final int columnCount = executeQuery.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; ++i) {
                System.out.print(executeQuery.getMetaData().getColumnName(i));
                System.out.print('\t');
            }
            System.out.println();
            while(executeQuery.next()) {
                for (int i = 1; i <= columnCount; ++i) {
                    System.out.print(executeQuery.getObject(i));
                    System.out.print('\t');
                }
                System.out.println();
            }
            executeQuery.close();
        }
    }
}
