package com.proyecto.controlador;

import com.proyecto.modelo.UMedida;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UMedidaController {

    public List<UMedida> listar() {
        String sql = "SELECT id_u_medida, u_descrip FROM public.u_medida";

        List<UMedida> uMedidas = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UMedida u = new UMedida();
                u.setId(rs.getLong("id_u_medida"));
                u.setDescripcion(rs.getString("u_descrip"));

                uMedidas.add(u);
            }

        } catch (Exception e) {
            System.out.println("Error metodo : listar()" + e.getMessage());
        }

        return uMedidas;
    }

    public List<UMedida> buscar(String buscar) {
        String sql = "SELECT id_u_medida, u_descrip FROM public.u_medida WHERE u_descrip ILIKE ?";
        List<UMedida> uMedidas = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + buscar + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UMedida u = new UMedida();
                    u.setId(rs.getLong("id_u_medida"));
                    u.setDescripcion(rs.getString("u_descrip"));

                    uMedidas.add(u);
                }
            }

        } catch (Exception e) {
            System.out.println("Error metodo : buscar()" + e.getMessage());
        }

        return uMedidas;
    }

    public boolean existeUMedida(String uMedida) {
        String sql = "SELECT COUNT(*) FROM public.u_medida WHERE u_descrip = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uMedida);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error metodo : existeUMedida()" + e.getMessage());
        }
        return false;
    }

    public boolean guardar(UMedida uMedida) {

        if (existeUMedida(uMedida.getDescripcion())) {
            return false;
        }

        String sql = "INSERT INTO public.u_medida(u_descrip) VALUES (?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uMedida.getDescripcion());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error metodo : guardar()" + e.getMessage());
            return false;
        }

    }

    public void editar(UMedida uMedida) {
        String sql = "UPDATE public.u_medida SET u_descrip=? WHERE id_u_medida=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uMedida.getDescripcion());
            stmt.setLong(2, uMedida.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error metodo : editar()" + e.getMessage());

        }

    }
    
    public void eliminar(Long id){
        
        String sql = "DELETE FROM public.u_medida WHERE id_u_medida = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error metodo : eliminar()" + e.getMessage());

        } 
    }

}
