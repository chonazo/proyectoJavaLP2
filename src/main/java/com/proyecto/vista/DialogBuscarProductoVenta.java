package com.proyecto.vista;

import com.proyecto.controlador.ProductoController;
import com.proyecto.modelo.Producto;
import com.proyecto.modelo.TipoProducto;
import com.proyecto.modelo.UMedida;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DialogBuscarProductoVenta extends javax.swing.JDialog {

    Producto producto;
    ProductoController service = new ProductoController();
    DefaultTableModel modelo;

    public DialogBuscarProductoVenta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        listarProductos();
    }

    public void listarProductos() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("T. PRODUCTO");
        modelo.addColumn("U. MEDIDA");
        modelo.addColumn("N. PRODUCTO");
        modelo.addColumn("PRECIO");
        modelo.addColumn("COD. BARRA");
        tblProducto.setModel(modelo);

        List<Producto> lista = service.listar();

        for (Producto p : lista) {
            Object[] fila = new Object[6];
            fila[0] = p.getId();
            fila[1] = p.getTipoProducto().getDescripcion();
            fila[2] = p.getuMedida().getDescripcion();
            fila[3] = p.getNombreProducto();
            fila[4] = p.getPrecio();
            fila[5] = p.getCodBarra();

            modelo.addRow(fila);
        }

    }

    public void buscarProductos() {
        String buscar = txtBuscar.getText().trim();

        if (buscar.isBlank()) {
            JOptionPane.showMessageDialog(null, "La casilla Buscar no debe quedar vacia", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        modelo.addColumn("ID");
        modelo.addColumn("T. PRODUCTO");
        modelo.addColumn("U. MEDIDA");
        modelo.addColumn("N. PRODUCTO");
        modelo.addColumn("PRECIO");
        modelo.addColumn("COD. BARRA");
        tblProducto.setModel(modelo);

        List<Producto> lista = service.buscarProductos(buscar);

        if (lista.isEmpty()) {

            int mensaje = JOptionPane.showConfirmDialog(null,
                    "No existe el producto buscado, ¿desea agregar el nuevo producto?",
                    "Agregar Producto",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (mensaje == JOptionPane.YES_OPTION) {
                listarProductos();
                return;
            }
        }

        for (Producto p : lista) {
            Object[] fila = new Object[6];
            fila[0] = p.getId();
            fila[1] = p.getTipoProducto().getDescripcion();
            fila[2] = p.getuMedida().getDescripcion();
            fila[3] = p.getNombreProducto();
            fila[4] = p.getPrecio();
            fila[5] = p.getCodBarra();

            System.out.println(p.getNombreProducto());

            modelo.addRow(fila);
        }

    }

    public Producto getProductoSeleccionado() {
        return producto;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setText("Buscar Productos");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 210, -1));

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProducto);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 570, 290));

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setText("Buscar Producto:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 40));

        txtBuscar.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 210, 40));

        btnBuscar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 110, 40));

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 50, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked
        if (evt.getClickCount() == 2) {
            int fila = tblProducto.getSelectedRow();
            if (fila != -1) {
                producto = new Producto();
                producto.setId(Long.valueOf(tblProducto.getValueAt(fila, 0).toString().trim()));

                TipoProducto tp = new TipoProducto();
                tp.setDescripcion(tblProducto.getValueAt(fila, 1).toString().trim());
                producto.setTipoProducto(tp);

                UMedida um = new UMedida();
                um.setDescripcion(tblProducto.getValueAt(fila, 2).toString().trim());
                producto.setuMedida(um);

                producto.setNombreProducto(tblProducto.getValueAt(fila, 3).toString().trim());
                producto.setPrecio(Integer.valueOf(tblProducto.getValueAt(fila, 4).toString().trim()));
                producto.setCodBarra(tblProducto.getValueAt(fila, 5).toString().trim());

                this.dispose();
            }
        }
    }//GEN-LAST:event_tblProductoMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarProductos();

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        listarProductos();
    }//GEN-LAST:event_btnActualizarActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogBuscarProductoVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogBuscarProductoVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogBuscarProductoVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogBuscarProductoVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogBuscarProductoVenta dialog = new DialogBuscarProductoVenta(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
