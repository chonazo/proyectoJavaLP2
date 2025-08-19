package com.proyecto.controlador;

import com.proyecto.modelo.Compra;
import com.proyecto.modelo.Deposito;
import com.proyecto.modelo.DetalleCompra;
import com.proyecto.modelo.Producto;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DetalleCompraController {

    public List<DetalleCompra> vistaDetalleCompraAnular() {
        String sql = "SELECT dc.cod_producto, p.descrip AS producto, dc.cantidad, dc.precio, d.descrip AS deposito, "
                + "dc.cod_compra, co.nro_factura FROM detalle_compra dc JOIN producto p ON p.cod_producto = "
                + "dc.cod_producto JOIN deposito d ON d.cod_deposito = dc.cod_deposito JOIN compra AS co "
                + "ON dc.cod_compra = co.cod_compra ORDER BY co.cod_compra DESC";

        List<DetalleCompra> dCompras = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DetalleCompra dc = getDetallesCompras(rs);
                dCompras.add(dc);
            }
        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }
        return dCompras;
    }

    public List<DetalleCompra> buscarPorNroFactura(String nroFactura) {
        String sql = "SELECT dc.cod_producto, p.descrip AS producto, dc.cantidad, dc.precio, d.descrip AS deposito, "
                + "dc.cod_compra, co.nro_factura FROM detalle_compra dc JOIN producto p ON p.cod_producto = "
                + "dc.cod_producto JOIN deposito d ON d.cod_deposito = dc.cod_deposito JOIN compra AS co "
                + "ON dc.cod_compra = co.cod_compra WHERE co.nro_factura ILIKE ?";

        List<DetalleCompra> dCompras = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nroFactura + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetalleCompra dc = getDetallesCompras(rs);
                    dCompras.add(dc);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error Metodo: buscarPorNroFactura() " + e.getMessage());
        }
        return dCompras;
    }

    public void guardar(DetalleCompra detalleCompra) {
        String sql = "INSERT INTO public.detalle_compra(cod_producto, cod_compra, cod_deposito, precio, cantidad) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, detalleCompra.getProducto().getId());
            stmt.setLong(2, detalleCompra.getCompra().getId());
            stmt.setLong(3, detalleCompra.getDeposito().getId());
            stmt.setInt(4, detalleCompra.getPrecio());
            stmt.setInt(5, detalleCompra.getCantidad());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error Metodo: guardar()" + e.getMessage());
        }
    }

    public List<DetalleCompra> obtenerDetallesPorCompra(Long codCompra) {
        String sql = "SELECT dc.cod_producto, p.descrip AS producto, dc.cantidad, dc.precio, "
                + "d.descrip AS deposito, dc.cod_compra, co.nro_factura, d.cod_deposito "
                + "FROM detalle_compra dc "
                + "JOIN producto p ON p.cod_producto = dc.cod_producto "
                + "JOIN deposito d ON d.cod_deposito = dc.cod_deposito "
                + "JOIN compra co ON co.cod_compra = dc.cod_compra "
                + "WHERE dc.cod_compra = ?";

        List<DetalleCompra> detalles = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, codCompra);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetalleCompra dc = getDetallesCompras(rs);

                    // Agregar ID del depósito (lo necesitaremos para stock)
                    Deposito d = new Deposito();
                    d.setId(rs.getLong("cod_deposito"));
                    d.setDescriDeposito(rs.getString("deposito"));
                    dc.setDeposito(d);

                    detalles.add(dc);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error obtenerDetallesPorCompra(): " + e.getMessage());
        }

        return detalles;
    }

    public DetalleCompra getDetallesCompras(final ResultSet rs) throws SQLException {
        DetalleCompra dc = new DetalleCompra();

        Producto p = new Producto();
        p.setId(rs.getLong("cod_producto"));
        p.setNombreProducto(rs.getString("producto"));
        dc.setProducto(p);

        Compra c = new Compra();
        c.setId(rs.getLong("cod_compra"));
        c.setNumFactura(rs.getString("nro_factura"));
        dc.setCompra(c);

        Deposito d = new Deposito();
        d.setDescriDeposito(rs.getString("deposito"));
        dc.setDeposito(d);

        dc.setPrecio(rs.getInt("precio"));
        dc.setCantidad(rs.getInt("cantidad"));
        //dc.setSubtotal(rs.getInt("subtotal")); // <-- campo calculado

        return dc;
    }

}
