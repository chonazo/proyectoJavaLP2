package com.proyecto.vista;

import com.proyecto.controlador.ProductoController;
import com.proyecto.controlador.TipoProductoController;
import com.proyecto.modelo.Producto;
import com.proyecto.modelo.TipoProducto;
import com.proyecto.modelo.UMedida;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DialogGestionProducto extends javax.swing.JDialog {

    ProductoController service = new ProductoController();
    Producto producto;
    TipoProductoController tipoProductoController;
    //UMedidaController uMedidaController;
    UMedida UMedidaActual;
    DefaultTableModel modelo;

    public DialogGestionProducto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Gestion Producto");
        pasarTipoProductoComboBox();
        //pasarUMedidasComboBox();
        listarProductos();
        desabilitarText();
    }

    public void cancelar() {
        cboTProducto.setSelectedIndex(0);
       // cboUMedida.setSelectedIndex(0);
        txtUMedidas.setText("");
        txtPrecio.setText("");
        txtCodBarra.setText("");
        txtBuscar.setText("");
        txtNProducto.setText("");
    }

    public void desabilitarText() {
        cboTProducto.setEnabled(false);
        txtUMedidas.setEnabled(false);
        txtPrecio.setEnabled(false);
        txtCodBarra.setEnabled(false);
        txtBuscar.setEnabled(false);
        txtNProducto.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnAgregar.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnEliminar.setEnabled(false);
        
    }

    public void habilitarText() {
        cboTProducto.setEnabled(true);
        txtCodBarra.setEnabled(true);
        txtBuscar.setEnabled(true);
        txtNProducto.setEnabled(true);
        btnActualizar.setEnabled(true);
        btnAgregar.setEnabled(true);
        btnBuscar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    private void pasarTipoProductoComboBox() {
        tipoProductoController = new TipoProductoController();
        List<TipoProducto> tiposProductos = tipoProductoController.listar();

        DefaultComboBoxModel<TipoProducto> modeloCombo = new DefaultComboBoxModel<>();
        modeloCombo.addElement(new TipoProducto(null, "-- Seleccione una ciudad --"));

        for (TipoProducto tp : tiposProductos) {
            modeloCombo.addElement(tp);
        }

        cboTProducto.setModel(modeloCombo);

    }

    /*private void pasarUMedidasComboBox() {
        uMedidaController = new UMedidaController();
        List<UMedida> uMedidas = uMedidaController.listar();

        DefaultComboBoxModel<UMedida> modeloCombo = new DefaultComboBoxModel<>();
        modeloCombo.addElement(new UMedida(null, "-- Seleccione una ciudad --"));

        for (UMedida um : uMedidas) {
            modeloCombo.addElement(um);
        }
        cboUMedida.setModel(modeloCombo);
    }*/

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
        
        // ajustamos el ancho de las columnas
        tblProducto.getColumnModel().getColumn(0).setPreferredWidth(10);  // ID
        tblProducto.getColumnModel().getColumn(1).setPreferredWidth(80);  // T. PRODUCTO 
        tblProducto.getColumnModel().getColumn(2).setPreferredWidth(30);  // U. MEDIDA
        tblProducto.getColumnModel().getColumn(3).setPreferredWidth(80);  // N. PRODUCTO 
        tblProducto.getColumnModel().getColumn(4).setPreferredWidth(30);  // PRECIO
        tblProducto.getColumnModel().getColumn(5).setPreferredWidth(30);  // COD. BARRA

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
        
         // ajustamos el ancho de las columnas
        tblProducto.getColumnModel().getColumn(0).setPreferredWidth(10);  // ID
        tblProducto.getColumnModel().getColumn(1).setPreferredWidth(80);  // T. PRODUCTO 
        tblProducto.getColumnModel().getColumn(2).setPreferredWidth(30);  // U. MEDIDA
        tblProducto.getColumnModel().getColumn(3).setPreferredWidth(80);  // N. PRODUCTO 
        tblProducto.getColumnModel().getColumn(4).setPreferredWidth(30);  // PRECIO
        tblProducto.getColumnModel().getColumn(5).setPreferredWidth(30);  // COD. BARRA

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

    public void guardar() {
        TipoProducto tProductoSeleccionado = (TipoProducto) cboTProducto.getSelectedItem();
        // validacion combobox tipo producto
        if (tProductoSeleccionado == null || tProductoSeleccionado.getId() == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un Tipo de producto válido.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        String UMedidas = txtUMedidas.getText().trim().toUpperCase();
        String nombre = txtNProducto.getText().trim().toUpperCase();
        String codBarra = txtCodBarra.getText().trim().toUpperCase();

        if (nombre.isBlank() || codBarra.isBlank() || UMedidas.isBlank()) {
            JOptionPane.showMessageDialog(null, "Debe completar todas las casillas para guardar.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (service.existeProductoCodBarra(codBarra)) {
            JOptionPane.showMessageDialog(null, "Ya existe un codigo de barra del producto ingresado, por favor ingrese otro",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            txtCodBarra.requestFocus();
            txtCodBarra.selectAll();
            return;
        }

        // procedemos a guardar 
        producto = new Producto();
        producto.setTipoProducto(tProductoSeleccionado);
        producto.setuMedida(UMedidaActual);
        producto.setNombreProducto(nombre);
        producto.setPrecio(0);
        producto.setCodBarra(codBarra);

        if (service.guardar(producto)) {
            JOptionPane.showMessageDialog(null, "Producto guardado exitosamente", "Exito!",
                    JOptionPane.INFORMATION_MESSAGE);
            listarProductos();

        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el producto", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        cancelar();

    }

    public void eliminar() {
        int fila = tblProducto.getSelectedRow();
        if (fila != -1) {

            int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro que quiere eliminar este producto?",
                    "Atencion", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                Long idProducto = Long.valueOf(tblProducto.getValueAt(fila, 0).toString().trim());
                service.eliminarProducto(idProducto);

                JOptionPane.showMessageDialog(this, "El producto fue eliminado de la tabla", "Exito!",
                        JOptionPane.INFORMATION_MESSAGE);

            }

        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto de la tabla para eliminar.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cboTProducto = new javax.swing.JComboBox<>();
        txtPrecio = new javax.swing.JTextField();
        txtCodBarra = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtUMedidas = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtNProducto = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setText("Gestion Productos");

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

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setText("Buscar Producto:");

        txtBuscar.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        btnBuscar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setText("Tipo Producto:");

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setText("Uni. Medidas:");

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setText("Precio:");

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel6.setText("Cod. Barra:");

        btnAgregar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnAgregar.setText("AGREGAR ");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel7.setText("Nom. Producto:");

        btnCancelar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton2.setText("ACCION");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(364, 364, 364)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboTProducto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtUMedidas, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrecio)
                                    .addComponent(txtCodBarra)
                                    .addComponent(txtNProducto))))))
                .addGap(9, 9, 9))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboTProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtUMedidas, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCodBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
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

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarProductos();
        cancelar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        guardar();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
        desabilitarText();
    }//GEN-LAST:event_btnCancelarActionPerformed

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

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        listarProductos();
        cancelar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        habilitarText();
        cancelar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
        listarProductos();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DialogGestionUMedidas dialog = new DialogGestionUMedidas(null, true);
        dialog.setVisible(true);

        UMedida u = dialog.getUMedidaSeleccionado();

        if (u != null) {
            txtUMedidas.setText(u.getDescripcion());
            Long id = u.getId();
            UMedidaActual = u;
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó ninguna Unidad de medida.",
                    "U Medida no seleccionado", JOptionPane.WARNING_MESSAGE);
        }
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
            java.util.logging.Logger.getLogger(DialogGestionProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogGestionProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogGestionProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogGestionProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogGestionProducto dialog = new DialogGestionProducto(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JComboBox<TipoProducto> cboTProducto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodBarra;
    private javax.swing.JTextField txtNProducto;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtUMedidas;
    // End of variables declaration//GEN-END:variables
}
