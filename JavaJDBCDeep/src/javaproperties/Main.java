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
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CodeFireUA <edu@codefire.com.ua>
 */
public class Main {

    static {
        try {
            Class.forName(com.mysql.jdbc.Driver.class.getName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Properties props = loadProperties();

        String hostname = String.format("%s:%s", props.getProperty("db.host.name"),
                props.getProperty("db.host.port"));

        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        DAO dao = new DAO(hostname, username, password);

        List<String> databaseList = null;

        System.out.println("Databases:");
        try {
            databaseList = dao.getDatabaseList();

            int i = 0;
            for (String database : databaseList) {
                System.out.printf(" %3d. %s\n", ++i, database);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (databaseList == null) {
            System.err.println("Not found databases!");
            return;
        }

        int choose;

        while (true) {
            System.out.printf("Make your choice (1-%d):\n", databaseList.size());

            try {
                String input = scanner.nextLine();
                choose = Integer.parseInt(input);

                if (choose > 0 && choose <= databaseList.size()) {
                    break;
                }

                System.out.println("Wrong choose!");
            } catch (NumberFormatException ex) {
                System.out.println("Wrong choose!");
            }
        }

        String databaseName = databaseList.get(choose - 1);

        List<String> tableList = null;

        try {
            tableList = dao.getTableList(databaseName);

            int i = 0;
            for (String database : tableList) {
                System.out.printf(" %3d. %s\n", ++i, database);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (tableList == null) {
            System.out.println("THIS DATABASE IS EMPTY! SELECT ANOTHER DATABASE");
            return;
        }

        while (true) {
            System.out.printf("Make your choice (1-%d):\n", tableList.size());

            try {
                String input = scanner.nextLine();
                choose = Integer.parseInt(input);

                if (choose > 0 && choose <= tableList.size()) {
                    break;
                }

                System.out.println("Wrong choose!");
            } catch (NumberFormatException ex) {
                System.out.println("Wrong choose!");
            }
        }

        String tableName = tableList.get(choose - 1);
        
        try {
            dao.printDatabaseTable(databaseName, tableName);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            props.load(fis);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return props;
    }

}
