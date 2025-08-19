package com.proyecto.vista;

import com.proyecto.controlador.ClienteController;
import com.proyecto.controlador.DetalleVentaController;
import com.proyecto.controlador.ProductoController;
import com.proyecto.controlador.StockController;
import com.proyecto.controlador.VentaController;
import com.proyecto.controlador.VentaPagoController;
import com.proyecto.modelo.Cliente;
import com.proyecto.modelo.Deposito;
import com.proyecto.modelo.DetalleVenta;
import com.proyecto.modelo.Producto;
import com.proyecto.modelo.Stock;
import com.proyecto.modelo.Usuario;
import com.proyecto.modelo.Venta;
import com.proyecto.modelo.VentaPago;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DialogGestionVenta extends javax.swing.JDialog {

    private Usuario usuarioLogueado;
    Producto productoActual;
    Long idDepositoActual = null;
    Deposito depositoActual;
    ClienteController clienteCtr;
    VentaController controller = new VentaController();
    DetalleVentaController dv = new DetalleVentaController();
    VentaPagoController vp = new VentaPagoController();
    Long idClienteActual;// contiene id del cliente
    DefaultTableModel modelo;
    //variables globales
    int descuento = 0; //contiene el descuento
    int netoPagar = 0;
    List<VentaPago> listaPagos = null;

    public DialogGestionVenta(java.awt.Frame parent, boolean modal, Usuario usuarioLogueado) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Gestion de Ventas");
        this.usuarioLogueado = usuarioLogueado;
        //nombre vendedor
        txtVendedor.setText(usuarioLogueado.getNombre());
        usuarioLogueado.getId();
        System.out.println("user: " + usuarioLogueado.getNombre());
        System.out.println("id user: " + usuarioLogueado.getId());

        //generar factura
        String nroFactura = controller.generarNumeroFactura();
        txtVNFactura.setText(nroFactura);
        //fecha actual
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        txtVFecha.setText(fechaActual);

        // simular precionar enter para agregar producto en grilla
        txtVCantidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnVAgregar.doClick(); // simula el clic del botón btnVAgregar
            }
        });

        //abrir formaPago
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "abrirVentaPago");

        getRootPane().getActionMap().put("abrirVentaPago", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirDialogoVentaPago();
            }
        });
        // cabecera tabla ,odificado
        cabeceraTablaVenta();
        // desabilitar campos ventas
        desabilitarCampos();
    }

    public void cabeceraTablaVenta() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        modelo.addColumn("#");
        modelo.addColumn("ID PRODUCTO");
        modelo.addColumn("CANTIDAD");
        modelo.addColumn("CODIGO BARRA");
        modelo.addColumn("DESCRIPCION");
        modelo.addColumn("PRECIO UNITARIO");
        modelo.addColumn("IMPORTE");
        tblVentas.setModel(modelo);

        // ajustamos el ancho de las columnas
        tblVentas.getColumnModel().getColumn(0).setPreferredWidth(10);  // Columna #
        tblVentas.getColumnModel().getColumn(1).setPreferredWidth(20);  // IDPRODUCTO      
        tblVentas.getColumnModel().getColumn(2).setPreferredWidth(20);  // CANTIDAD     
        tblVentas.getColumnModel().getColumn(3).setPreferredWidth(30); // CODIGO BARRA
        tblVentas.getColumnModel().getColumn(4).setPreferredWidth(150); // DESCRIPCION 
         
    }

    private void recalcularNumerosDeItem() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.setValueAt(i + 1, i, 0);
        }
    }

    public void agregarProductoAVenta() {
        // validaciones 
        String nombreCliente = txtCiDescripcion.getText().trim();
        Long idProducto = Long.valueOf(txtVIdProducto.getText().trim());
        String codigoBarra = txtVCodBarra.getText().trim();
        String descripcion = txtVDescripcion.getText().trim();

        //validaciones
        if (nombreCliente.isBlank()) {
            JOptionPane.showMessageDialog(null, "Los datos del cliente no pueden quedar vacíos",
                    "Atención!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (codigoBarra.isBlank() || descripcion.isBlank()) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para ser agregado",
                    "Atención!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stock = Integer.parseInt(txtVStock.getText().trim());

        if (stock <= 0) {
            JOptionPane.showMessageDialog(null, "Este producto no está en existencia. Avise al encargado de compras.",
                    "Atención!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //int cantidad = Integer.parseInt());
        String cantidadStr = txtVCantidad.getText().trim();

        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad válida.",
                    "Atención!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que cero.",
                        "Atención!", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La cantidad ingresada no es un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cantidad > stock) {
            JOptionPane.showMessageDialog(null, "La cantidad supera el stock disponible.",
                    "Atención!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // datos para agregar a la grilla
        int nroItem = modelo.getRowCount() + 1;
        int precioUnitario = productoActual.getPrecio();
        int importe = cantidad * precioUnitario;

        modelo = (DefaultTableModel) tblVentas.getModel();

        // verificamos si hay producto en la grilla, si hay registro, preguntamos si se modifica cantdad
        String codBarraActual = productoActual.getCodBarra().trim();

        for (int i = 0; i < modelo.getRowCount(); i++) {

            String codBarraTable = modelo.getValueAt(i, 2).toString().trim();

            if (codBarraActual.equals(codBarraTable)) {

                int respuesta = JOptionPane.showConfirmDialog(this,
                        "Este producto ya fue agregado. ¿Desea sumar la nueva cantidad a la ya existente?",
                        "Producto repetido",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (respuesta == JOptionPane.YES_OPTION) {

                    int cantidadExistente = Integer.parseInt(modelo.getValueAt(i, 1).toString());
                    int nuevaCantidad = cantidadExistente + cantidad;

                    if (nuevaCantidad > stock) {

                        JOptionPane.showMessageDialog(null, "La cantidad total supera el stock disponible.",
                                "Atención!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    modelo.setValueAt(nuevaCantidad, i, 1); // columna cantidad
                    modelo.setValueAt(nuevaCantidad * precioUnitario, i, 5); // columna importe                    
                }
                calcularTotalVenta();
                limpiarTextProducto();
                return;
            }
        }

        Object[] fila = new Object[7];
        fila[0] = nroItem;
        fila[1] = idProducto;
        fila[2] = cantidad;
        fila[3] = codigoBarra;
        fila[4] = descripcion;
        fila[5] = precioUnitario;
        fila[6] = importe;

        modelo.addRow(fila);

        // Recalcular números de ítem
        recalcularNumerosDeItem();
        calcularTotalVenta();
        limpiarTextProducto();
    }

    public int calcularTotalVenta() {
        int total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int importe = Integer.parseInt(modelo.getValueAt(i, 5).toString());
            total += importe;
        }
        return total;
    }

    public void eliminarProductoTabla() {
        int fila = tblVentas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila para anular un producto.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar este producto?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            modelo = (DefaultTableModel) tblVentas.getModel();
            modelo.removeRow(fila);
            productoActual = null;
        }
    }

    public void limpiarTabla() {
        modelo = (DefaultTableModel) tblVentas.getModel();
        modelo.setRowCount(0);
        productoActual = null;
    }

    public void limpiarTextProducto() {
        txtVIdProducto.setText("");
        txtVCodBarra.setText("");
        txtVDescripcion.setText("");
        txtVStock.setText("");
        txtVDeposito.setText("");
        txtVCantidad.setText("");
    }

    public void desabilitarCampos() {
        txtVFecha.setEnabled(false);
        txtVNFactura.setEnabled(false);
        txtVendedor.setEnabled(false);
        txtVRucCi.setEnabled(false);
        txtCiDescripcion.setEnabled(false);
        txtVIdProducto.setEnabled(false);
        txtVCodBarra.setEnabled(false);
        txtVDescripcion.setEnabled(false);
        txtVStock.setEnabled(false);
        txtVDeposito.setEnabled(false);
        txtImporte.setEnabled(false);
        txtDescuento.setEnabled(false);
        txtNetoPagar.setEnabled(false);

    }

    public void habilitarCampos() {
        btnVBuscarCodBarra.setEnabled(true);
        btnVAgregar.setEnabled(true);
        btnVAnularProd.setEnabled(true);
        btnVCancelar.setEnabled(true);

    }

    public void abrirDialogoVentaPago() {
        int filas = tblVentas.getRowCount();
        if (filas == 0) {
            JOptionPane.showMessageDialog(null, "Debe cargar al menos un producto antes de registrar el pago.",
                    "Atención",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int totalVenta = calcularTotalVenta();
        DialogVentaPago dialog = new DialogVentaPago(this, true, totalVenta);
        dialog.setVisible(true);

        //recibimos datos al cerrar ventaPago
        int descuento = dialog.getDescuento();
        int netoPagar = dialog.getNetoPagar();
        List<VentaPago> pagos = dialog.getPagos();

        //mostramos datos 
        txtDescuento.setText(String.valueOf(descuento));
        txtNetoPagar.setText(String.valueOf(netoPagar));
        txtImporte.setText(String.valueOf(totalVenta));

        //guardamos datos para guardar ventas
        this.descuento = descuento;
        this.netoPagar = netoPagar;
        this.listaPagos = pagos;

    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
        txtDescuento.setText(String.valueOf(descuento));
    }

    public void setNetoPagar(int netoPagar) {
        this.netoPagar = netoPagar;
        txtNetoPagar.setText(String.valueOf(netoPagar));
    }

    public void setListaPagos(List<VentaPago> listaPagos) {
        this.listaPagos = listaPagos;
    }

    public void guardarVenta() throws IOException {
        Long idCliente = idClienteActual;
        Long idVendedor = usuarioLogueado.getId();
        java.sql.Date fechaRegistro = new java.sql.Date(System.currentTimeMillis());
        int total = Integer.parseInt(txtImporte.getText());
        int importe = Integer.parseInt(txtNetoPagar.getText());
        int descuento = Integer.parseInt(txtDescuento.getText());
        String estado = "T";
        String numFactura = txtVNFactura.getText().trim();

        // Creamos el objeto Venta
        Venta venta = new Venta();

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        venta.setCliente(cliente);

        Usuario usuario = new Usuario();
        usuario.setId(idVendedor);
        venta.setUsuario(usuario);

        venta.setFecha(fechaRegistro);
        venta.setTotal(total);
        venta.setImporte(importe);
        venta.setEstado(estado);
        venta.setNumFactura(numFactura);
        venta.setDescuento(descuento);

        // Guardamos la venta en la BD
        Venta ventaGuardada = controller.guardar(venta);
        if (ventaGuardada == null) {
            JOptionPane.showMessageDialog(null, "Error al guardar la venta.");
            return;
        }

        guardarDetalleVenta(ventaGuardada);
        guardarVentaPago(ventaGuardada);
        JOptionPane.showMessageDialog(null, "Venta registrada correctamente.");
        
       generarFacturaPDF(ventaGuardada);

    }

    private void guardarVentaPago(Venta venta) {
        for (VentaPago pago : listaPagos) {
            pago.setVenta(venta); // Asignamos la venta con su ID
            vp.guardarVentaPago(pago);     // Tu controlador de VentaPago
        }
    }

    private void guardarDetalleVenta(Venta venta) {
        int filas = tblVentas.getRowCount();

        ProductoController productoCtr = new ProductoController();
        StockController stockCtr = new StockController();

        for (int i = 0; i < filas; i++) {
            try {
                // obtenemos datos de la tabla
                long idProducto = Long.parseLong(tblVentas.getValueAt(i, 1).toString());
                int cantidad = Integer.parseInt(tblVentas.getValueAt(i, 2).toString());
                int precioUnitario = Integer.parseInt(tblVentas.getValueAt(i, 5).toString());

                // Buscamos el producto por ID
                Producto producto = productoCtr.buscarProductoPorId(idProducto);
                if (producto == null) {
                    JOptionPane.showMessageDialog(this, "Producto con ID " + idProducto + " no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // buscamos stock para obtener depósito
                Stock stock = stockCtr.buscarPrimerStockDisponible(idProducto);
                if (stock == null) {
                    JOptionPane.showMessageDialog(this, "No hay stock disponible para el producto " + producto.getNombreProducto(), "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                Deposito deposito = stock.getDeposito();

                // creamos detalle venta 
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                detalle.setProducto(producto);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalle.setDeposito(deposito);

                // guardamos el detalle
                dv.guardar(detalle);

                // Descontar del stock actual
                int stockActual = stock.getCantidad();
                int nuevoStock = stockActual - cantidad;

                if (nuevoStock < 0) {
                    JOptionPane.showMessageDialog(null,
                            "Stock insuficiente para el producto " + producto.getNombreProducto(),
                            "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                stock.setCantidad(nuevoStock);
                stockCtr.actualizar(stock);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error en formato numérico en la fila " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void generarFacturaPDF(Venta venta) throws IOException {
    Document documento = new Document(PageSize.A4, 50, 50, 50, 50);

    try {
        // Ruta de guardado
        String nombreArchivo = "facturas/factura_" + venta.getNumFactura() + ".pdf";
        File directorio = new File("facturas");
        if (!directorio.exists()) directorio.mkdirs();

        PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
        documento.open();

        // Fuente y estilos
        Font negrita16 = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font normal12 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font negrita10 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

        // ENCABEZADO
        documento.add(new Paragraph("FACTURA LEGAL", negrita16));
        documento.add(new Paragraph("Timbrado N° 12345678", negrita10));
        documento.add(new Paragraph("Vigencia: 01/01/2024 al 31/12/2025", negrita10));
        documento.add(new Paragraph("RUC: 80012345-6", negrita10));
        documento.add(Chunk.NEWLINE);

        // DATOS DEL CLIENTE Y VENTA
        PdfPTable tablaCabecera = new PdfPTable(2);
        tablaCabecera.setWidthPercentage(100);

        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.addElement(new Paragraph("Cliente: " + txtCiDescripcion.getText(), normal12));
        cell1.addElement(new Paragraph("CI/RUC: " + txtVRucCi.getText(), normal12));

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.addElement(new Paragraph("Factura N°: " + venta.getNumFactura(), normal12));
        cell2.addElement(new Paragraph("Fecha: " + txtVFecha.getText(), normal12));
        cell2.addElement(new Paragraph("Vendedor: " + txtVendedor.getText(), normal12));

        tablaCabecera.addCell(cell1);
        tablaCabecera.addCell(cell2);
        documento.add(tablaCabecera);
        documento.add(Chunk.NEWLINE);

        // TABLA DETALLE PRODUCTOS
        PdfPTable tablaDetalle = new PdfPTable(5);
        tablaDetalle.setWidthPercentage(100);
        tablaDetalle.setWidths(new float[]{10, 35, 15, 20, 20});

        tablaDetalle.addCell("Cant.");
        tablaDetalle.addCell("Descripción");
        tablaDetalle.addCell("P. Unit.");
        tablaDetalle.addCell("Importe");
        tablaDetalle.addCell("Depósito");

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String cantidad = modelo.getValueAt(i, 2).toString();
            String desc = modelo.getValueAt(i, 4).toString();
            String precio = modelo.getValueAt(i, 5).toString();
            String importe = modelo.getValueAt(i, 6).toString();
            String deposito = txtVDeposito.getText();

            tablaDetalle.addCell(cantidad);
            tablaDetalle.addCell(desc);
            tablaDetalle.addCell(precio);
            tablaDetalle.addCell(importe);
            tablaDetalle.addCell(deposito);
        }
        documento.add(tablaDetalle);
        documento.add(Chunk.NEWLINE);

        // PIE DE FACTURA
        PdfPTable tablaTotales = new PdfPTable(2);
        tablaTotales.setWidthPercentage(40);
        tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);

        tablaTotales.addCell("Total:");
        tablaTotales.addCell(txtImporte.getText());
        tablaTotales.addCell("Descuento:");
        tablaTotales.addCell(txtDescuento.getText());
        tablaTotales.addCell("Neto a pagar:");
        tablaTotales.addCell(txtNetoPagar.getText());

        documento.add(tablaTotales);

        documento.close();

        // Abrir el PDF automáticamente
        Desktop.getDesktop().open(new File(nombreArchivo));

    } catch (DocumentException | FileNotFoundException e) {
        JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
        e.printStackTrace();
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtVRucCi = new javax.swing.JTextField();
        txtCiDescripcion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtVFecha = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtVNFactura = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtVCodBarra = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtVDescripcion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtVStock = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtVDeposito = new javax.swing.JTextField();
        btnVAnularProd = new javax.swing.JButton();
        btnVAgregar = new javax.swing.JButton();
        btnVBuscarCodBarra = new javax.swing.JButton();
        txtVIdProducto = new javax.swing.JTextField();
        txtVCantidad = new javax.swing.JTextField();
        btnVCancelar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtVendedor = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVentas = new javax.swing.JTable();
        btnVBuscarCliente = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtImporte = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtDescuento = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        txtNetoPagar = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        lblPagar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtVRucCi.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel1.add(txtVRucCi, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 180, 30));

        txtCiDescripcion.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel1.add(txtCiDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 280, 30));

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setText("Fecha:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 60, 30));

        txtVFecha.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel1.add(txtVFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 120, 30));

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setText("Nº Factura:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 100, 30));

        txtVNFactura.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel1.add(txtVNFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 170, 30));

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setText("Vendedor:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 100, 32));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 14))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel1.setText("Cod. Barra:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 100, 32));

        txtVCodBarra.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel2.add(txtVCodBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 190, 30));

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel9.setText("Descripcion:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 112, 32));

        txtVDescripcion.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel2.add(txtVDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 30, 230, 30));

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel6.setText("Stock:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 60, 32));

        txtVStock.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel2.add(txtVStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 60, 30));

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel8.setText("Deposito:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 80, 32));

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel7.setText("Cantidad:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 99, 32));

        txtVDeposito.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel2.add(txtVDeposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 190, 32));

        btnVAnularProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/anular.png"))); // NOI18N
        btnVAnularProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVAnularProdActionPerformed(evt);
            }
        });
        jPanel2.add(btnVAnularProd, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 70, 47, -1));

        btnVAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/vender.png"))); // NOI18N
        btnVAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVAgregarActionPerformed(evt);
            }
        });
        jPanel2.add(btnVAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 70, 50, -1));

        btnVBuscarCodBarra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnVBuscarCodBarra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVBuscarCodBarraActionPerformed(evt);
            }
        });
        jPanel2.add(btnVBuscarCodBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, 50, -1));

        txtVIdProducto.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel2.add(txtVIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 50, 30));

        txtVCantidad.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel2.add(txtVCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 80, 32));

        btnVCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cancel.png"))); // NOI18N
        btnVCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVCancelarActionPerformed(evt);
            }
        });
        jPanel2.add(btnVCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 70, 47, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 810, 120));

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setText("Ruc o Ci: ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 100, 32));

        txtVendedor.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jPanel1.add(txtVendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 210, 30));

        tblVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblVentas);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 810, 220));

        btnVBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnVBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVBuscarClienteActionPerformed(evt);
            }
        });
        jPanel1.add(btnVBuscarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 50, 50, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Importe", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 14))); // NOI18N

        txtImporte.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtImporte, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtImporte)
                .addGap(15, 15, 15))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 210, 90));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descuento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 14))); // NOI18N

        txtDescuento.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtDescuento, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtDescuento)
                .addGap(13, 13, 13))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 470, 240, 90));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Neto a Pagar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 14))); // NOI18N

        txtNetoPagar.setFont(new java.awt.Font("Verdana", 1, 36)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNetoPagar, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNetoPagar)
                .addContainerGap())
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 470, 300, -1));

        jButton2.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jButton2.setText("Generar Venta");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 580, 240, 50));

        lblPagar.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblPagar.setForeground(new java.awt.Color(255, 0, 0));
        lblPagar.setText("Forma de pago F10");
        jPanel1.add(lblPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 216, 190, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVBuscarCodBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVBuscarCodBarraActionPerformed
        DialogBuscarProductoVenta dialog = new DialogBuscarProductoVenta(null, true);
        dialog.setVisible(true);

        Producto p = dialog.getProductoSeleccionado();
        if (p != null) {
            txtVIdProducto.setText(String.valueOf(p.getId()));
            txtVDescripcion.setText(p.getNombreProducto());
            txtVCodBarra.setText(p.getCodBarra());
            productoActual = p;

            // buscamos stock            
            StockController stockController = new StockController();
            Stock stock = stockController.buscarPrimerStockDisponible(p.getId());

            if (stock != null) {
                txtVStock.setText(String.valueOf(stock.getCantidad()));
                txtVDeposito.setText(stock.getDeposito().getDescriDeposito());
                idDepositoActual = stock.getDeposito().getId();
            } else {
                txtVStock.setText(String.valueOf(0));
                idDepositoActual = null;
                txtVDeposito.setText("Producto sin deposito");

            }

        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún producto.",
                    "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnVBuscarCodBarraActionPerformed


    private void btnVBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVBuscarClienteActionPerformed
        DialogGestionClientes dialog = new DialogGestionClientes(null, true);
        dialog.setVisible(true);

        Cliente c = dialog.getClienteSeleccionado();

        if (c != null) {
            idClienteActual = c.getId();
            txtVRucCi.setText(c.getRucCI());
            txtCiDescripcion.setText(c.getNombreCli() + " " + c.getApellidoCli());
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún cliente.",
                    "cliente no seleccionado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnVBuscarClienteActionPerformed

    private void btnVAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVAgregarActionPerformed
        agregarProductoAVenta();
    }//GEN-LAST:event_btnVAgregarActionPerformed

    private void btnVAnularProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVAnularProdActionPerformed
        eliminarProductoTabla();
        txtImporte.setText("");
        txtDescuento.setText("");
        txtNetoPagar.setText("");
    }//GEN-LAST:event_btnVAnularProdActionPerformed

    private void btnVCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVCancelarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea anular la venta?",
                "Confirmar anulación", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            limpiarTextProducto();
            limpiarTabla();
            txtVRucCi.setText("");
            txtCiDescripcion.setText("");
            txtImporte.setText("");
            txtDescuento.setText("");
            txtNetoPagar.setText("");
        }
    }//GEN-LAST:event_btnVCancelarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            guardarVenta();
        } catch (IOException ex) {
            Logger.getLogger(DialogGestionVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        modelo.setRowCount(0);        

    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVAgregar;
    private javax.swing.JButton btnVAnularProd;
    private javax.swing.JButton btnVBuscarCliente;
    private javax.swing.JButton btnVBuscarCodBarra;
    private javax.swing.JButton btnVCancelar;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPagar;
    private javax.swing.JTable tblVentas;
    private javax.swing.JTextField txtCiDescripcion;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtImporte;
    private javax.swing.JTextField txtNetoPagar;
    private javax.swing.JTextField txtVCantidad;
    private javax.swing.JTextField txtVCodBarra;
    private javax.swing.JTextField txtVDeposito;
    private javax.swing.JTextField txtVDescripcion;
    private javax.swing.JTextField txtVFecha;
    private javax.swing.JTextField txtVIdProducto;
    private javax.swing.JTextField txtVNFactura;
    private javax.swing.JTextField txtVRucCi;
    private javax.swing.JTextField txtVStock;
    private javax.swing.JTextField txtVendedor;
    // End of variables declaration//GEN-END:variables

}
