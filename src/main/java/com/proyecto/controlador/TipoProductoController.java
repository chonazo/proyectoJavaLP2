package com.proyecto.controlador;

import com.proyecto.modelo.TipoProducto;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoProductoController {

    public List<TipoProducto> listar() {
        String sql = "SELECT cod_tipo_prod, descrip FROM public.tipo_producto";
        
        List<TipoProducto> tipoProductos = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                TipoProducto tp = new TipoProducto();

                tp.setId(rs.getLong("cod_tipo_prod"));
                tp.setDescripcion(rs.getString("descrip"));

                tipoProductos.add(tp);
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : listar()" + e.getMessage());
        }
        return tipoProductos;
    }

    public List<TipoProducto> buscar(String buscar) {
        
        String sql = "SELECT cod_tipo_prod, descrip FROM public.tipo_producto WHERE descrip ILIKE ?";
        
        List<TipoProducto> tipoProductos = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + buscar + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TipoProducto tp = new TipoProducto();

                    tp.setId(rs.getLong("cod_tipo_prod"));
                    tp.setDescripcion(rs.getString("descrip"));

                    tipoProductos.add(tp);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : buscar()" + e.getMessage());
        }

        return tipoProductos;
    }
    
    public boolean existeTipoProducto(String nombre){
        String sql = "SELECT COUNT(*) FROM public.tipo_producto WHERE descrip = ?";
        
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    return rs.getInt(1) > 0;
                    
                }
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : existeTipoProducto()" + e.getMessage());
        }
        return false;
    }

    public boolean guardar(TipoProducto tipoProducto){
        if(existeTipoProducto(tipoProducto.getDescripcion())){
            return false;
        }
        
        String sql = "INSERT INTO public.tipo_producto(descrip) VALUES (?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipoProducto.getDescripcion());
            stmt.executeUpdate();            
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error metodo : guardar()" + e.getMessage());
            return false;
        }        
        
    }
    
    public void editar(TipoProducto tipoProducto){
        String sql = "UPDATE public.tipo_producto SET descrip=? WHERE cod_tipo_prod=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipoProducto.getDescripcion());
            stmt.setLong(2, tipoProducto.getId());
            stmt.executeUpdate();            
                        
        } catch (SQLException e) {
            System.out.println("Error metodo : editar()" + e.getMessage());
            
        }  
    }
    
    public void eliminar(Long id){
        String sql = "DELETE FROM public.tipo_producto WHERE cod_tipo_prod=?";
          try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();            
                        
        } catch (SQLException e) {
            System.out.println("Error metodo : editar()" + e.getMessage());
            
        }        
        
    }    
    
}
