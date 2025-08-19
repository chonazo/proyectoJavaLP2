package com.proyecto.controlador;

import com.proyecto.modelo.Venta;
import com.proyecto.modelo.VentaPago;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VentaPagoController {

    public List<VentaPago> listarPorVenta() {

        String sql = "SELECT id_v_pago, cod_venta, forma_pago, monto, descripcion, num_tarjeta FROM public.venta_pago";

        List<VentaPago> pagos = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                VentaPago ventaPago = getVentaPago(rs);
                pagos.add(ventaPago);

            }
        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }

        return pagos;

    }

    public VentaPago guardarVentaPago(VentaPago vp) {
        String sql = "INSERT INTO public.venta_pago(cod_venta, forma_pago, monto, descripcion, num_tarjeta) "
                + "VALUES (?, ?, ?, ?, ?) RETURNING id_v_pago";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, vp.getVenta().getId());
            stmt.setString(2, vp.getFormaPago());
            stmt.setInt(3, vp.getMonto());
            stmt.setString(4, vp.getDescripcion());
            stmt.setString(5, vp.getNumeroTarjeta());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Long idGenerado = rs.getLong(1);
                vp.setId(idGenerado);
                return vp;
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: guardarVentaPago()" + e.getMessage());
        }
        return null;
    }

    public void eliminarPorId(Long id) {

        String sql = "DELETE FROM public.venta_pago WHERE id_v_pago = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Metodo: eliminarProId()" + e.getMessage());
        }

    }

    public int sumarPagosPorVenta(Long codVenta) {
        String sql = "SELECT SUM(monto) FROM public.venta_pago WHERE cod_venta = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, codVenta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: eliminarProId()" + e.getMessage());
        }
        return 0;
    }

    public VentaPago getVentaPago(final ResultSet rs) throws SQLException {
        VentaPago ventaPago = new VentaPago();

        ventaPago.setId(rs.getLong("id_v_pago"));

        Venta v = new Venta();
        v.setId(rs.getLong("cod_venta"));
        ventaPago.setVenta(v);

        ventaPago.setFormaPago(rs.getString("forma_pago"));
        ventaPago.setMonto(rs.getInt("monto"));
        ventaPago.setDescripcion(rs.getString("descripcion"));
        ventaPago.setNumeroTarjeta(rs.getString("num_tarjeta"));
        return ventaPago;
    }

}
