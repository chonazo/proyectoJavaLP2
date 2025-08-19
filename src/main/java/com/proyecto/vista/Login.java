package com.proyecto.vista;

import com.proyecto.controlador.UsuarioController;
import com.proyecto.modelo.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    private UsuarioController service;

    public Login() {
        initComponents();
        service = new UsuarioController();
        this.setLocationRelativeTo(null);
        this.setTitle("Login");

        this.txtPass.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtPass.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }

        });

        this.txtPass.addActionListener((ActionEvent e) -> {
            Login.this.btnIngresar.doClick();
        });

    }

    public void validarUsuario() {
        String user = txtNick.getText().trim().toUpperCase();
        String pass = txtPass.getText().trim();

        if (user.isBlank()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar su nick", "Error! debe ingresar su usuario", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pass.isBlank()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar su contraseña", "Error! debe ingresar su password", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuarioEncontrado = service.buscarPorNick(user);

        if (usuarioEncontrado != null) { //validamos el estado usuario
            if (usuarioEncontrado.getEstado().equalsIgnoreCase("I")) {
                JOptionPane.showMessageDialog(null, "El usuario está inactivo. Contacte con el administrador.", "Usuario inactivo", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // encriptamos la contraseña ingresada antes de comparar
        String passEncriptado = service.encriptarMD5(pass);

        if (usuarioEncontrado != null) {
            // Contraseña correcta
            if (usuarioEncontrado.getClave().equals(passEncriptado)) {
                String nivelUsuario = usuarioEncontrado.getNivel();//guardamos nivel de usuario para autenticar su rol
                System.out.println("Nivel Usuario: " + nivelUsuario);

                Principal principal = new Principal(usuarioEncontrado); // pasamos a Principal el nivel de usuario
                principal.setVisible(true);
                principal.setSize(1024, 800);
                principal.setLocationRelativeTo(null);
                principal.setTitle("Menu Principal -- Usuario : " + usuarioEncontrado.getNombre());
                dispose();

            } else {
                // Contraseña incorrecta
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "Error de contraseña!", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            // Usuario no encontrado
            JOptionPane.showMessageDialog(null, "El Usuario no existe en la base de datos", "Usuario no encontrado", JOptionPane.ERROR_MESSAGE);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblLogin = new javax.swing.JLabel();
        txtNick = new javax.swing.JTextField();
        lblUser = new javax.swing.JLabel();
        txtPass = new javax.swing.JPasswordField();
        lblPass = new javax.swing.JLabel();
        btnIngresar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/login.png"))); // NOI18N
        jPanel1.add(lblLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 180, 160));

        txtNick.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel1.add(txtNick, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 260, 30));

        lblUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user.png"))); // NOI18N
        jPanel1.add(lblUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 30, 30));

        txtPass.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        txtPass.setText("jPasswordField1");
        jPanel1.add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 260, 30));

        lblPass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pass.png"))); // NOI18N
        jPanel1.add(lblPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 30, 30));

        btnIngresar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnIngresar.setText("INGRESAR");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        jPanel1.add(btnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 130, 50));

        jButton1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton1.setText("CANCELAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 390, 130, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 480));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        validarUsuario();
    }//GEN-LAST:event_btnIngresarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        txtNick.setText("");
        txtPass.setText("************");
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {
        // Manejador de excepciones global para toda la aplicación
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            ex.printStackTrace();
            String errorMsg = "Error no controlado:\n" + ex.toString();
            if (javax.swing.SwingUtilities.isEventDispatchThread()) {
                JOptionPane.showMessageDialog(null, errorMsg, "Error Crítico", JOptionPane.ERROR_MESSAGE);
            } else {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, errorMsg, "Error Crítico", JOptionPane.ERROR_MESSAGE);
                });
            }
        });

        // Tu código existente para el Look and Feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Resto de tu código main
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTextField txtNick;
    private javax.swing.JPasswordField txtPass;
    // End of variables declaration//GEN-END:variables
}
