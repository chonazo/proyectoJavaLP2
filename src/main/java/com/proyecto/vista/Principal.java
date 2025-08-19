package com.proyecto.vista;

import com.proyecto.modelo.Usuario;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Principal extends javax.swing.JFrame {

    private Usuario usuarioLogueado;

    public Principal(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        initComponents();
        validarAcceso(); // Nueva función que oculta o muestra menús
        usuarioVendedor();
    }

    public Principal() {
        initComponents();
    }

    private void validarAcceso() {
        // solo se muestra la opción si el usuario tiene nivel ADMINISTRADOR
        if (!"ADMINISTRADOR".equalsIgnoreCase(usuarioLogueado.getNivel())) {
            jmGestionUsuario.setVisible(false);
        }
    }

    private void usuarioVendedor() {
        if (!"VENDEDOR".equalsIgnoreCase(usuarioLogueado.getNivel())) {
            jmCompras.setVisible(false);
        }
    }

    private void abrirManualPDF() {
        try {
           
            File archivoPDF = new File("manual/manual_usuario.pdf");

            if (!archivoPDF.exists()) {
                JOptionPane.showMessageDialog(this, "No se encontró el manual PDF", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(archivoPDF);
            } else {
                JOptionPane.showMessageDialog(this, "No se puede abrir el PDF en este sistema", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error al abrir el PDF:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jplPrincipal = new javax.swing.JPanel();
        jmPrincipal = new javax.swing.JMenuBar();
        menuCliente = new javax.swing.JMenu();
        menuACiudad = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        menuTProducto = new javax.swing.JMenuItem();
        menuUMedidas = new javax.swing.JMenuItem();
        menuDeposito = new javax.swing.JMenuItem();
        menuSalir = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jmGestionUsuario = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jmCompras = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jplPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/archivo.png"))); // NOI18N
        menuCliente.setText("Archivo");

        menuACiudad.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuACiudad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ciudad.png"))); // NOI18N
        menuACiudad.setText("Ciudad");
        menuACiudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuACiudadActionPerformed(evt);
            }
        });
        menuCliente.add(menuACiudad);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cliente.png"))); // NOI18N
        jMenuItem3.setText("Clientes");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        menuCliente.add(jMenuItem3);

        menuTProducto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuTProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/tipoProd.png"))); // NOI18N
        menuTProducto.setText("Tipo Producto");
        menuTProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTProductoActionPerformed(evt);
            }
        });
        menuCliente.add(menuTProducto);

        menuUMedidas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuUMedidas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/uMedidas.png"))); // NOI18N
        menuUMedidas.setText("U. Medida");
        menuUMedidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUMedidasActionPerformed(evt);
            }
        });
        menuCliente.add(menuUMedidas);

        menuDeposito.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuDeposito.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/deposito.png"))); // NOI18N
        menuDeposito.setText("Deposito");
        menuDeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDepositoActionPerformed(evt);
            }
        });
        menuCliente.add(menuDeposito);

        menuSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/close.png"))); // NOI18N
        menuSalir.setText("salir");
        menuSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSalirActionPerformed(evt);
            }
        });
        menuCliente.add(menuSalir);

        jmPrincipal.add(menuCliente);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuario.png"))); // NOI18N
        jMenu2.setText("Usuarios");

        jmGestionUsuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jmGestionUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuarioMenu.png"))); // NOI18N
        jmGestionUsuario.setText("Gestion Usuario");
        jmGestionUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmGestionUsuarioActionPerformed(evt);
            }
        });
        jMenu2.add(jmGestionUsuario);

        jmPrincipal.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/proveedores.png"))); // NOI18N
        jMenu3.setText("Proveedores");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/provee.png"))); // NOI18N
        jMenuItem1.setText("Gestion Proveedores");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jmPrincipal.add(jMenu3);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/compraPro.png"))); // NOI18N
        jMenu1.setText("Compra Productos");

        jmCompras.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jmCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/compra.png"))); // NOI18N
        jmCompras.setText("Compra");
        jmCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmComprasActionPerformed(evt);
            }
        });
        jMenu1.add(jmCompras);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/anular.png"))); // NOI18N
        jMenuItem4.setText("Anular Compra");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/stock.png"))); // NOI18N
        jMenuItem5.setText("Stock");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jmPrincipal.add(jMenu1);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/vender.png"))); // NOI18N
        jMenu4.setText("Ventas Productos");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/venta.png"))); // NOI18N
        jMenuItem2.setText("Ventas");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jmPrincipal.add(jMenu4);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/help.png"))); // NOI18N
        jMenu5.setText("Ayuda");

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/manual.png"))); // NOI18N
        jMenuItem8.setText("Manual de Uso");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem8);

        jmPrincipal.add(jMenu5);

        jMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout_2.png"))); // NOI18N
        jMenu6.setText("Cerrar Sesión");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout.png"))); // NOI18N
        jMenuItem6.setText("Logout");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem6);

        jmPrincipal.add(jMenu6);

        setJMenuBar(jmPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jplPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 1072, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jplPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSalirActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea salir del sistema?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_menuSalirActionPerformed

    private void jmGestionUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmGestionUsuarioActionPerformed
        this.setVisible(false); // cerramos Principal

        // mostramos el DialogGestionUsuarios
        DialogGestionUsuarios dialog = new DialogGestionUsuarios(this, true);
        dialog.setVisible(true);

        this.setVisible(true); // volvemos a mostrar Principal despues de cerrar el DialogGestionUsuarios
    }//GEN-LAST:event_jmGestionUsuarioActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.setVisible(false);

        DialogGestionProveedores dialog = new DialogGestionProveedores(this, true);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menuACiudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuACiudadActionPerformed
        this.setVisible(false);

        DialogoGestionCiudades dialog = new DialogoGestionCiudades(this, true);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_menuACiudadActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        this.setVisible(false);

        DialogGestionClientes dialog = new DialogGestionClientes(this, true);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void menuUMedidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUMedidasActionPerformed
        this.setVisible(false);
        DialogGestionUMedidas dialog = new DialogGestionUMedidas(this, true);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_menuUMedidasActionPerformed

    private void menuTProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTProductoActionPerformed
        this.setVisible(false);
        DialogGestionTipoProducto dialog = new DialogGestionTipoProducto(this, true);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_menuTProductoActionPerformed

    private void menuDepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDepositoActionPerformed
        this.setVisible(false);
        DialogGestionDepositos dialog = new DialogGestionDepositos(this, true);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_menuDepositoActionPerformed

    private void jmComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmComprasActionPerformed
        this.setVisible(false);
        DialogGestionProducto dialog = new DialogGestionProducto(this, true);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_jmComprasActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        this.setVisible(false);
        DialogGestionVenta dialog = new DialogGestionVenta(this, true, usuarioLogueado);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        this.setVisible(false);
        DialogAnularCompra dialog = new DialogAnularCompra(this, true);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        this.setVisible(false);
        DialogVistaStock dialog = new DialogVistaStock(this, true);
        dialog.setVisible(true);

        this.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea cerrar la sesión actual?",
                "Confirmar cierre de sesión.",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            Login login = new Login();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            this.dispose();
        }


    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
         abrirManualPDF();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jmCompras;
    private javax.swing.JMenuItem jmGestionUsuario;
    private javax.swing.JMenuBar jmPrincipal;
    private javax.swing.JPanel jplPrincipal;
    private javax.swing.JMenuItem menuACiudad;
    private javax.swing.JMenu menuCliente;
    private javax.swing.JMenuItem menuDeposito;
    private javax.swing.JMenuItem menuSalir;
    private javax.swing.JMenuItem menuTProducto;
    private javax.swing.JMenuItem menuUMedidas;
    // End of variables declaration//GEN-END:variables
}
