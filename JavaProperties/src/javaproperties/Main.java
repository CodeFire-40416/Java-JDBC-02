/*
 * Copyright (C) 2016 CodeFireUA <edu@codefire.com.ua>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package javaproperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CodeFireUA <edu@codefire.com.ua>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            Class.forName(com.mysql.jdbc.Driver.class.getName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        Properties props = new Properties();
        
        try (FileInputStream fis  = new FileInputStream("db.properties")) {
            props.load(fis);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        String connectionString = String.format("jdbc:mysql://%s:%s/%s",
                props.getProperty("db.host.name"),
                props.getProperty("db.host.port"),
                props.getProperty("db.database")
        );

        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(connectionString, username, password)) {

            try (ResultSet rs = conn.createStatement().executeQuery("SHOW DATABASES")) {
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

//        props.put("db.host.name", "127.0.0.1");
//        props.put("db.host.port", "3306");
//        props.put("db.database", "test");
//        props.put("db.username", "student");
//        props.put("db.password", "12345");
//        try (FileOutputStream fos = new FileOutputStream("db.properties")) {
//            props.store(fos, null);
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
