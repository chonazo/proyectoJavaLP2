package com.proyecto.controlador;

import com.proyecto.modelo.Deposito;
import com.proyecto.modelo.DetalleVenta;
import com.proyecto.modelo.Producto;
import com.proyecto.modelo.Venta;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaController {

    public List<DetalleVenta> listarPorVenta(Long codVenta) {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT dv.cod_venta, dv.cod_producto, dv.cod_deposito, dv.det_precio_unit, dv.det_cantidad, "
                + "p.descrip AS pro_descri, p.cod_barra, d.descrip AS depo_descri FROM det_venta dv JOIN producto p "
                + "ON p.cod_producto = dv.cod_producto JOIN deposito d ON d.cod_deposito = dv.cod_deposito WHERE "
                + "dv.cod_venta = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, codVenta);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    DetalleVenta d = getDetalleVenta(codVenta, rs);
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error listarPorVenta(): " + e.getMessage());
        }

        return lista;
    }

    public void guardar(DetalleVenta d) {
        String sql = "INSERT INTO det_venta (cod_venta, cod_producto, cod_deposito, det_precio_unit, det_cantidad) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, d.getVenta().getId());
            stmt.setLong(2, d.getProducto().getId());
            stmt.setLong(3, d.getDeposito().getId());
            stmt.setInt(4, d.getPrecioUnitario());
            stmt.setInt(5, d.getCantidad());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en guardar(): " + e.getMessage());
        }
    }

    public void eliminarPorVenta(Long codVenta) {
        String sql = "DELETE FROM det_venta WHERE cod_venta = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, codVenta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error eliminarPorVenta(): " + e.getMessage());
        }
    }

    public DetalleVenta getDetalleVenta(Long codVenta, final ResultSet rs) throws SQLException {
        DetalleVenta d = new DetalleVenta();
        Venta v = new Venta();
        v.setId(codVenta);
        d.setVenta(v);
        Producto p = new Producto();
        p.setId(rs.getLong("cod_producto"));
        p.setNombreProducto(rs.getString("pro_descri"));
        p.setCodBarra(rs.getString("cod_barra"));
        d.setProducto(p);
        Deposito dep = new Deposito();
        dep.setId(rs.getLong("cod_deposito"));
        dep.setDescriDeposito(rs.getString("depo_descri"));
        d.setDeposito(dep);
        d.setCantidad(rs.getInt("det_cantidad"));
        d.setPrecioUnitario(rs.getInt("det_precio_unit"));
        return d;
    }

}
