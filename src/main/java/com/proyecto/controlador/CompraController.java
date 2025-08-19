package com.proyecto.controlador;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.proyecto.modelo.Compra;
import com.proyecto.modelo.DetalleCompra;
import com.proyecto.modelo.Proveedor;
import com.proyecto.modelo.Usuario;
import com.proyecto.util.Conexion;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CompraController {

    public List<Compra> listarCompras() {
        String sql = "SELECT c.cod_compra, c.cod_proveedor, p.razon_social, p.ruc, c.nro_factura, "
                + "c.fecha_compra, c.estado, c.usu_cod, u.usu_nick,c.fecha_registro "
                + "FROM public.compra AS c INNER JOIN public.proveedor AS p ON (c.cod_proveedor = p.cod_proveedor) "
                + "INNER JOIN public.usuario AS u ON (c.usu_cod = u.usu_cod)";

        List<Compra> compras = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Compra c = getCompras(rs);
                compras.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : listarCompras()" + e.getMessage());
        }

        return compras;
    }

    public List<Compra> buscar(String numFact) {

        String sql = "SELECT c.cod_compra, c.cod_proveedor, p.razon_social, p.ruc, c.nro_factura, "
                + "c.fecha_compra, c.estado, c.usu_cod, u.usu_nick,c.fecha_registro "
                + "FROM public.compra AS c INNER JOIN public.proveedor AS p ON (c.cod_proveedor = p.cod_proveedor) "
                + "INNER JOIN public.usuario AS u ON (c.usu_cod = u.usu_cod) WHERE c.nro_factura ILIKE ?";

        List<Compra> compras = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + numFact + "%");

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Compra c = getCompras(rs);
                    compras.add(c);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : buscar()" + e.getMessage());
        }

        return compras;
    }

    public boolean existeNumFactura(String factura) {
        String sql = "SELECT COUNT(*) FROM public.compra WHERE nro_factura = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, factura);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : existeNumFactura()" + e.getMessage());
        }

        return false;
    }

    public Compra guardar(Compra compra) {
        if (existeNumFactura(compra.getNumFactura())) {
            return null;
        }

        String sql = "INSERT INTO public.compra(cod_proveedor, nro_factura, fecha_compra, estado, usu_cod, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?, ?) RETURNING cod_compra";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, compra.getProveedor().getId());
            stmt.setString(2, compra.getNumFactura());
            stmt.setDate(3, compra.getFechaCompra());
            stmt.setString(4, compra.getEstado());
            stmt.setLong(5, compra.getUsuario().getId());
            stmt.setDate(6, compra.getFechaRegistro());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Long idGenerado = rs.getLong(1);
                compra.setId(idGenerado);
                return compra;
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : guardar()" + e.getMessage());
        }
        return null;
    }

    public Long obtenerCodCompraPorNroFactura(String nroFactura) {
        String sql = "SELECT cod_compra FROM compra WHERE nro_factura = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nroFactura);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("cod_compra");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener obtenerCodCompraPorNroFactura: " + e.getMessage());
        }
        return null;
    }

    public Compra obtenerPorFactura(String nroFactura) {
        String sql = "SELECT c.cod_compra, c.cod_proveedor, p.razon_social, p.ruc, c.nro_factura, "
                + "c.fecha_compra, c.estado, c.usu_cod, u.usu_nick, c.fecha_registro "
                + "FROM public.compra AS c "
                + "INNER JOIN public.proveedor AS p ON (c.cod_proveedor = p.cod_proveedor) "
                + "INNER JOIN public.usuario AS u ON (c.usu_cod = u.usu_cod) "
                + "WHERE c.nro_factura = ? LIMIT 1";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nroFactura);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getCompras(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerPorFactura: " + e.getMessage());
        }

        return null;
    }

    public boolean anularCompraPorFactura(String nroFactura) {
        Compra compra = obtenerPorFactura(nroFactura);

        if (compra == null) {
            System.out.println("Compra no encontrada para anular.");
            return false;
        }

        // 1. Cambiar estado a "ANULADO"
        String sqlUpdate = "UPDATE compra SET estado = 'ANULADO' WHERE cod_compra = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
            stmt.setLong(1, compra.getId());
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al anular compra: " + e.getMessage());
        }

        return false;
    }

    public Compra getCompras(final ResultSet rs) throws SQLException {
        Compra c = new Compra();
        c.setId(rs.getLong("cod_compra"));

        Proveedor p = new Proveedor();
        p.setId(rs.getLong("cod_proveedor"));
        p.setRazonSocial(rs.getString("razon_social"));
        p.setRuc(rs.getString("ruc"));
        c.setProveedor(p);

        c.setNumFactura(rs.getString("nro_factura"));
        c.setFechaCompra(rs.getDate("fecha_compra"));
        c.setEstado(rs.getString("estado"));

        Usuario u = new Usuario();
        u.setId(rs.getLong("usu_cod"));
        u.setNick(rs.getString("usu_nick"));
        c.setUsuario(u);

        c.setFechaRegistro(rs.getDate("fecha_registro"));
        return c;
    }

    public void generarReporteAnulacion(Compra compra, List<DetalleCompra> detalles) throws IOException {
        try {
            // Ruta automática al lado del .jar
            String nombreArchivo = "Anulacion_" + compra.getNumFactura() + ".pdf";
            String rutaDestino = new File(nombreArchivo).getAbsolutePath();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(rutaDestino));
            document.open();

            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fontNormal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            document.add(new Paragraph("REPORTE DE ANULACIÓN DE COMPRA", fontTitulo));
            document.add(new Paragraph(" ")); // Espacio

            // Datos generales
            document.add(new Paragraph("N° Factura: " + compra.getNumFactura(), fontNormal));
            document.add(new Paragraph("Proveedor: " + compra.getProveedor().getRazonSocial(), fontNormal));
            document.add(new Paragraph("RUC: " + compra.getProveedor().getRuc(), fontNormal));
            document.add(new Paragraph("Fecha Compra: " + compra.getFechaCompra(), fontNormal));
            document.add(new Paragraph("Fecha Anulación: " + new java.sql.Date(System.currentTimeMillis()), fontNormal));
            document.add(new Paragraph("Usuario: " + compra.getUsuario().getNick(), fontNormal));
            document.add(new Paragraph("Estado: " + compra.getEstado(), fontNormal));
            document.add(new Paragraph(" ")); // Espacio

            // Tabla
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 2, 2, 2, 2});

            table.addCell("N°");
            table.addCell("Producto");
            table.addCell("Cantidad");
            table.addCell("Precio");
            table.addCell("Subtotal");
            table.addCell("Depósito");

            int index = 1;
            for (DetalleCompra dc : detalles) {
                table.addCell(String.valueOf(index++));
                table.addCell(dc.getProducto().getNombreProducto());
                table.addCell(String.valueOf(dc.getCantidad()));
                table.addCell(String.valueOf(dc.getPrecio()));
                table.addCell(String.valueOf(dc.getCantidad() * dc.getPrecio()));
                table.addCell(dc.getDeposito().getDescriDeposito());
            }

            document.add(table);
            document.close();

            // Abrir PDF automáticamente con visor predeterminado
            Desktop.getDesktop().open(new File(rutaDestino));

        } catch (DocumentException | IOException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
