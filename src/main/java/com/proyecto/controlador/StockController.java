package com.proyecto.controlador;

import com.proyecto.modelo.Deposito;
import com.proyecto.modelo.Producto;
import com.proyecto.modelo.Stock;
import com.proyecto.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StockController {

    public List<Stock> listar() {
        String sql = "SELECT s.cod_deposito, d.descrip AS depo_descri, s.cod_producto, p.descrip as pro_descri, "
                + "p.cod_barra, s.cantidad FROM public.stock AS s INNER JOIN public.deposito AS d ON "
                + "(s.cod_deposito = d.cod_deposito) INNER JOIN public.producto AS p ON (s.cod_producto = p.cod_producto)";

        List<Stock> stock = new ArrayList<>();
        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Stock s = getStock(rs);
                stock.add(s);

            }

        } catch (SQLException e) {
            System.out.println("Error Metodo: listar()" + e.getMessage());
        }

        return stock;
    }
    
    public List<Stock> buscarPorDescriCodBarra(String criterio){
        String sql = "SELECT s.cod_deposito, d.descrip AS depo_descri, s.cod_producto, p.descrip as pro_descri, "
                + "p.cod_barra, s.cantidad FROM public.stock AS s INNER JOIN public.deposito AS d ON "
                + "(s.cod_deposito = d.cod_deposito) INNER JOIN public.producto AS p ON (s.cod_producto = p.cod_producto)"
                + "WHERE p.descrip ILIKE ? OR p.cod_barra ILIKE ?";
        
        List<Stock> stocks = new ArrayList<>();
        
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String buscar = "%" + criterio + "%";
            stmt.setString(1, buscar);
            stmt.setString(2, buscar);
            
            try(ResultSet rs = stmt.executeQuery()){
                
                while(rs.next()){
                    Stock s = getStock(rs);
                    stocks.add(s);
                }
            }
            
        }catch (SQLException e) {
            System.out.println("Error metodo : buscarPorDescriCodBarra(): " + e.getMessage());
        }
        
        return stocks;
    }

    public void guardar(Stock stock) {
        String sql = "INSERT INTO public.stock(cod_deposito, cod_producto, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, stock.getDeposito().getId());
            stmt.setLong(2, stock.getProducto().getId());
            stmt.setInt(3, stock.getCantidad());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error Metodo: guardar()" + e.getMessage());
        }

    }

    public void actualizar(Stock stock) {
        String sql = "UPDATE public.stock SET cantidad = ? WHERE cod_deposito = ? AND cod_producto = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stock.getCantidad());
            stmt.setLong(2, stock.getDeposito().getId());
            stmt.setLong(3, stock.getProducto().getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error metodo : actualizar() - " + e.getMessage());
        }
    }
    
    public Stock buscarStock(long codProducto, long codDeposito) {
        String sql = "SELECT s.cod_deposito, d.descrip AS depo_descri, s.cod_producto, p.descrip as pro_descri, "
                + "p.cod_barra, s.cantidad FROM public.stock AS s INNER JOIN public.deposito AS d ON "
                + "(s.cod_deposito = d.cod_deposito) INNER JOIN public.producto AS p ON (s.cod_producto = p.cod_producto) "
                + "WHERE s.cod_producto = ? AND s.cod_deposito = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, codProducto);
            stmt.setLong(2, codDeposito);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return getStock(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error metodo : buscarStock(): " + e.getMessage());
        }

        return null; // no se encontró stock
    }

    public Stock buscarPrimerStockDisponible(long codProducto) {
        String sql = "SELECT s.cod_deposito, d.descrip AS depo_descri, s.cod_producto, p.descrip AS pro_descri, "
                + "p.cod_barra, s.cantidad FROM stock AS s JOIN deposito AS d ON (s.cod_deposito = d.cod_deposito) "
                + "JOIN producto AS p ON (s.cod_producto = p.cod_producto) WHERE s.cod_producto = ? AND s.cantidad > 0 "
                + "ORDER BY s.cantidad DESC LIMIT 1";

        try (Connection conn = Conexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, codProducto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getStock(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error buscarPrimerStockDisponible(): " + e.getMessage());
        }

        return null;
    }

    public Stock getStock(final ResultSet rs) throws SQLException {
        Stock s = new Stock();

        Deposito d = new Deposito();
        d.setId(rs.getLong("cod_deposito"));
        d.setDescriDeposito(rs.getString("depo_descri"));
        s.setDeposito(d);

        Producto p = new Producto();
        p.setId(rs.getLong("cod_producto"));
        p.setNombreProducto(rs.getString("pro_descri"));
        p.setCodBarra(rs.getString("cod_barra"));
        s.setProducto(p);

        s.setCantidad(rs.getInt("cantidad"));
        return s;
    }

}
