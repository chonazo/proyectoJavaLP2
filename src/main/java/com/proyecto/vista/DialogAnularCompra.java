package com.proyecto.vista;

import com.proyecto.controlador.CompraController;
import com.proyecto.controlador.DetalleCompraController;
import com.proyecto.controlador.StockController;
import com.proyecto.modelo.Compra;
import com.proyecto.modelo.DetalleCompra;
import com.proyecto.modelo.Stock;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DialogAnularCompra extends javax.swing.JDialog {

    DefaultTableModel modelo;
    DetalleCompraController service = new DetalleCompraController();
    CompraController compraController = new CompraController();

    public DialogAnularCompra(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Anular compra");
        listarCompras();
    }

    public void listarCompras() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("#");
        modelo.addColumn("Desc. Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");
        modelo.addColumn("Subtotal");
        modelo.addColumn("Depósito");
        modelo.addColumn("Cod. Compra");
        modelo.addColumn("Nº Factura");

        tblAnularCompra.setModel(modelo);

        // ajustamos el ancho de las columnas
        tblAnularCompra.getColumnModel().getColumn(0).setPreferredWidth(10);  // Columna #
        tblAnularCompra.getColumnModel().getColumn(1).setPreferredWidth(120); // Columna Desc. Producto
        tblAnularCompra.getColumnModel().getColumn(2).setPreferredWidth(35);  // Columna Cantidad
        tblAnularCompra.getColumnModel().getColumn(3).setPreferredWidth(40);  // Columna Precio
        tblAnularCompra.getColumnModel().getColumn(4).setPreferredWidth(40);  // Columna Subtotal
        tblAnularCompra.getColumnModel().getColumn(6).setPreferredWidth(40);  // Columna Cod. Compra

        List<DetalleCompra> lista = service.vistaDetalleCompraAnular();

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay compras registradas.",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (DetalleCompra dt : lista) {
            Object[] fila = new Object[8];
            fila[0] = dt.getProducto().getId();
            fila[1] = dt.getProducto().getNombreProducto();
            fila[2] = dt.getCantidad();
            fila[3] = dt.getPrecio();
            fila[4] = dt.getSubtotal();
            fila[5] = dt.getDeposito().getDescriDeposito();
            fila[6] = dt.getCompra().getId();
            fila[7] = dt.getCompra().getNumFactura();

            modelo.addRow(fila);
        }
    }

    public void buscarCompraPorFactura() {
        String buscar = txtBuscarPorFact.getText().trim();

        System.out.println("text buscar = " + buscar);

        if (buscar.isBlank()) {
            JOptionPane.showMessageDialog(null, "La casilla buscar no debe quedar vacia para buscar.",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("#");
        modelo.addColumn("Desc. Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");
        modelo.addColumn("Subtotal");
        modelo.addColumn("Depósito");
        modelo.addColumn("Cod. Compra");
        modelo.addColumn("Nº Factura");

        tblAnularCompra.setModel(modelo);

        // ajustamos el ancho de las columnas
        tblAnularCompra.getColumnModel().getColumn(0).setPreferredWidth(10);  // Columna #
        tblAnularCompra.getColumnModel().getColumn(1).setPreferredWidth(120); // Columna Desc. Producto
        tblAnularCompra.getColumnModel().getColumn(2).setPreferredWidth(35);  // Columna Cantidad
        tblAnularCompra.getColumnModel().getColumn(3).setPreferredWidth(40);  // Columna Precio
        tblAnularCompra.getColumnModel().getColumn(4).setPreferredWidth(40);  // Columna Subtotal
        tblAnularCompra.getColumnModel().getColumn(6).setPreferredWidth(40);  // Columna Cod. Compra

        List<DetalleCompra> lista = service.buscarPorNroFactura(buscar);
        System.out.println("Lista encontrada = " + lista);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Producto con codigo de barra no encontrado.",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (DetalleCompra dt : lista) {
            Object[] fila = new Object[8];
            fila[0] = dt.getProducto().getId();
            fila[1] = dt.getProducto().getNombreProducto();
            fila[2] = dt.getCantidad();
            fila[3] = dt.getPrecio();
            fila[4] = dt.getSubtotal();
            fila[5] = dt.getDeposito().getDescriDeposito();
            fila[6] = dt.getCompra().getId();
            fila[7] = dt.getCompra().getNumFactura();

            modelo.addRow(fila);
        }

    }

    public void anularCompraYActualizarStock(String nroFactura) {
        Compra compra = compraController.obtenerPorFactura(nroFactura);

        if (compra == null) {
            JOptionPane.showMessageDialog(null, "Compra no encontrada.");
            return;
        }

        if ("ANULADO".equalsIgnoreCase(compra.getEstado())) {
            JOptionPane.showMessageDialog(null, "Esta compra ya está anulada.");
            return;
        }

        Long codCompra = compra.getId();
        List<DetalleCompra> detalles = service.obtenerDetallesPorCompra(codCompra);

        StockController stockController = new StockController();

        for (DetalleCompra dc : detalles) {
            long codProducto = dc.getProducto().getId();
            long codDeposito = dc.getDeposito().getId();
            int cantidad = dc.getCantidad();

            Stock stockActual = stockController.buscarStock(codProducto, codDeposito);
            if (stockActual != null) {
                int nuevaCantidad = stockActual.getCantidad() - cantidad;
                if (nuevaCantidad < 0) {
                    nuevaCantidad = 0;
                }

                stockActual.setCantidad(nuevaCantidad);
                stockController.actualizar(stockActual);
            } else {
                System.out.println("No se encontró stock del producto " + codProducto + " en depósito " + codDeposito);
            }
        }

        boolean anulado = compraController.anularCompraPorFactura(nroFactura);

        if (anulado) {
            JOptionPane.showMessageDialog(this, "Compra anulada exitosamente.");
            listarCompras(); // refrescar tabla
        } else {
            JOptionPane.showMessageDialog(this, "Error al anular la compra.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtBuscarPorFact = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAnularCompra = new javax.swing.JTable();
        btnBuscar = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel1.setText("Anular Compra");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 6, 180, 31));

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setText("Ingrese Nº de Factura :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 194, 41));
        jPanel1.add(txtBuscarPorFact, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 249, 40));

        tblAnularCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblAnularCompra);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 730, 220));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 53, 40));

        btnAnular.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAnular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/anular.png"))); // NOI18N
        btnAnular.setText("Anular");
        btnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnular, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 360, 140, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarCompraPorFactura();
        txtBuscarPorFact.setText("");
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularActionPerformed

        int fila = tblAnularCompra.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un número de factura para anular");
            return;
        }

        String nroFactura = tblAnularCompra.getValueAt(fila, 7).toString();
        System.out.println(nroFactura);

        // Anular y actualizar
        anularCompraYActualizarStock(nroFactura);
        listarCompras();

        Compra compra = compraController.obtenerPorFactura(nroFactura);
        List<DetalleCompra> detalles = service.obtenerDetallesPorCompra(compra.getId());

        try {
            // generamos el reporte 
            compraController.generarReporteAnulacion(compra, detalles);
        } catch (IOException ex) {
            Logger.getLogger(DialogAnularCompra.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnAnularActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogAnularCompra.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogAnularCompra.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogAnularCompra.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogAnularCompra.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogAnularCompra dialog = new DialogAnularCompra(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAnularCompra;
    private javax.swing.JTextField txtBuscarPorFact;
    // End of variables declaration//GEN-END:variables
}
