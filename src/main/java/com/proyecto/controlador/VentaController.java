package com.proyecto.controlador;

import com.proyecto.modelo.Cliente;
import com.proyecto.modelo.Usuario;
import com.proyecto.modelo.Venta;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VentaController {

    public List<Venta> listar() {
        String sql = "SELECT v.cod_venta, v.id_cliente, c.ci_ruc, c.cli_nombre, c.cli_apellido, "
                + "v.usu_cod, usu_nick, v.fecha, "
                + "v.total, v.importe, v.estado, v.num_factura, v.descuentos FROM public.venta AS v INNER JOIN public.clientes "
                + "AS c ON (v.id_cliente = c.id_cliente) "
                + "INNER JOIN public.usuario AS u ON (v.usu_cod = u.usu_cod)";

        List<Venta> ventas = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Venta v = getVentas(rs);
                ventas.add(v);

            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }
        return ventas;
    }

    public Venta buscarPorId(Long id) {
        String sql = "SELECT v.cod_venta, v.id_cliente, c.ci_ruc, c.cli_nombre, c.cli_apellido, "
                + "v.usu_cod, u.usu_nick, v.fecha, v.total, v.importe, v.estado, v.num_factura, v.descuentos "
                + "FROM public.venta AS v "
                + "INNER JOIN public.clientes AS c ON v.id_cliente = c.id_cliente "
                + "INNER JOIN public.usuario AS u ON v.usu_cod = u.usu_cod "
                + "WHERE v.cod_venta = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return getVentas(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error Metodo: buscarPorId() - " + e.getMessage());
        }
        return null;
    }

    public Venta guardar(Venta venta) {
        String sql = "INSERT INTO public.venta(id_cliente, usu_cod, fecha, total, importe, estado, num_factura, descuentos) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING cod_venta";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, venta.getCliente().getId());
            stmt.setLong(2, venta.getUsuario().getId());
            stmt.setDate(3, venta.getFecha());
            stmt.setInt(4, venta.getTotal());
            stmt.setInt(5, venta.getImporte());
            stmt.setString(6, venta.getEstado());
            stmt.setString(7, venta.getNumFactura());
            stmt.setInt(8, venta.getDescuento());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Long idGenerado = rs.getLong(1);
                venta.setId(idGenerado);
                return venta;
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: guardar()" + e.getMessage());
        }
        return null;
    }

    public String generarNumeroFactura() {
        String prefijo = "001-001-";
        int siguienteNumero = 1;//si no hay factura inicia con esta variable 
        String sql = "SELECT num_factura FROM public.venta WHERE num_factura LIKE ? ORDER BY num_factura DESC LIMIT 1";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prefijo + "%");

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String ultimoNroFactura = rs.getString("nro_factura");//ultimo numero factura 
                String numeroStr = ultimoNroFactura.substring(prefijo.length()); //obtenemos ultima parte 

                try {
                    siguienteNumero = Integer.parseInt(numeroStr) + 1; // la ultima parte lo convertimos integer + 1
                } catch (NumberFormatException e) {
                    siguienteNumero = 1; // si hay error dejamos en uno
                }
            }
        } catch (SQLException e) {
            System.out.println("Error metodo : generarNumeroFactura()" + e.getMessage());
        }
        return prefijo + String.format("%04d", siguienteNumero); // concatenamos a prefijo siguiente numero (String)
    }

    public Venta getVentas(final ResultSet rs) throws SQLException {
        Venta v = new Venta();
        
        v.setId(rs.getLong("cod_venta"));
        
        Cliente c = new Cliente();
        c.setId(rs.getLong("id_cliente"));
        c.setRucCI(rs.getString("ci_ruc"));
        c.setNombreCli(rs.getString("cli_nombre"));
        c.setApellidoCli(rs.getString("cli_apellido"));
        v.setCliente(c);
        
        Usuario u = new Usuario();
        u.setId(rs.getLong("usu_cod"));
        u.setNick(rs.getString("usu_nick"));
        v.setUsuario(u);
        
        v.setFecha(rs.getDate("fecha"));
        v.setTotal(rs.getInt("total"));
        v.setImporte(rs.getInt("importe"));
        v.setEstado(rs.getString("estado"));
        v.setNumFactura(rs.getString("num_factura"));
        v.setDescuento(rs.getInt("descuentos"));
        return v;
    }

}
