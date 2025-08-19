package com.proyecto.controlador;

import com.proyecto.modelo.Ciudad;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;

public class CiudadController {

    public List<Ciudad> listarCiudades() {
        String sql = "SELECT cod_ciudad, descrip_ciudad FROM public.ciudad ORDER BY descrip_ciudad";
        List<Ciudad> ciudades = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ciudad ciudad = new Ciudad();
                ciudad.setId(rs.getLong("cod_ciudad"));
                ciudad.setNombreCiudad(rs.getString("descrip_ciudad"));
                ciudades.add(ciudad);
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : listarCiudades()" + e.getMessage());
        }

        return ciudades;
    }

    public Ciudad obtenerCiudadPorId(Long id) {
        String sql = "SELECT cod_ciudad, descrip_ciudad FROM public.ciudad WHERE cod_ciudad=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ciudad ciudad = new Ciudad();
                    ciudad.setId(rs.getLong("cod_ciudad"));
                    ciudad.setNombreCiudad(rs.getString("descrip_ciudad"));
                    return ciudad;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error metodo: obtenerCiudadPorId()" + e.getMessage());
        }

        return null;
    }

    public List<Ciudad> buscar(String buscar) {
        String sql = "SELECT cod_ciudad, descrip_ciudad FROM public.ciudad WHERE descrip_ciudad ILIKE ?";

        List<Ciudad> ciudades = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + buscar + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ciudad c = new Ciudad();
                    c.setId(rs.getLong("cod_ciudad"));
                    c.setNombreCiudad(rs.getString("descrip_ciudad"));
                    ciudades.add(c);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error metodo: buscar()" + e.getMessage());
        }

        return ciudades;
    }

    public boolean existeCiudad(String ciudad) {
        String sql = "SELECT count(*) FROM public.ciudad WHERE UPPER(descrip_ciudad) = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ciudad);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error metodo: existeCiudad()" + e.getMessage());
        }
        return false;
    }

    public boolean agregar(Ciudad ciudad) {
        if (existeCiudad(ciudad.getNombreCiudad())) {
            return false;
        }

        String sql = "INSERT INTO public.ciudad(descrip_ciudad) VALUES (?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ciudad.getNombreCiudad());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error metodo: agregar()" + e.getMessage());
            return false;
        }

    }

    public void editar(Ciudad ciudad) {
        String sql = "UPDATE public.ciudad SET descrip_ciudad=? WHERE cod_ciudad=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ciudad.getNombreCiudad());
            stmt.setLong(2, ciudad.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error metodo: editar()" + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        String sql = "DELETE FROM public.ciudad WHERE cod_ciudad=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error metodo: eliminar()" + e.getMessage());
        }
    }

    public void generarInformeCiudades(String filePath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Título
            document.add(new Paragraph("Informe de Ciudades"));
            document.add(new Paragraph(" "));

            // Tabla de 2 columnas
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(80);
            tabla.setSpacingBefore(10f);
            tabla.setSpacingAfter(10f);

            // Cabeceras
            tabla.addCell("Código Ciudad");
            tabla.addCell("Nombre de Ciudad");

            // Consulta y carga de datos
            String sql = "SELECT cod_ciudad, descrip_ciudad FROM public.ciudad ORDER BY cod_ciudad ASC;";
            try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("cod_ciudad"));
                    tabla.addCell(rs.getString("descrip_ciudad"));
                }
            }

            // Agregamos la tabla al PDF
            document.add(tabla);
            document.close();

            JOptionPane.showMessageDialog(null, "PDF generado exitosamente en: " + filePath,
                    "Informe Generado", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | FileNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el informe: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
