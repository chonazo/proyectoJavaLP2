package com.proyecto.controlador;

import com.proyecto.modelo.Deposito;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepositoController {

    public List<Deposito> listar() {
        String sql = "SELECT cod_deposito, descrip FROM public.deposito";
        List<Deposito> depositos = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Deposito d = new Deposito();
                d.setId(rs.getLong("cod_deposito"));
                d.setDescriDeposito(rs.getString("descrip"));

                depositos.add(d);

            }
        } catch (SQLException e) {
            System.out.println("Error metodo : listar()" + e.getMessage());
        }

        return depositos;
    }

    public List<Deposito> buscar(String buscar) {
        String sql = "SELECT cod_deposito, descrip FROM public.deposito WHERE descrip ILIKE ?";
        List<Deposito> depositos = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + buscar + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Deposito d = new Deposito();
                    d.setId(rs.getLong("cod_deposito"));
                    d.setDescriDeposito(rs.getString("descrip"));

                    depositos.add(d);

                }
            }
        } catch (SQLException e) {
            System.out.println("Error metodo : buscar()" + e.getMessage());
        }

        return depositos;
    }

    public boolean existeDeposito(String nombre) {
        String sql = "SELECT COUNT(*) FROM public.deposito WHERE descrip = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : existeDeposito()" + e.getMessage());
        }

        return false;
    }

    public boolean guardar(Deposito deposito) {
        if (existeDeposito(deposito.getDescriDeposito())) {
            return false;
        }

        String sql = "INSERT INTO public.deposito(descrip) VALUES (?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, deposito.getDescriDeposito());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error metodo : guardar()" + e.getMessage());
            return false;
        }

    }

    public void editar(Deposito deposito) {
        String sql = "UPDATE public.deposito SET descrip=? WHERE cod_deposito=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, deposito.getDescriDeposito());
            stmt.setLong(2, deposito.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error metodo : editar()" + e.getMessage());

        }
    }

    public void eliminar(Long id) {
        String sql = "DELETE FROM public.deposito WHERE cod_deposito=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error metodo : editar()" + e.getMessage());

        }
    }

}
