package com.proyecto.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static String url = "jdbc:postgresql://localhost:5432/proyecto";
    private static String username = "postgres";
    private static String password = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /*public static void main(String[] args) {
        try {
            Connection conn = Conexion.getConnection();
            System.out.println("Conexion exitosa a la base de datos PostgreSQL");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos");
            e.printStackTrace();

        }
    }*/

}
