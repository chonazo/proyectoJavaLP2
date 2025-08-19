package com.proyecto.vista;

import com.proyecto.controlador.CompraController;
import com.proyecto.controlador.DepositoController;
import com.proyecto.controlador.DetalleCompraController;
import com.proyecto.controlador.ProductoController;
import com.proyecto.controlador.ProveedorController;
import com.proyecto.controlador.StockController;
import com.proyecto.controlador.UsuarioController;
import com.proyecto.modelo.Compra;
import com.proyecto.modelo.Deposito;
import com.proyecto.modelo.DetalleCompra;
import com.proyecto.modelo.Producto;
import com.proyecto.modelo.Proveedor;
import com.proyecto.modelo.Stock;
import com.proyecto.modelo.Usuario;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DialogGestionCompras extends javax.swing.JDialog {

    Proveedor proveedorActual = null;
    ProductoController productoController = new ProductoController();
    ProveedorController proController = new ProveedorController();
    Usuario usuarioActual = null; // contiene el objeto usuario
    UsuarioController usuController = new UsuarioController();
    Producto productoActual = null; // contiene el objeto producto
    Compra compraActual; // objeto compra guardado guardarCompraActual()
    CompraController service;
    DepositoController depoController; // contiene la lista de deposito para el combo box
    DetalleCompraController detaCompraService = new DetalleCompraController();
    StockController stockController = new StockController();
    DefaultTableModel modelo;

    public DialogGestionCompras(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Gestion de Compras");
        modeloTablaSinEditar();
        pasarDepositoComboBox();
        bloquearCompras();
        bloquearProducto();

    }

    public void modeloTablaSinEditar() {

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        modelo.addColumn("ID");
        modelo.addColumn("PRODUCTO");
        modelo.addColumn("COD. BARRA");
        modelo.addColumn("CANTIDAD");
        modelo.addColumn("PRECIO");
        modelo.addColumn("DEPOSITO");
        modelo.addColumn("SUBTOTAL");
        tblCompra.setModel(modelo);
    }

    public void bloquearCompras() {
        txtProveedor.setEnabled(false);
        jdcFCompra.setEnabled(false);
        txtFactura.setEnabled(false);
        txtUsuario.setEnabled(false);
        btnBProveedor.setEnabled(false);
        btnBUsuario.setEnabled(false);
        btnCoCancelar.setEnabled(false);
        btnCoAceptar.setEnabled(false);
    }

    public void desbloquearCompras() {
        //txtProveedor.setEnabled(true);
        jdcFCompra.setEnabled(true);
        txtFactura.setEnabled(true);
        //txtUsuario.setEnabled(true);
        btnBProveedor.setEnabled(true);
        btnBUsuario.setEnabled(true);
        btnCoCancelar.setEnabled(true);
        btnCoAceptar.setEnabled(true);
    }

    public void bloquearProducto() {
        txtProductoCompra.setEnabled(false);
        txtCantidadPro.setEnabled(false);
        txtPrecioCo.setEnabled(false);
        cboDepositoCo.setEnabled(false);
        btnCancelarCo.setEnabled(false);
        btnAgregarCo.setEnabled(false);
        btnBProducto.setEnabled(false);
    }

    public void desbloquearProducto() {
        //txtProductoCompra.setEnabled(true);
        txtCantidadPro.setEnabled(true);
        txtPrecioCo.setEnabled(true);
        cboDepositoCo.setEnabled(true);
        btnCancelarCo.setEnabled(true);
        btnAgregarCo.setEnabled(true);
        btnBProducto.setEnabled(true);
    }

    public void limpiarTextCo() {
        txtProductoCompra.setText("");
        txtCantidadPro.setText("");
        txtPrecioCo.setText("");
        cboDepositoCo.setSelectedIndex(0);
    }

    public void limpiarText() {
        txtProveedor.setText("");
        jdcFCompra.setDate(null);
        txtFactura.setText("");
        txtUsuario.setText("");
    }

    public void pasarDepositoComboBox() {
        depoController = new DepositoController();
        List<Deposito> depositos = depoController.listar();
        DefaultComboBoxModel<Deposito> modeloCombo = new DefaultComboBoxModel<>();
        modeloCombo.addElement(new Deposito(null, "-- Seleccione un deposito --"));

        for (Deposito d : depositos) {
            modeloCombo.addElement(d);
        }

        cboDepositoCo.setModel(modeloCombo);
    }

    public Compra guardarCompraActual() {

        String proveedor = txtProveedor.getText().trim().toUpperCase();
        String numFactura = txtFactura.getText().trim().toUpperCase();
        String nickUser = txtUsuario.getText().trim().toUpperCase();
        String estado = "T";
        Date fechaCompra = jdcFCompra.getDate();

        if (proveedor.isBlank() || numFactura.isBlank() || nickUser.isBlank() || fechaCompra == null) {
            JOptionPane.showMessageDialog(this, "Debe completar todas las casillas.",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Compra compra = new Compra();

        // convertimos a java.sql.Date 
        java.sql.Date fechaRegistro = new java.sql.Date(System.currentTimeMillis());
        java.sql.Date fechaCompraSql = new java.sql.Date(fechaCompra.getTime());

        compra.setFechaCompra(fechaCompraSql);
        compra.setFechaRegistro(fechaRegistro);
        compra.setNumFactura(numFactura);
        compra.setEstado(estado);
        compra.setProveedor(this.proveedorActual);
        compra.setUsuario(this.usuarioActual);

        service = new CompraController();
        Compra compraInsertada = service.guardar(compra);

        if (compraInsertada != null) {
            this.compraActual = compraInsertada;
            JOptionPane.showMessageDialog(null, "Compra registrada exitosamente.", "Inserción correcta",
                    JOptionPane.INFORMATION_MESSAGE);
            return compraInsertada;
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo registrar la compra. Verifique si la factura ya existe.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void agregarDetalleATabla() {
        // validar producto
        if (productoActual == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validar cantidad
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidadPro.getText().trim());
            if (cantidad <= 0) {

                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this, "Ingrese una cantidad valida y debe ser mayor a cero.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validar precio
        int precio;
        try {
            precio = Integer.parseInt(txtPrecioCo.getText().trim());
            if (precio <= 0) {

                JOptionPane.showMessageDialog(this, "El precio debe ser mayor que cero.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this, "Ingrese un precio valido y debe ser mayor a cero.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validar deposito
        Deposito deposito = (Deposito) cboDepositoCo.getSelectedItem();
        if (deposito == null || deposito.getId() == null
                || deposito.getDescriDeposito().equalsIgnoreCase("-- Seleccione un deposito --")) {

            JOptionPane.showMessageDialog(this, "Debe seleccionar un depósito.",
                    "Error de depósito",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //subtotal
        Integer subTotal = precio * cantidad;

        //recuperamos el modelo
        modelo = (DefaultTableModel) tblCompra.getModel();

        //verificamos que no haya duplicado en la tabla 
        Long idProductoActual = productoActual.getId();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Long idTable = Long.valueOf(modelo.getValueAt(i, 0).toString().trim()); // columna 0 = id del producto
            if (idProductoActual.equals(idTable)) {
                JOptionPane.showMessageDialog(this, "Este producto ya fue agregado, verifique la cantidad en ves de duplicar",
                        "Producto duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        //ingresamos datos a la tabla
        Object[] fila = new Object[7];
        fila[0] = productoActual.getId();
        fila[1] = productoActual.getNombreProducto();
        fila[2] = productoActual.getCodBarra();
        fila[3] = cantidad;
        fila[4] = precio;
        fila[5] = deposito;
        fila[6] = subTotal;

        modelo.addRow(fila);

        // limpiamos todo cada que se inserta un producto
        limpiarTextCo();
        productoActual = null;
    }

    public void pasarDatos() {
        int fila = tblCompra.getSelectedRow();
        String producto = tblCompra.getValueAt(fila, 1).toString().trim();
        String cantidad = tblCompra.getValueAt(fila, 3).toString().trim();
        String precio = tblCompra.getValueAt(fila, 4).toString().trim();
        String deposito = tblCompra.getValueAt(fila, 5).toString().trim();

        txtProductoCompra.setText(producto);
        txtCantidadPro.setText(cantidad);
        txtPrecioCo.setText(precio);

        //pasamos datos cbo
        for (int i = 0; i < cboDepositoCo.getItemCount(); i++) {
            Deposito d = cboDepositoCo.getItemAt(i);
            if (d.getDescriDeposito().equalsIgnoreCase(deposito)) {
                cboDepositoCo.setSelectedIndex(i);
                break;
            }
        }

        // reconstruimos productoActual desde la tabla
        productoActual = new Producto();
        productoActual.setId(Long.valueOf(tblCompra.getValueAt(fila, 0).toString()));
        productoActual.setNombreProducto(tblCompra.getValueAt(fila, 1).toString());
        productoActual.setCodBarra(tblCompra.getValueAt(fila, 2).toString());
    }

    public void editarProductoEnTabla() {
        int fila = tblCompra.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila de la tabla para editar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (productoActual == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto de la tabla válido para editar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidadPro.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que cero.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cantidad inválida.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int precio;
        try {
            precio = Integer.parseInt(txtPrecioCo.getText().trim());
            if (precio <= 0) {
                JOptionPane.showMessageDialog(null, "El precio debe ser mayor que cero.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Precio inválido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Deposito deposito = (Deposito) cboDepositoCo.getSelectedItem();
        if (deposito == null || deposito.getId() == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un depósito válido para editar..",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int subtotal = cantidad * precio;

        modelo = (DefaultTableModel) tblCompra.getModel();
        modelo.setValueAt(productoActual.getId(), fila, 0);
        modelo.setValueAt(productoActual.getNombreProducto(), fila, 1);
        modelo.setValueAt(productoActual.getCodBarra(), fila, 2);
        modelo.setValueAt(cantidad, fila, 3);
        modelo.setValueAt(precio, fila, 4);
        modelo.setValueAt(deposito.getDescriDeposito(), fila, 5);
        modelo.setValueAt(subtotal, fila, 6);

        JOptionPane.showMessageDialog(null, "El registro fue editado correctamente",
                "Exito!", JOptionPane.INFORMATION_MESSAGE);

        limpiarTextCo();
        productoActual = null;
    }

    public void limpiarTabla() {
        modelo = (DefaultTableModel) tblCompra.getModel();
        modelo.setRowCount(0);
        limpiarTextCo();
        productoActual = null;
    }

    public void eliminarFilaTabla() {
        int fila = tblCompra.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar este producto?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            modelo = (DefaultTableModel) tblCompra.getModel();
            modelo.removeRow(fila);
            limpiarTextCo();
            productoActual = null;
        }
    }

    public void guardarDetallesYStock() {
        if (compraActual == null) {
            JOptionPane.showMessageDialog(null, "No hay compra actual registrada.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modelo = (DefaultTableModel) tblCompra.getModel();
        DetalleCompraController detalleController = new DetalleCompraController();
        StockController stockController = new StockController();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Long idProducto = Long.valueOf(modelo.getValueAt(i, 0).toString());
            int cantidad = Integer.parseInt(modelo.getValueAt(i, 3).toString());
            int precioUnit = Integer.parseInt(modelo.getValueAt(i, 4).toString());

            Deposito deposito = (Deposito) modelo.getValueAt(i, 5);

            Producto producto = new Producto();
            producto.setId(idProducto);

            // insertamos el detalle de compra
            DetalleCompra d = new DetalleCompra();
            d.setCompra(compraActual);
            d.setProducto(producto);
            d.setCantidad(cantidad);
            d.setPrecio(precioUnit);
            d.setDeposito(deposito);

            detaCompraService.guardar(d);

            // === actualizamos precio de producto 25% ===
            int nuevoPrecioVenta = (int) (precioUnit * 1.25);
            productoController = new ProductoController();
            Producto productoActualizado = productoController.buscarProductoPorId(idProducto);

            if (productoActualizado != null) {
                productoActualizado.setPrecio(nuevoPrecioVenta);
                productoController.actualizarPrecio(productoActualizado);
            }

            Stock stockExistente = stockController.buscarStock(idProducto, deposito.getId());

            if (stockExistente != null) {//si existe en stock el producto actualizamos update
                stockExistente.setCantidad(stockExistente.getCantidad() + cantidad);
                stockController.actualizar(stockExistente);

            } else { // si no existe el producto insertamos insert
                Stock nuevoStock = new Stock();
                nuevoStock.setProducto(producto);
                nuevoStock.setDeposito(deposito);
                nuevoStock.setCantidad(cantidad);

                stockController.guardar(nuevoStock);

            }

        }
        JOptionPane.showMessageDialog(this, "La compra fue realizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarTabla(); // ya lo tienes implementado
        limpiarText();  // limpiar cabecera de compra
        bloquearProducto();
        bloquearCompras();
        compraActual = null;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jdcFCompra = new com.toedter.calendar.JDateChooser();
        txtUsuario = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtProveedor = new javax.swing.JTextField();
        btnBUsuario = new javax.swing.JButton();
        btnBProveedor = new javax.swing.JButton();
        txtFactura = new javax.swing.JTextField();
        btnCoCancelar = new javax.swing.JButton();
        btnCoAceptar = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCompra = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtProductoCompra = new javax.swing.JTextField();
        btnBProducto = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtCantidadPro = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPrecioCo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboDepositoCo = new javax.swing.JComboBox<>();
        btnAgregarCo = new javax.swing.JButton();
        btnCancelarCo = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setText("Gestion Compras");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Compra", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 14))); // NOI18N
        jPanel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel3.setText("PROVEEDOR");

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel4.setText("Nº FACTURA");

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel6.setText("F. COMPRA");

        jdcFCompra.setDateFormatString("yyyy-MM-dd");
        jdcFCompra.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        txtUsuario.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel8.setText("USUARIO");

        txtProveedor.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        btnBUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBUsuarioActionPerformed(evt);
            }
        });

        btnBProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBProveedorActionPerformed(evt);
            }
        });

        txtFactura.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        btnCoCancelar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnCoCancelar.setText("CANCELAR");
        btnCoCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCoCancelarActionPerformed(evt);
            }
        });

        btnCoAceptar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnCoAceptar.setText("ACEPTAR");
        btnCoAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCoAceptarActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/check.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcFCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtFactura)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCoAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCoCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnCoCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCoAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdcFCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        tblCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCompraMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCompra);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel2.setText("PRODUCTO");

        txtProductoCompra.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        btnBProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBProductoActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel7.setText("CANTIDAD");

        txtCantidadPro.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel10.setText("PRECIO");

        txtPrecioCo.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel11.setText("DEPOSITO");

        cboDepositoCo.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        btnAgregarCo.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnAgregarCo.setText("AGREGAR");
        btnAgregarCo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCoActionPerformed(evt);
            }
        });

        btnCancelarCo.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnCancelarCo.setText("CANCELAR");
        btnCancelarCo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarCoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrecioCo))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnBProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtCantidadPro, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cboDepositoCo, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAgregarCo, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addComponent(btnCancelarCo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtProductoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioCo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboDepositoCo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAgregarCo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCantidadPro, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancelarCo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21))
        );

        jButton5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton5.setText("EDITAR");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton6.setText("ELIMINAR");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton7.setText("LIMPIAR");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton1.setText("GENERAR COMPRA");
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
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 761, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(17, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(351, 351, 351)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(333, 333, 333)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBProveedorActionPerformed
        DialogBuscarProveedor dialog = new DialogBuscarProveedor(null, true);
        dialog.setVisible(true);

        Proveedor p = dialog.getProveedorSeleccionado();

        if (p != null) {
            txtProveedor.setText(p.getRazonSocial());
            proveedorActual = p;
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún proveedor.",
                    "Proveedor no seleccionado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnBProveedorActionPerformed

    private void btnBUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBUsuarioActionPerformed
        DialogBuscarUser dialog = new DialogBuscarUser(null, true);
        dialog.setVisible(true);

        Usuario u = dialog.getUsuarioSeleccionado();
        if (u != null) {
            txtUsuario.setText(u.getNick());
            usuarioActual = u;
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún usuario.",
                    "Usuario no seleccionado", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_btnBUsuarioActionPerformed

    private void btnBProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBProductoActionPerformed
        DialogGestionProducto dialog = new DialogGestionProducto(null, true);
        dialog.setVisible(true);

        Producto p = dialog.getProductoSeleccionado();
        if (p != null) {
            txtProductoCompra.setText(p.getNombreProducto());
            productoActual = p;
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún producto.",
                    "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnBProductoActionPerformed

    private void btnCoAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCoAceptarActionPerformed

        if (txtProveedor.getText().trim().isBlank() || jdcFCompra.getDate() == null || txtFactura.getText().trim().isBlank() || txtUsuario.getText().trim().isBlank()) {
            JOptionPane.showMessageDialog(null, "Ninguno de los registros debe quedar vacio para guardar la compra",
                    "Atencion!",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            bloquearCompras();
            desbloquearProducto();
        }


    }//GEN-LAST:event_btnCoAceptarActionPerformed

    private void btnCoCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCoCancelarActionPerformed
        int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere cancelar? se podrían borrar los registros ingresados",
                "Atención!",
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION);

        if (pregunta == JOptionPane.YES_OPTION) {
            limpiarText();
            bloquearCompras();
        }

    }//GEN-LAST:event_btnCoCancelarActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        desbloquearCompras();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void btnCancelarCoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarCoActionPerformed
        int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere cancelar? se borrarán los registros ingresados en producto y compra",
                "Atención!",
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION);

        if (pregunta == JOptionPane.YES_OPTION) {
            limpiarText();
            bloquearCompras();
            limpiarTextCo();
            bloquearProducto();
        }
    }//GEN-LAST:event_btnCancelarCoActionPerformed

    private void tblCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCompraMouseClicked
        if (evt.getClickCount() == 1) {
            pasarDatos();
        }
    }//GEN-LAST:event_tblCompraMouseClicked

    private void btnAgregarCoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCoActionPerformed
        agregarDetalleATabla();
    }//GEN-LAST:event_btnAgregarCoActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        editarProductoEnTabla();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        limpiarTabla();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        eliminarFilaTabla();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Compra compraGuardada = guardarCompraActual();
        if (compraGuardada != null) {
            guardarDetallesYStock();
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo completar la operación. Verifique los datos de la compra.",
                    "Error al guardar", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(DialogGestionCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogGestionCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogGestionCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogGestionCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogGestionCompras dialog = new DialogGestionCompras(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAgregarCo;
    private javax.swing.JButton btnBProducto;
    private javax.swing.JButton btnBProveedor;
    private javax.swing.JButton btnBUsuario;
    private javax.swing.JButton btnCancelarCo;
    private javax.swing.JButton btnCoAceptar;
    private javax.swing.JButton btnCoCancelar;
    private javax.swing.JComboBox<Deposito> cboDepositoCo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcFCompra;
    private javax.swing.JTable tblCompra;
    private javax.swing.JTextField txtCantidadPro;
    private javax.swing.JTextField txtFactura;
    private javax.swing.JTextField txtPrecioCo;
    private javax.swing.JTextField txtProductoCompra;
    private javax.swing.JTextField txtProveedor;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
