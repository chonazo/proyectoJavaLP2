package com.proyecto.vista;

import com.proyecto.modelo.VentaPago;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DialogVentaPago extends javax.swing.JDialog {

    private int totalVenta;

    DialogGestionVenta padre;
    DefaultTableModel modelo;

    public DialogVentaPago(JDialog parent, boolean modal, int totalVenta) {
        super(parent, modal);
        this.padre = (DialogGestionVenta) parent;
        this.totalVenta = totalVenta;
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Forma de Pago");
        txtMonto.setEnabled(false);
        cboDescripcion.setEnabled(false);
        txtNumTarjeta.setEnabled(false);
        cabeceraTablaVentaPago();

    }

    public void cabeceraTablaVentaPago() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        modelo.addColumn("Forma de Pago");
        modelo.addColumn("Monto");
        modelo.addColumn("Descripción");
        modelo.addColumn("N° de Tarjeta");

        tblFormaPago.setModel(modelo);

    }

    public void formaPago() {

        String formaPago = cboFormaPago.getSelectedItem().toString();

        if (formaPago.equals("EFECTIVO")) {
            txtMonto.setEnabled(true);
            cboDescripcion.setEnabled(false);
            txtNumTarjeta.setEnabled(false);
            cboDescripcion.setSelectedIndex(0);
            txtNumTarjeta.setText("");

        } else if (formaPago.contains("TARJETA")) {
            txtMonto.setEnabled(true);
            cboDescripcion.setEnabled(true);
            txtNumTarjeta.setEnabled(true);

        } else {
            txtMonto.setEnabled(false);
            cboDescripcion.setEnabled(false);
            txtNumTarjeta.setEnabled(false);
        }
    }

    public void agregarPagoTabla() {
        String formaPago = cboFormaPago.getSelectedItem().toString();
        String descripcion = "";
        if (formaPago.equals("TARJETA DEBITO") || formaPago.equals("TARJETA CREDITO")) {
            descripcion = cboDescripcion.getSelectedItem() != null ? cboDescripcion.getSelectedItem().toString() : "";
        }
        String montoStr = txtMonto.getText().trim();
        String nroTarjeta = txtNumTarjeta.getText().trim();

        if (formaPago.equals("-- Elegir F. Pago --") || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos obligatorios.",
                    "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cboDescripcion.equals("-- Elegir Tarjeta --") || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos obligatorios.",
                    "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((formaPago.equals("TARJETA DEBITO") || formaPago.equals("TARJETA CREDITO"))
                && (cboDescripcion.getSelectedIndex() == 0 || montoStr.isEmpty())) {
            JOptionPane.showMessageDialog(null, "Debe elegir una tarjeta y completar el numero de comprobante.",
                    "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int monto;
        try {
            monto = Integer.parseInt(montoStr);
            if (monto <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El monto debe ser un número positivo.");
            return;
        }

        modelo = (DefaultTableModel) tblFormaPago.getModel();

        Object[] fila = new Object[4];
        fila[0] = formaPago;
        fila[1] = descripcion;
        fila[2] = montoStr;
        fila[3] = nroTarjeta;

        modelo.addRow(fila);

        actualizarResumenPagos();
        limpiarCamposPago();

    }

    private void actualizarResumenPagos() {
        int totalEfectivo = 0;
        int totalTarjetaDebito = 0;
        int totalTarjetaCredito = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String forma = modelo.getValueAt(i, 0).toString();
            int monto = Integer.parseInt(modelo.getValueAt(i, 2).toString());

            switch (forma) {
                case "EFECTIVO" ->
                    totalEfectivo += monto;
                case "TARJETA DEBITO" ->
                    totalTarjetaDebito += monto;
                case "TARJETA CREDITO" ->
                    totalTarjetaCredito += monto;
            }
        }

        int totalVenta = this.totalVenta;
        int totalPagado = totalEfectivo + totalTarjetaDebito + totalTarjetaCredito;

        // solo aplicamos descuento si todo se paga con tarjeta de credito
        int descuento = (totalPagado == totalTarjetaCredito && totalTarjetaCredito > 0) ? (int) (totalVenta * 0.15) : 0;
        int netoPagar = totalVenta - descuento;
        int vuelto = totalPagado - netoPagar;

        // mostramos en los txt correspondietes
        txtTotalEfectivo.setText(String.valueOf(totalEfectivo));
        txtTotalDebito.setText(String.valueOf(totalTarjetaDebito));
        txtTotalCredito.setText(String.valueOf(totalTarjetaCredito));
        txtImporteTotal.setText(String.valueOf(totalVenta));
        txtDescuento.setText(String.valueOf(descuento));
        txtNetoPagar.setText(String.valueOf(netoPagar));
        txtImportePagado.setText(String.valueOf(totalPagado));
        txtVuelto.setText(String.valueOf(vuelto));
    }

    public int obtenerTotalPagadoActual() {
        modelo = (DefaultTableModel) tblFormaPago.getModel();
        int total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += Integer.parseInt(modelo.getValueAt(i, 2).toString());
        }
        return total;
    }

    public void limpiarCamposPago() {
        cboFormaPago.setSelectedIndex(0);
        cboDescripcion.setSelectedIndex(0);
        txtMonto.setText("");
        txtNumTarjeta.setText("");
    }

    //metodos para obtener descuento y neto a pagar, esto pasamos a ventas 
    public int getDescuento() {
        return Integer.parseInt(txtDescuento.getText().trim());
    }

    public int getNetoPagar() {
        return Integer.parseInt(txtNetoPagar.getText().trim());
    }

    public List<VentaPago> getPagos() {
        List<VentaPago> pagos = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            VentaPago vp = new VentaPago();
            vp.setFormaPago(modelo.getValueAt(i, 0).toString().trim());
            vp.setDescripcion(modelo.getValueAt(i, 1).toString().trim());
            vp.setMonto(Integer.valueOf(modelo.getValueAt(i, 2).toString().trim()));
            vp.setNumeroTarjeta(modelo.getValueAt(i, 3).toString().trim());
            pagos.add(vp);

        }
        return pagos;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboDescripcion = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtMonto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNumTarjeta = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFormaPago = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtTotalEfectivo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTotalDebito = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JTextField();
        txtNetoPagar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtImportePagado = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtVuelto = new javax.swing.JTextField();
        txtImporteTotal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTotalCredito = new javax.swing.JTextField();
        cboFormaPago = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        btnConfirmarPago = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel1.setText("Forma de Pago");

        cboDescripcion.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        cboDescripcion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Elegir Tarjeta --", "Visa Credito", "Mastercard Credito", "Cabal Credito", "Credicard Credito", "Ueno Credito", "Banco Itau", "Banco Continental", "Banco Sudameris", "Banco N. de Fomento", "Banco Familiar", "Banco Ueno", "Banco Continental", "Banco GNB", "Banco BASA" }));

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setText("Descripción Tarjeta");

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setText("Monto");

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setText("Nº Comprobante");

        tblFormaPago.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblFormaPago);

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel6.setText("TOTAL  EFECTIVO");

        txtTotalEfectivo.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        txtTotalEfectivo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalEfectivo.setText("0");

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel7.setText("TOTAL  TAR. DEBITO");

        txtTotalDebito.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        txtTotalDebito.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalDebito.setText("0");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RESUMEN DE PAGO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 14))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel5.setText("Importe Total: ");

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel8.setText("Descuento:");

        txtDescuento.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtDescuento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDescuento.setText("0");

        txtNetoPagar.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNetoPagar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNetoPagar.setText("0");

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel9.setText("Neto a Pagar:");

        jLabel10.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel10.setText("Importe Pagado:");

        txtImportePagado.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtImportePagado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtImportePagado.setText("0");

        jLabel11.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel11.setText("Vuelto:");

        txtVuelto.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtVuelto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtVuelto.setText("0");

        txtImporteTotal.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtImporteTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtImporteTotal.setText("0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                .addGap(27, 27, 27))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNetoPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtImportePagado, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtVuelto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescuento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtImporteTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(17, 17, 17))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImporteTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNetoPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImportePagado, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVuelto, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        jLabel12.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel12.setText("TOTAL  TAR. CREDITO");

        txtTotalCredito.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        txtTotalCredito.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalCredito.setText("0");

        cboFormaPago.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        cboFormaPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Elegir F. Pago --", "EFECTIVO", "TARJETA DEBITO", "TARJETA CREDITO" }));
        cboFormaPago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboFormaPagoMouseClicked(evt);
            }
        });
        cboFormaPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFormaPagoActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/check.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnConfirmarPago.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btnConfirmarPago.setText("CONFIRMAR PAGO");
        btnConfirmarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarPagoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalDebito, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnConfirmarPago)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboFormaPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(cboDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(txtNumTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboFormaPago, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboDescripcion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtMonto, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNumTarjeta, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtTotalEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(txtTotalDebito, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(txtTotalCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(26, 26, 26)
                .addComponent(btnConfirmarPago, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

    private void cboFormaPagoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboFormaPagoMouseClicked

    }//GEN-LAST:event_cboFormaPagoMouseClicked

    private void cboFormaPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFormaPagoActionPerformed
        formaPago();
    }//GEN-LAST:event_cboFormaPagoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        agregarPagoTabla();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnConfirmarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarPagoActionPerformed
        int totalPagado = obtenerTotalPagadoActual();
        int netoPagar = getNetoPagar();

        if (totalPagado < netoPagar) {
            JOptionPane.showMessageDialog(this, "El monto total pagado no cubre el total de la venta.",
                    "Pago insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pasar descuento, neto a pagar y lista de pagos al padre (DialogGestionVenta)
        padre.setDescuento(getDescuento());
        padre.setNetoPagar(getNetoPagar());
        padre.setListaPagos(getPagos());

        // Cierra el diálogo
        this.dispose();
    }//GEN-LAST:event_btnConfirmarPagoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmarPago;
    private javax.swing.JComboBox<String> cboDescripcion;
    private javax.swing.JComboBox<String> cboFormaPago;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFormaPago;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtImportePagado;
    private javax.swing.JTextField txtImporteTotal;
    private javax.swing.JTextField txtMonto;
    private javax.swing.JTextField txtNetoPagar;
    private javax.swing.JTextField txtNumTarjeta;
    private javax.swing.JTextField txtTotalCredito;
    private javax.swing.JTextField txtTotalDebito;
    private javax.swing.JTextField txtTotalEfectivo;
    private javax.swing.JTextField txtVuelto;
    // End of variables declaration//GEN-END:variables
}
