package com.proyecto.controlador;

import com.proyecto.modelo.Usuario;
import com.proyecto.util.Conexion;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

public class UsuarioController {

    public List<Usuario> listar() {
        String sql = "SELECT usu_cod, usu_nombres, usu_nick, usu_clave, usu_nivel, estado FROM public.usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = getUsuario(rs);
                usuarios.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }

        return usuarios;
    }

    public Usuario buscarPorNick(String nick) {
        String sql = "SELECT usu_cod, usu_nombres, usu_nick, usu_clave, usu_nivel, estado "
                + "FROM public.usuario WHERE UPPER(usu_nick) = ?";
        Usuario u = null;
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nick);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u = getUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: buscarPorNick()" + e.getMessage());
        }

        return u;

    }

    public String encriptarMD5(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(texto.getBytes());

            // Convertimos el array de bytes a hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error al encriptar: " + e.getMessage());
            return null;
        }

    }

    public boolean existeNick(String nick) {

        String sql = "SELECT COUNT(*) FROM public.usuario WHERE usu_nick = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nick);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // si es mayor que cero el nick existe
                }
            }

        } catch (SQLException e) {

            System.out.println("Error Metod: existeNick()" + e.getMessage());
        }

        return false; // asumimos que no existe el nick
    }

    public boolean guardar(Usuario usuario) {
        // verificamos si el nick ya existe
        if (existeNick(usuario.getNick())) {
            return false; // si existe retornamos false
        }

        String sql = "INSERT INTO public.usuario(usu_nombres, usu_nick, usu_clave, usu_nivel, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getNick());
            stmt.setString(3, encriptarMD5(usuario.getClave()));
            stmt.setString(4, usuario.getNivel());
            stmt.setString(5, usuario.getEstado());

            stmt.executeUpdate();
            return true; // si no existe retornamos true  

        } catch (SQLException e) {
            System.out.println("Error en método guardar(): " + e.getMessage());
            return false;
        }
    }

    public void editar(Usuario usuario) {

        String sql = "UPDATE public.usuario SET usu_nombres=?, usu_nick=?, usu_clave=?, usu_nivel=?, estado=? WHERE usu_cod = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getNick());
            stmt.setString(3, encriptarMD5(usuario.getClave()));
            stmt.setString(4, usuario.getNivel());
            stmt.setString(5, usuario.getEstado());
            stmt.setLong(6, usuario.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error Metodo: editar()" + e.getMessage());

        }

    }

    public void Eliminar(Long id) {//se usa update para inactivar usuario y no borrarlo
        String sql = "DELETE FROM public.usuario WHERE usu_cod = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Metodo: eliminar()" + e.getMessage());
        }
    }

    public List<Usuario> buscarUsuarios(String criterio) {
        String sql = "SELECT usu_cod, usu_nombres, usu_nick, usu_clave, usu_nivel, estado "
                + "FROM public.usuario WHERE usu_nombres ILIKE ? OR usu_nick ILIKE ?";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String buscar = "%" + criterio + "%";
            stmt.setString(1, buscar);
            stmt.setString(2, buscar);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario u = getUsuario(rs);
                    usuarios.add(u);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: buscarPorNick()" + e.getMessage());
        }

        return usuarios;

    }

    private Usuario getUsuario(final ResultSet rs) throws SQLException {

        Usuario u = new Usuario();
        u.setId(rs.getLong("usu_cod"));
        u.setNombre(rs.getString("usu_nombres"));
        u.setNick(rs.getString("usu_nick"));
        u.setClave(rs.getString("usu_clave"));
        u.setNivel(rs.getString("usu_nivel"));
        u.setEstado(rs.getString("estado"));
        return u;
    }

    public void generarInformeUsuarios(String filePath) {
        Document document = new Document();
        try {
            // inicializamos el escritor PDF
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // añadimos el título
            document.add(new Paragraph("Informe de Usuarios"));
            document.add(new Paragraph(" "));

            // creamos la tabla con 6 columnas
            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100); // ancho completo del documento
            tabla.setSpacingBefore(10f);
            tabla.setSpacingAfter(10f);

            // cabeceras de la tabla
            tabla.addCell("Código");
            tabla.addCell("Nombre");
            tabla.addCell("Nick");
            tabla.addCell("Clave (MD5)");
            tabla.addCell("Nivel");
            tabla.addCell("Estado");

            // consulta a la base de datos
            String sql = "SELECT usu_cod, usu_nombres, usu_nick, usu_clave, usu_nivel, estado FROM public.usuario ORDER BY usu_cod ASC";

            try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        tabla.addCell(rs.getString("usu_cod"));
                        tabla.addCell(rs.getString("usu_nombres"));
                        tabla.addCell(rs.getString("usu_nick"));
                        tabla.addCell(rs.getString("usu_clave"));
                        tabla.addCell(rs.getString("usu_nivel"));
                        tabla.addCell(rs.getString("estado"));
                    }
                }
            }

            // añadimos la tabla al documento
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
