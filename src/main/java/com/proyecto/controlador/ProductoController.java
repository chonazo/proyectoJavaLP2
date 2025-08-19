package com.proyecto.controlador;

import com.proyecto.modelo.Producto;
import com.proyecto.modelo.TipoProducto;
import com.proyecto.modelo.UMedida;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoController {

    public List<Producto> listar() {
        String sql = "SELECT p.cod_producto, tp.cod_tipo_prod, tp.descrip AS tipo_producto, um.id_u_medida, um.u_descrip, p.descrip AS nombre_producto, p.precio, p.cod_barra "
                + "FROM public.producto AS p INNER JOIN public.tipo_producto AS tp ON (p.cod_tipo_prod = tp.cod_tipo_prod) "
                + "INNER JOIN public.u_medida AS um ON (p.id_u_medida = um.id_u_medida) ORDER BY cod_producto ASC";

        List<Producto> productos = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producto p = getProducto(rs);
                productos.add(p);

            }
        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }

        return productos;

    }

    public List<Producto> buscarProductos(String criterio) {
        String sql = "SELECT p.cod_producto, tp.cod_tipo_prod, tp.descrip AS tipo_producto, um.id_u_medida, um.u_descrip, p.descrip AS nombre_producto, p.precio, p.cod_barra "
                + "FROM public.producto AS p INNER JOIN public.tipo_producto AS tp ON (p.cod_tipo_prod = tp.cod_tipo_prod) "
                + "INNER JOIN public.u_medida AS um ON (p.id_u_medida = um.id_u_medida) WHERE p.descrip ILIKE ? OR p.cod_barra ILIKE ?";

        List<Producto> productos = new ArrayList<>();

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String parametroBusqueda = "%" + criterio + "%";
            stmt.setString(1, parametroBusqueda);
            stmt.setString(2, parametroBusqueda);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto p = getProducto(rs);
                    productos.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }
        return productos;
    }

    public Producto buscarProductosPorCodigoBarra(String buscar) {
        String sql = "SELECT p.cod_producto, tp.cod_tipo_prod, tp.descrip AS tipo_producto, um.id_u_medida, um.u_descrip, p.descrip AS nombre_producto, p.precio, p.cod_barra "
                + "FROM public.producto AS p INNER JOIN public.tipo_producto AS tp ON (p.cod_tipo_prod = tp.cod_tipo_prod) "
                + "INNER JOIN public.u_medida AS um ON (p.id_u_medida = um.id_u_medida) WHERE p.cod_barra = ?";
        Producto producto = null;
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, buscar);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Producto p = getProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }
        return producto;
    }

    public Producto buscarProductoPorId(Long id) {
        String sql = "SELECT p.cod_producto, tp.cod_tipo_prod, tp.descrip AS tipo_producto, um.id_u_medida, um.u_descrip, p.descrip AS nombre_producto, p.precio, p.cod_barra "
                + "FROM public.producto AS p INNER JOIN public.tipo_producto AS tp ON (p.cod_tipo_prod = tp.cod_tipo_prod) "
                + "INNER JOIN public.u_medida AS um ON (p.id_u_medida = um.id_u_medida) WHERE p.cod_producto = ?";
        Producto p = null;
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    p = getProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }
        return p;
    }
    
    
    
    public boolean existeProductoCodBarra(String codBarra) {

        String sql = "SELECT COUNT(*) FROM public.producto WHERE cod_barra = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codBarra);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: existeProducto()" + e.getMessage());
        }
        return false;
    }

    public boolean guardar(Producto producto) {
        if (existeProductoCodBarra(producto.getCodBarra())) {
            return false;
        }
        String sql = "INSERT INTO public.producto(cod_tipo_prod, id_u_medida, descrip, precio, cod_barra) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, producto.getTipoProducto().getId());
            stmt.setLong(2, producto.getuMedida().getId());
            stmt.setString(3, producto.getNombreProducto());
            stmt.setInt(4, producto.getPrecio());
            stmt.setString(5, producto.getCodBarra());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error Metodo: guardar()" + e.getMessage());
            return false;
        }
    }
    
    public void eliminarProducto(Long id){
        String sql = "DELETE FROM public.producto WHERE cod_producto = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        }catch (SQLException e) {
            System.out.println("Error Metodo: eliminarProducto()" + e.getMessage());            
        }
    }
    
    public void actualizarPrecio(Producto producto){
        String sql = "UPDATE public.producto SET precio = ? WHERE cod_producto = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, producto.getPrecio());
            stmt.setLong(2, producto.getId());
            stmt.executeUpdate();
            
        }catch (SQLException e) {
            System.out.println("Error Metodo: actualizarPrecio()" + e.getMessage());            
        }
    }

    public Producto getProducto(final ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("cod_producto"));

        TipoProducto tp = new TipoProducto();
        tp.setId(rs.getLong("cod_tipo_prod"));
        tp.setDescripcion(rs.getString("tipo_producto"));
        p.setTipoProducto(tp);

        UMedida um = new UMedida();
        um.setId(rs.getLong("id_u_medida"));
        um.setDescripcion(rs.getString("u_descrip"));
        p.setuMedida(um);

        p.setNombreProducto(rs.getString("nombre_producto"));
        p.setPrecio(rs.getInt("precio"));
        p.setCodBarra(rs.getString("cod_barra"));
        return p;
    }

}
