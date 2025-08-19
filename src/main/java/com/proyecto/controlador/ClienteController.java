package com.proyecto.controlador;

import com.proyecto.modelo.Ciudad;
import com.proyecto.modelo.Cliente;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {

    public List<Cliente> listar() {
        String sql = "SELECT c.id_cliente, c.cod_ciudad, ci.descrip_ciudad, c.ci_ruc, c.cli_nombre, c.cli_apellido, c.cli_direccion, c.cli_telefono "
                + "FROM public.clientes as c INNER JOIN public.ciudad as ci ON (c.cod_ciudad = ci.cod_ciudad)";
        
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cl = getClientes(rs);
                clientes.add(cl);
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }

        return clientes;

    }

    public List<Cliente> buscar(String rucCi) {
        String sql = "SELECT c.id_cliente, c.cod_ciudad, ci.descrip_ciudad, c.ci_ruc, c.cli_nombre, c.cli_apellido, c.cli_direccion, c.cli_telefono "
                + "FROM public.clientes as c INNER JOIN public.ciudad as ci "
                + "ON (c.cod_ciudad = ci.cod_ciudad) WHERE ci_ruc ILIKE ?";

        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + rucCi + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cl = getClientes(rs);
                    clientes.add(cl);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: buscar()" + e.getMessage());
        }

        return clientes;
    }

    public boolean existeRucCi(String rucCi) {
        
        String sql = "SELECT count(*) FROM public.clientes WHERE UPPER(ci_ruc)=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rucCi);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: existeRucCi()" + e.getMessage());
        }
        return false;
    }
    
    public boolean guardar(Cliente cliente){
        if(existeRucCi(cliente.getRucCI())){
            return false;
        }
        
        String sql = "INSERT INTO public.clientes"
                + "(cod_ciudad, ci_ruc, cli_nombre, cli_apellido, cli_direccion, cli_telefono) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try(Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setLong(1, cliente.getCiudad().getId());
            stmt.setString(2, cliente.getRucCI());
            stmt.setString(3, cliente.getNombreCli());
            stmt.setString(4, cliente.getApellidoCli());
            stmt.setString(5, cliente.getDireccionCli());
            stmt.setString(6, cliente.getTelefonoCli());
            
            stmt.executeUpdate();
            return true;
            
        }catch(SQLException e){
            System.out.println("Error Metodo: guardar()" + e.getMessage());
            return false;
        }
    }
    
    public void editar(Cliente cliente){
        String sql = "UPDATE public.clientes SET cod_ciudad=?, ci_ruc=?, cli_nombre=?, cli_apellido=?, cli_direccion=?, cli_telefono=? "
                + "WHERE id_cliente = ?";
        
        try(Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setLong(1, cliente.getCiudad().getId());
            stmt.setString(2, cliente.getRucCI());
            stmt.setString(3, cliente.getNombreCli());
            stmt.setString(4, cliente.getApellidoCli());
            stmt.setString(5, cliente.getDireccionCli());
            stmt.setString(6, cliente.getTelefonoCli());
            stmt.setLong(7, cliente.getId());
            
            stmt.executeUpdate();
                        
        }catch(SQLException e){
            System.out.println("Error Metodo: editar()" + e.getMessage());            
        }
    }
    
    public void eliminar(Long id){
        String sql = "DELETE FROM public.clientes WHERE id_cliente=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setLong(1, id);
          stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error Metodo: eliminar()" + e.getMessage()); 
        }
    }

    public Cliente getClientes(final ResultSet rs) throws SQLException {
        Cliente cl = new Cliente();
        cl.setId(rs.getLong("id_cliente"));
        Ciudad c = new Ciudad();
        c.setId(rs.getLong("cod_ciudad"));
        c.setNombreCiudad(rs.getString("descrip_ciudad"));
        cl.setCiudad(c);
        cl.setRucCI(rs.getString("ci_ruc"));
        cl.setNombreCli(rs.getString("cli_nombre"));
        cl.setApellidoCli(rs.getString("cli_apellido"));
        cl.setDireccionCli(rs.getString("cli_direccion"));
        cl.setTelefonoCli(rs.getString("cli_telefono"));
        return cl;
    }

}
