package com.proyecto.vista;

import com.proyecto.controlador.UsuarioController;
import com.proyecto.modelo.Usuario;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class DialogGestionUsuarios extends javax.swing.JDialog {

    UsuarioController service = new UsuarioController();
    Usuario usuario;
    DefaultTableModel modelo;

    public DialogGestionUsuarios(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("Gestion Usuario");
        this.setLocationRelativeTo(null);
        desabilitarCampos();
        listarUsuarios();
    }

    public void desabilitarCampos() {
        txtId.setEnabled(false);
        txtBuscar.setEnabled(false);
        txtNombre.setEnabled(false);
        txtNick.setEnabled(false);
        txtClave.setEnabled(false);
        cboNivel.setEnabled(false);
        cboEstado.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnInforme.setEnabled(false);
    }

    public void habilitarCampos() {
        txtBuscar.setEnabled(true);
        txtNombre.setEnabled(true);
        txtNick.setEnabled(true);
        txtClave.setEnabled(true);
        cboNivel.setEnabled(true);
        cboEstado.setEnabled(true);
        btnBuscar.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnEditar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnActualizar.setEnabled(true);
        btnInforme.setEnabled(true);
    }

    public void pasarDatos() {
        int fila = tblUsuario.getSelectedRow();

        String id = tblUsuario.getValueAt(fila, 0).toString().trim();
        String nombre = tblUsuario.getValueAt(fila, 1).toString().trim();
        String nick = tblUsuario.getValueAt(fila, 2).toString().trim();
        String clave = tblUsuario.getValueAt(fila, 3).toString().trim();
        String nivel = tblUsuario.getValueAt(fila, 4).toString().trim();
        String estado = tblUsuario.getValueAt(fila, 5).toString().trim();

        txtId.setText(id);
        txtNombre.setText(nombre);
        txtNick.setText(nick);
        txtClave.setText(clave);
        cboNivel.setSelectedItem(nivel);
        cboEstado.setSelectedItem(estado);

    }

    public void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtNick.setText("");
        txtClave.setText("");
        cboNivel.setSelectedIndex(0);
        cboEstado.setSelectedIndex(0);
        txtNombre.requestFocus();
    }

    public void listarUsuarios() {

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        modelo.addColumn("ID");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("NICK");
        modelo.addColumn("CLAVE");
        modelo.addColumn("NIVEL");
        modelo.addColumn("ESTADO");

        tblUsuario.setModel(modelo);

        // ajustamos el ancho de las columnas
        tblUsuario.getColumnModel().getColumn(0).setPreferredWidth(10);  // ID
        tblUsuario.getColumnModel().getColumn(1).setPreferredWidth(20);  // NOMBRE 
        tblUsuario.getColumnModel().getColumn(2).setPreferredWidth(20);  // NICK 
        tblUsuario.getColumnModel().getColumn(3).setPreferredWidth(90);  // CLAVE
        tblUsuario.getColumnModel().getColumn(4).setPreferredWidth(40);  // NIVEL 
        tblUsuario.getColumnModel().getColumn(5).setPreferredWidth(20);  // ESTADO 

        List<Usuario> lista = service.listar();

        for (Usuario u : lista) {
            Object[] fila = new Object[6];
            fila[0] = u.getId();
            fila[1] = u.getNombre();
            fila[2] = u.getNick();
            fila[3] = u.getClave();
            fila[4] = u.getNivel();
            fila[5] = u.getEstado();

            modelo.addRow(fila);
        }

    }

    public void buscarUsuarios() {

        String buscar = txtBuscar.getText().trim();

        if (buscar.isBlank()) {
            JOptionPane.showMessageDialog(null, "La casilla Buscar no debe quedar vacia", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        } else {

            modelo = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

            };

            // ajustamos el ancho de las columnas
            tblUsuario.getColumnModel().getColumn(0).setPreferredWidth(10);  // ID
            tblUsuario.getColumnModel().getColumn(1).setPreferredWidth(20);  // NOMBRE 
            tblUsuario.getColumnModel().getColumn(2).setPreferredWidth(20);  // NICK 
            tblUsuario.getColumnModel().getColumn(3).setPreferredWidth(90);  // CLAVE
            tblUsuario.getColumnModel().getColumn(4).setPreferredWidth(40);  // NIVEL 
            tblUsuario.getColumnModel().getColumn(5).setPreferredWidth(20);  // ESTADO 

            modelo.addColumn("ID");
            modelo.addColumn("NOMBRE");
            modelo.addColumn("NICK");
            modelo.addColumn("CLAVE");
            modelo.addColumn("NIVEL");
            modelo.addColumn("ESTADO");

            tblUsuario.setModel(modelo);

            List<Usuario> listar = service.buscarUsuarios(buscar);
            System.out.println("listador: " + listar);

            if (listar.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No existe usuarios con ese nick", "Información",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {

                for (Usuario u : listar) {
                    Object[] fila = new Object[6];
                    fila[0] = u.getId();
                    fila[1] = u.getNombre();
                    fila[2] = u.getNick();
                    fila[3] = u.getClave();
                    fila[4] = u.getNivel();
                    fila[5] = u.getEstado();

                    modelo.addRow(fila);
                }

                txtBuscar.setText("");
                txtNombre.requestFocus();
            }
        }

    }

    public void guardar() {
        String nombre = txtNombre.getText().trim().toUpperCase();
        String nick = txtNick.getText().trim().toUpperCase();
        String clave = txtClave.getText().trim();
        String nivel = cboNivel.getSelectedItem().toString().toUpperCase();
        String estado = cboEstado.getSelectedItem().toString().toUpperCase();

        if (nombre.isBlank() || nick.isBlank() || clave.isBlank()
                || nivel.isBlank() || estado.equals("-- Seleccionar --")) {
            JOptionPane.showMessageDialog(null, "Para agregar debe completar todas las casillas", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }

        if (service.existeNick(nick)) {
            JOptionPane.showMessageDialog(this, "El nick ya existe, elija otro", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtNick.requestFocus();
            txtNick.selectAll();
            return;
        }

        usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setNick(nick);
        usuario.setClave(clave);
        usuario.setNivel(nivel);
        usuario.setEstado(estado);

        if (service.guardar(usuario)) {
            JOptionPane.showMessageDialog(null, "Usuario guardado exitosamente", "Exito!",
                    JOptionPane.INFORMATION_MESSAGE);

            limpiarCampos();
            listarUsuarios();

        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el usuario", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void editar() {

        int fila = tblUsuario.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente de la tabla", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }

        Long id = Long.valueOf(txtId.getText().trim());
        String nombre = txtNombre.getText().trim().toUpperCase();
        String nick = txtNick.getText().trim().toUpperCase();
        String clave = txtClave.getText().trim().toUpperCase();
        String nivel = cboNivel.getSelectedItem().toString().toUpperCase();
        String estado = cboEstado.getSelectedItem().toString().toUpperCase();

        if (nombre.isBlank() || nick.isBlank() || clave.isBlank()
                || nivel.isBlank() || estado.equalsIgnoreCase("-- Seleccionar --")) {

            JOptionPane.showMessageDialog(null, "Para editar debe completar todas las casillas", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuario = new Usuario();

        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setNick(nick);
        usuario.setClave(clave);
        usuario.setNivel(nivel);
        usuario.setEstado(estado);

        service.editar(usuario);

        JOptionPane.showMessageDialog(null, "Usuario editado exitosamente", "Exito!",
                JOptionPane.INFORMATION_MESSAGE);
        limpiarCampos();
        listarUsuarios();

    }

    public void Eliminar() {
        int fila = tblUsuario.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente de la tabla para borrar", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        } else {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este usuario?",
                    "Confirmar Inactivación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                Long id = Long.parseLong(txtId.getText().trim());
                service.Eliminar(id);
                limpiarCampos();
                listarUsuarios();
                JOptionPane.showMessageDialog(null, "El usuario fue elminado exitosamente", "Exito!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuario = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNick = new javax.swing.JTextField();
        txtClave = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        cboEstado = new javax.swing.JComboBox<>();
        btnActualizar = new javax.swing.JButton();
        btnAccion = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnInforme = new javax.swing.JButton();
        cboNivel = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setText("Gestion Usuarios");

        tblUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsuario);

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setText("Buscar Usuario:");

        txtBuscar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setText("Nombre:");

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setText("Clave:");

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setText("Nick:");

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel6.setText("Nivel:");

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel7.setText("Estado:");

        txtNick.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        txtClave.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        txtNombre.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel8.setText("Id:");

        txtId.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        btnEditar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnEditar.setText("EDITAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        cboEstado.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        cboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccionar --", "A", "I" }));

        btnActualizar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnAccion.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnAccion.setText("ACCION");
        btnAccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccionActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnInforme.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnInforme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/informe.png"))); // NOI18N
        btnInforme.setText("INFORME");
        btnInforme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInformeActionPerformed(evt);
            }
        });

        cboNivel.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        cboNivel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccionar --", "ADMINISTRADOR", "VENDEDOR", "COMPRADOR" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(321, 321, 321)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNick, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboNivel, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnInforme)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAccion, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAccion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNick, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboNivel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInforme, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        listarUsuarios();
        limpiarCampos();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarUsuarios();
        limpiarCampos();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Eliminar();
        limpiarCampos();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        editar();
        limpiarCampos();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
        limpiarCampos();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tblUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuarioMouseClicked
        pasarDatos();
    }//GEN-LAST:event_tblUsuarioMouseClicked

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitarCampos();
        limpiarCampos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccionActionPerformed
        habilitarCampos();
    }//GEN-LAST:event_btnAccionActionPerformed

    private void btnInformeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInformeActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar informe como...");//título de la ventana 
        fileChooser.setSelectedFile(new File("informe_usuarios.pdf")); // sugerimos un nombre

        // filtramos para que solo se muestren o guarden archivos con extensión .pdf
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos PDF", "pdf");
        fileChooser.setFileFilter(filtro);

        // muestra el cuadro de diálogo para guardar el archivo
        int seleccion = fileChooser.showSaveDialog(this);

        //si el usuario aprueba se guarda el pdf 
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String ruta = archivo.getAbsolutePath();

            // nos aseguramos que el nombre del archivo termine en .pdf
            if (!ruta.toLowerCase().endsWith(".pdf")) {
                ruta += ".pdf";
            }

            // llamamos al metodo con la ruta seleccionada
            service.generarInformeUsuarios(ruta);
        }
    }//GEN-LAST:event_btnInformeActionPerformed

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        if (!(Character.isLetter(evt.getKeyChar()))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreKeyTyped

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogGestionUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogGestionUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogGestionUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogGestionUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogGestionUsuarios dialog = new DialogGestionUsuarios(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAccion;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnInforme;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JComboBox<String> cboNivel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUsuario;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtClave;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNick;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
