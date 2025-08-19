package com.proyecto.controlador;

import com.proyecto.modelo.Proveedor;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProveedorController {

    public List<Proveedor> listar() {

        String sql = "SELECT cod_proveedor, razon_social, ruc, direccion FROM public.proveedor";

        List<Proveedor> proveedores = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Proveedor p = getProveedor(rs);
                proveedores.add(p);
            }

        } catch (SQLException e) {

            System.out.println("Error Metodo: listar()" + e.getMessage());
        }

        return proveedores;
    }

    public List<Proveedor> buscar(String criterio) {

        String sql = "SELECT cod_proveedor, razon_social, ruc, direccion "
                + "FROM public.proveedor WHERE razon_social ILIKE ? OR ruc ILIKE ?";

        List<Proveedor> proveedores = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String parametroBusqueda = "%" + criterio + "%";
            stmt.setString(1, parametroBusqueda);
            stmt.setString(2, parametroBusqueda);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Proveedor p = getProveedor(rs);
                    proveedores.add(p);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: buscar()" + e.getMessage());
        }

        return proveedores;
    }

    public boolean existeRuc(String ruc) {
        String sql = "SELECT COUNT(*) FROM public.proveedor WHERE ruc = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ruc);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: existeRuc()" + e.getMessage());
        }

        return false;
    }

    public boolean guardar(Proveedor proveedor) {
        if (existeRuc(proveedor.getRuc())) {
            return false;
        }

        String sql = "INSERT INTO public.proveedor(razon_social, ruc, direccion) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getRazonSocial());
            stmt.setString(2, proveedor.getRuc());
            stmt.setString(3, proveedor.getDireccion());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error Metodo: guardar()" + e.getMessage());
            return false;
        }
    }

    public void editar(Proveedor proveedor) {

        String sql = "UPDATE public.proveedor SET razon_social=?, ruc=?, direccion=? WHERE cod_proveedor=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proveedor.getRazonSocial());
            stmt.setString(2, proveedor.getRuc());
            stmt.setString(3, proveedor.getDireccion());
            stmt.setLong(4, proveedor.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error Metodo: editar()" + e.getMessage());
        }

    }

    public void eliminar(Long id) {
        String sql = "DELETE FROM public.proveedor WHERE cod_proveedor=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error Metodo: eliminar()" + e.getMessage());
        }

    }

    public Proveedor getProveedor(final ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setId(rs.getLong("cod_proveedor"));
        p.setRazonSocial(rs.getString("razon_social"));
        p.setRuc(rs.getString("ruc"));
        p.setDireccion(rs.getString("direccion"));
        return p;
    }
}
