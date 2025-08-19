package com.proyecto.vista;

import com.proyecto.controlador.UMedidaController;
import com.proyecto.modelo.UMedida;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DialogGestionUMedidas extends javax.swing.JDialog {

    UMedidaController service = new UMedidaController();
    UMedida uMedida;
    DefaultTableModel modelo;

    public DialogGestionUMedidas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Gestiones U. Medidas");
        listarUMedidas();
        inactivarText();
    }

    public void inactivarText() {
        txtBuscar.setEnabled(false);
        txtNombreUM.setEnabled(false);
        txtId.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnAgregar.setEnabled(false);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnBuscar.setEnabled(false);
        limpiar();
    }

    public void activarText() {
        txtBuscar.setEnabled(true);
        txtNombreUM.setEnabled(true);
        btnActualizar.setEnabled(true);
        btnAgregar.setEnabled(true);
        btnEditar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnBuscar.setEnabled(true);
    }

    public void pasarDatos() {
        int fila = tblUMedida.getSelectedRow();

        String id = tblUMedida.getValueAt(fila, 0).toString().trim();
        String nombre = tblUMedida.getValueAt(fila, 1).toString().trim();

        txtId.setText(id);
        txtNombreUM.setText(nombre);
    }

    public void listarUMedidas() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        modelo.addColumn("ID");
        modelo.addColumn("N.UNI. MEDIDAS");
        tblUMedida.setModel(modelo);

        List<UMedida> lista = service.listar();
        for (UMedida u : lista) {
            Object[] fila = new Object[2];
            fila[0] = u.getId();
            fila[1] = u.getDescripcion();

            modelo.addRow(fila);
        }
        limpiar();
    }

    public void buscar() {

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
        modelo.addColumn("N.UNI. MEDIDAS");
        tblUMedida.setModel(modelo);

        List<UMedida> lista = service.buscar(buscar);

        if (lista.isEmpty()) {

            JOptionPane.showMessageDialog(null, "No existe las U. de Medidas buscadas", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            listarUMedidas();
        }

        for (UMedida u : lista) {
            Object[] fila = new Object[2];
            fila[0] = u.getId();
            fila[1] = u.getDescripcion();

            modelo.addRow(fila);
        }
    }

    public void guardar() {
        String nombre = txtNombreUM.getText().trim().toUpperCase();

        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(null, "La casilla Nombre. U. Medidas no debe quedar vacia", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (service.existeUMedida(nombre)) {
            JOptionPane.showMessageDialog(null, "Ya existe una U. de Medida con el nombre ingresado, por favor ingrese otro",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            txtNombreUM.requestFocus();
            txtNombreUM.selectAll();
            return;
        }

        uMedida = new UMedida();
        uMedida.setDescripcion(nombre);

        if (service.guardar(uMedida)) {
            JOptionPane.showMessageDialog(null, "U. de medida guardado exitosamente", "Exito!",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiar();
            listarUMedidas();
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo guardar la unidad de medida", "Error!",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void editar() {
        int fila = tblUMedida.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una U. de medida de la tabla", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Long id = Long.valueOf(txtId.getText().trim());
        String nombre = txtNombreUM.getText().trim().toUpperCase();

        if (nombre.isBlank()) {

            JOptionPane.showMessageDialog(null, "La casilla Nombre. U. Medidas no debe quedar vacia", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        uMedida = new UMedida();
        uMedida.setId(id);
        uMedida.setDescripcion(nombre);

        service.editar(uMedida);

        JOptionPane.showMessageDialog(null, "U. Medida editado exitosamente", "Exito!",
                JOptionPane.INFORMATION_MESSAGE);

        listarUMedidas();
        limpiar();
    }

    public void eliminar() {
        int fila = tblUMedida.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una U. de medida de la tabla", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            Long id = Long.valueOf(txtId.getText().trim());
            int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro que quieres eliminar esta U. Medida",
                    "Confirmar para eliminar.",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                service.eliminar(id);
                listarUMedidas();
                limpiar();
                JOptionPane.showMessageDialog(null, "La U. Medida fue eliminado exitosamente", "Exito!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void limpiar() {
        txtBuscar.setText("");
        txtId.setText("");
        txtNombreUM.setText("");
    }

    public UMedida getUMedidaSeleccionado() {
        return uMedida;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUMedida = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtNombreUM = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setText("Gestion Unidad de Medidas");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(111, 6, 291, 32));

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setText("Buscar U. Medida:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 44, 156, 26));
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 76, 174, 34));

        btnBuscar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 76, 98, 34));

        tblUMedida.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblUMedida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUMedidaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUMedida);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 296, 147));

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setText("Nombre U. Medida:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, 167, 28));
        jPanel1.add(txtNombreUM, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, 185, 32));

        btnAgregar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnAgregar.setText("GUARDAR");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 185, 36));

        btnActualizar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 327, -1, 36));

        btnEliminar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 329, -1, 32));

        btnEditar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnEditar.setText("EDITAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 329, 102, 32));
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 327, 70, 36));

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setText("Id:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 335, 35, -1));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cancel.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 70, 50, 40));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/check.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, 50, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        guardar();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        listarUMedidas();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void tblUMedidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUMedidaMouseClicked
        if (evt.getClickCount() == 2) { //el usuario debe hacer doble clic para seleccionar provvedor
            int fila = tblUMedida.getSelectedRow();
            if (fila != -1) { //si la fila esta seleccionada se realiza el listaner                

                uMedida = new UMedida();

                uMedida.setId(Long.valueOf(tblUMedida.getValueAt(fila, 0).toString()));
                uMedida.setDescripcion(tblUMedida.getValueAt(fila, 1).toString());

                this.dispose();
            }
        }

        if (evt.getClickCount() == 1) {
            int fila = tblUMedida.getSelectedRow();
            if (fila != -1) {
                pasarDatos();
            }
        }
    }//GEN-LAST:event_tblUMedidaMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        activarText();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        inactivarText();
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogGestionUMedidas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogGestionUMedidas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogGestionUMedidas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogGestionUMedidas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogGestionUMedidas dialog = new DialogGestionUMedidas(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUMedida;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombreUM;
    // End of variables declaration//GEN-END:variables
}
