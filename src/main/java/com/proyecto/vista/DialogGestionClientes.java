package com.proyecto.vista;

import com.proyecto.controlador.CiudadController;
import com.proyecto.controlador.ClienteController;
import com.proyecto.modelo.Ciudad;
import com.proyecto.modelo.Cliente;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DialogGestionClientes extends javax.swing.JDialog {

    ClienteController service = new ClienteController();
    CiudadController ciudadController;
    Cliente c;
    DefaultTableModel modelo;

    public DialogGestionClientes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("Gestion Cliente");
        this.setLocationRelativeTo(null);
        cargarCiudadesEnComboBox();
        bloquearCasillas();
        listarCliente();
    }

    public void bloquearCasillas() {
        txtBuscar.setEnabled(false);
        txtRucCi.setEnabled(false);
        txtNombre.setEnabled(false);
        txtApellido.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtTelefono.setEnabled(false);
        cboCiudad.setEnabled(false);
        txtId.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(false);
    }

    public void desbloquearCasillas() {
        txtBuscar.setEnabled(true);
        txtRucCi.setEnabled(true);
        txtNombre.setEnabled(true);
        txtApellido.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        cboCiudad.setEnabled(true);
        btnBuscar.setEnabled(true);
        btnEditar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnActualizar.setEnabled(true);
    }

    private void cargarCiudadesEnComboBox() {
        ciudadController = new CiudadController();
        List<Ciudad> ciudades = ciudadController.listarCiudades();

        DefaultComboBoxModel<Ciudad> modeloCombo = new DefaultComboBoxModel<>();
        modeloCombo.addElement(new Ciudad(null, "-- Seleccione una ciudad --"));

        for (Ciudad ci : ciudades) {
            modeloCombo.addElement(ci);
        }

        cboCiudad.setModel(modeloCombo);
    }

    public void pasarDatos() {
        int fila = tblCliente.getSelectedRow();

        String id = tblCliente.getValueAt(fila, 0).toString().trim();
        String nombreCiudad = tblCliente.getValueAt(fila, 1).toString().trim();
        String rucIc = tblCliente.getValueAt(fila, 2).toString().trim();
        String nombre = tblCliente.getValueAt(fila, 3).toString().trim();
        String apellido = tblCliente.getValueAt(fila, 4).toString().trim();
        String direccion = tblCliente.getValueAt(fila, 5).toString().trim();
        String telefono = tblCliente.getValueAt(fila, 6).toString().trim();

        txtId.setText(id);
        //pasar el nombre al combo box
        for (int i = 0; i < cboCiudad.getItemCount(); i++) {
            Ciudad ciudad = cboCiudad.getItemAt(i);
            if (ciudad.getNombreCiudad().equalsIgnoreCase(nombreCiudad)) {
                cboCiudad.setSelectedIndex(i);
                break;
            }
        }
        txtRucCi.setText(rucIc);
        txtNombre.setText(nombre);
        txtApellido.setText(apellido);
        txtDireccion.setText(direccion);
        txtTelefono.setText(telefono);

        /* 
        // pasar el id al combo box
        for (int i = 0; i < cboCiudad.getItemCount(); i++) {
            Ciudad ciudad = cboCiudad.getItemAt(i);
            if (ciudad.getId().equals(ciudadId)) {
                cboCiudad.setSelectedIndex(i);
                break;
            }
        }*/
    }

    public void limpiarCampos() {
        cboCiudad.setSelectedIndex(0);
        txtRucCi.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtBuscar.setText("");
        txtId.setText("");
    }

    public void listarCliente() {

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("ID");
        modelo.addColumn("CIUDAD");
        modelo.addColumn("RUC");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("DIRECCION");
        modelo.addColumn("TELEFONO");

        tblCliente.setModel(modelo);

        // ajustamos el ancho de las columnas
        tblCliente.getColumnModel().getColumn(0).setPreferredWidth(10);  // ID
        tblCliente.getColumnModel().getColumn(1).setPreferredWidth(50);  // CIUDAD 
        tblCliente.getColumnModel().getColumn(2).setPreferredWidth(30);  // RUC
        tblCliente.getColumnModel().getColumn(3).setPreferredWidth(30);  // NOMBRE 
        tblCliente.getColumnModel().getColumn(4).setPreferredWidth(30);  // APELLIDO
        tblCliente.getColumnModel().getColumn(5).setPreferredWidth(80);  // DIRECCION
        tblCliente.getColumnModel().getColumn(6).setPreferredWidth(30);  // TELEFONO

        List<Cliente> lista = service.listar();
        for (Cliente cl : lista) {
            Object[] fila = new Object[7];
            fila[0] = cl.getId();
            fila[1] = cl.getCiudad().getNombreCiudad();
            fila[2] = cl.getRucCI();
            fila[3] = cl.getNombreCli();
            fila[4] = cl.getApellidoCli();
            fila[5] = cl.getDireccionCli();
            fila[6] = cl.getTelefonoCli();

            modelo.addRow(fila);
        }

    }

    public void buscar() {

        String buscar = txtBuscar.getText().trim();

        if (buscar.isBlank()) {

            JOptionPane.showMessageDialog(null, "La casilla Buscar no debe quedar vacia", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        } else {
            modelo = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
                
            };
            modelo.addColumn("ID");
            modelo.addColumn("CIUDAD");
            modelo.addColumn("RUC");
            modelo.addColumn("NOMBRE");
            modelo.addColumn("APELLIDO");
            modelo.addColumn("DIRECCION");
            modelo.addColumn("TELEFONO");

            tblCliente.setModel(modelo);

            // ajustamos el ancho de las columnas
            tblCliente.getColumnModel().getColumn(0).setPreferredWidth(10);  // ID
            tblCliente.getColumnModel().getColumn(1).setPreferredWidth(50);  // CIUDAD 
            tblCliente.getColumnModel().getColumn(2).setPreferredWidth(30);  // RUC
            tblCliente.getColumnModel().getColumn(3).setPreferredWidth(30);  // NOMBRE 
            tblCliente.getColumnModel().getColumn(4).setPreferredWidth(30);  // APELLIDO
            tblCliente.getColumnModel().getColumn(5).setPreferredWidth(80);  // DIRECCION
            tblCliente.getColumnModel().getColumn(6).setPreferredWidth(30);  // TELEFONO

            List<Cliente> lista = service.buscar(buscar);

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No existe el cliente buscado", "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                listarCliente();
            }

            for (Cliente cl : lista) {
                Object[] fila = new Object[7];
                fila[0] = cl.getId();
                fila[1] = cl.getCiudad().getNombreCiudad();
                fila[2] = cl.getRucCI();
                fila[3] = cl.getNombreCli();
                fila[4] = cl.getApellidoCli();
                fila[5] = cl.getDireccionCli();
                fila[6] = cl.getTelefonoCli();

                modelo.addRow(fila);
            }

            txtBuscar.setText("");

        }

    }

    public void guardar() {
        // cboCiudad ya tiene el objeto ciudad con el id y nombre 
        Ciudad ciudadSeleccionada = (Ciudad) cboCiudad.getSelectedItem();

        if (ciudadSeleccionada == null || ciudadSeleccionada.getId() == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una ciudad válida.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rucCi = txtRucCi.getText().trim().toUpperCase().toUpperCase();
        String nombre = txtNombre.getText().trim().toUpperCase().toUpperCase();
        String apellido = txtApellido.getText().trim().toUpperCase().toUpperCase();
        String direccion = txtDireccion.getText().trim().toUpperCase().toUpperCase();
        String telefono = txtTelefono.getText().trim().toUpperCase().toUpperCase();

        if (rucCi.isBlank() || nombre.isBlank() || apellido.isBlank() || direccion.isBlank() || telefono.isBlank()) {
            JOptionPane.showMessageDialog(null, "Debe completar todas las casillas para guardar.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (service.existeRucCi(rucCi)) {//validacion doble 
            JOptionPane.showMessageDialog(null, "El ruc o ci del cliente ya existe, favor introdusca otro", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = new Cliente();
        c.setCiudad(ciudadSeleccionada);// en el controller definimos que queremos id 
        c.setRucCI(rucCi);
        c.setNombreCli(nombre);
        c.setApellidoCli(apellido);
        c.setDireccionCli(direccion);
        c.setTelefonoCli(telefono);

        if (service.guardar(c)) {

            JOptionPane.showMessageDialog(null, "Cliente guardado exitosamente", "Exito!",
                    JOptionPane.INFORMATION_MESSAGE);

            listarCliente();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el usuario", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editar() {

        int fila = tblCliente.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente de la tabla", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }

        Ciudad ciudadSeleccionada = (Ciudad) cboCiudad.getSelectedItem();

        if (ciudadSeleccionada == null || ciudadSeleccionada.getId() == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una ciudad válida.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Long id = Long.valueOf(txtId.getText().trim());
        String rucCi = txtRucCi.getText().trim().toUpperCase().toUpperCase();
        String nombre = txtNombre.getText().trim().toUpperCase().toUpperCase();
        String apellido = txtApellido.getText().trim().toUpperCase().toUpperCase();
        String direccion = txtDireccion.getText().trim().toUpperCase().toUpperCase();
        String telefono = txtTelefono.getText().trim().toUpperCase().toUpperCase();

        if (rucCi.isBlank() || nombre.isBlank() || apellido.isBlank() || direccion.isBlank() || telefono.isBlank()) {
            JOptionPane.showMessageDialog(null, "Debe completar todas las casillas para guardar.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = new Cliente();
        c.setId(id);
        c.setCiudad(ciudadSeleccionada);//id ciudad
        c.setRucCI(rucCi);
        c.setNombreCli(nombre);
        c.setApellidoCli(apellido);
        c.setDireccionCli(direccion);
        c.setTelefonoCli(telefono);

        service.editar(c);

        JOptionPane.showMessageDialog(null, "Usuario editado exitosamente", "Exito!",
                JOptionPane.INFORMATION_MESSAGE);

        listarCliente();
        limpiarCampos();
    }

    public void eliminar() {
        int fila = tblCliente.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente válido.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro que quieres eliminar este proveedor",
                    "Confirmar para eliminar.",
                    JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                Long id = Long.valueOf(txtId.getText().trim());
                service.eliminar(id);
                listarCliente();
                limpiarCampos();
                JOptionPane.showMessageDialog(null, "El cliente fue eliminado exitosamente", "Exito!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        listarCliente();
        limpiarCampos();
    }

    public Cliente getClienteSeleccionado() {
        return c;
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
        tblCliente = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        cboCiudad = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtRucCi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btActualizar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setText("Gestion Clientes");

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setText("Buscar Cliente:");

        txtBuscar.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        btnBuscar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        tblCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCliente);

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setText("Ciudad:");

        cboCiudad.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setText("Ruc / Ci:");

        txtRucCi.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setText("Nombre:");

        txtNombre.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        txtApellido.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel6.setText("Apellido:");

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel7.setText("Direccion:");

        txtDireccion.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel8.setText("Telefono");

        txtTelefono.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel9.setText("Id:");

        txtId.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

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

        btnGuardar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btActualizar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btActualizar.setText("ACCION");
        btActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btActualizarActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRucCi))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNombre))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtApellido))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDireccion))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(33, 33, 33))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(401, 401, 401)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboCiudad, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtRucCi, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btActualizarActionPerformed
        desbloquearCasillas();
    }//GEN-LAST:event_btActualizarActionPerformed

    private void tblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClienteMouseClicked
        if (evt.getClickCount() == 1) {
            pasarDatos();
        }
        if (evt.getClickCount() == 2) {
            int fila = tblCliente.getSelectedRow();
            if (fila != -1) {
                c = new Cliente();

                c.setId(Long.valueOf(tblCliente.getValueAt(fila, 0).toString().trim()));

                Ciudad cl = new Ciudad();
                cl.setNombreCiudad(tblCliente.getValueAt(fila, 1).toString().trim());
                c.setCiudad(cl);

                c.setRucCI(tblCliente.getValueAt(fila, 2).toString().trim());
                c.setNombreCli(tblCliente.getValueAt(fila, 3).toString().trim());
                c.setApellidoCli(tblCliente.getValueAt(fila, 4).toString().trim());
                c.setDireccionCli(tblCliente.getValueAt(fila, 5).toString().trim());
                c.setTelefonoCli(tblCliente.getValueAt(fila, 6).toString().trim());

                this.dispose();

            }
        }

    }//GEN-LAST:event_tblClienteMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        listarCliente();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        bloquearCasillas();
        limpiarCampos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogGestionClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogGestionClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogGestionClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogGestionClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogGestionClientes dialog = new DialogGestionClientes(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btActualizar;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<Ciudad> cboCiudad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCliente;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRucCi;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
