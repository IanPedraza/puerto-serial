package puerto.serie.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import listeners.onConsoleListener;
import puerto.serie.ControladorPuertos;

public class Ventana extends JFrame {

    //CONSTANTES
    private static boolean OPEN = false;

    //VISTAS
    private JPanel panel;
    private JTable tabla;
    private JButton boton_abrir;
    private JButton boton_enviar;
    private JButton boton_limpiar_ventana;
    private JTextArea campo_mensaje;
    private JTextArea campo_consola;    
    private JLabel label_puertos;
    private JLabel label_consola;
    private JLabel label_mensaje;
    private JLabel label_baudios;    
    private JComboBox lista_baudios;

    //ADMINISTRADORES
    private final ControladorPuertos controlador;
    private DefaultTableModel model;
    
    //LISTENERS
    private ActionListener abrir_puerto;
    private ActionListener limpiar_pantalla;
    private ActionListener enviar_mensaje;
    private onConsoleListener consoleListeer;

    //ARREGLOS
    private final Object[] lista_puertos;
    private final String[] opciones_baudios;

    public Ventana() {
        definirClicks();
        
        controlador = new ControladorPuertos(consoleListeer);
        lista_puertos = controlador.obtenerLista().toArray();

        opciones_baudios = new String[]{
            "1200",
            "2400",
            "4800",
            "9600",
            "19200",
            "38400",
            "57600",
            "115200",
            "230400",
            "460800",
            "921600",
        };
        
        inicomponents();
    }

    private void definirClicks() {
        limpiar_pantalla = (ActionEvent e) -> {
            campo_consola.setText("");
        };
        
        enviar_mensaje = (ActionEvent e) -> {
            String mensaje = campo_mensaje.getText();

            if (mensaje.trim().length() > 0) {
                controlador.enviarMensaje(mensaje);
                campo_mensaje.setText("");
            }

        };

        consoleListeer = (String cadena) -> {
            campo_consola.append(cadena);
        };

        abrir_puerto = (ActionEvent e) -> {
            if (lista_puertos.length > 0) {
                if (!OPEN) {
                    int index = tabla.getSelectedRow();

                    if (index >= 0) {
                        if (controlador.abrirPuerto(index, Integer.parseInt((String) lista_baudios.getSelectedItem()))) {
                            tabla.setEnabled(false);
                            boton_abrir.setText("CLOSE");
                            campo_mensaje.setText("");
                            campo_mensaje.setEnabled(true);
                            boton_enviar.setEnabled(true);
                            boton_limpiar_ventana.setEnabled(true);
                            lista_baudios.setEnabled(false);
                            OPEN = !OPEN;
                        }
                    }
                } else {
                    controlador.cerrarConexion();
                    boton_abrir.setText("OPEN");
                    campo_mensaje.setText("");
                    campo_mensaje.setEnabled(false);
                    boton_enviar.setEnabled(false);
                    boton_limpiar_ventana.setEnabled(false);
                    lista_baudios.setEnabled(true);
                    OPEN = !OPEN;
                }
            }
        };
    }

    private void inicomponents() {
        this.setTitle("Conexión Serial");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        Border borde = BorderFactory.createLineBorder(Color.GRAY, 1);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        model = new DefaultTableModel();
        model.addColumn("Puertos");

        model.addRow(lista_puertos);

        tabla = new JTable(model);
        tabla.setBounds(20, 45, 150, 200);
        tabla.setBorder(borde);
        tabla.setFont(new Font("Arial", Font.PLAIN, 16));

        boton_abrir = new JButton("OPEN");
        boton_abrir.setBounds(180, 45, 90, 35);
        boton_abrir.addActionListener(abrir_puerto);

        boton_enviar = new JButton("ENVIAR");
        boton_enviar.setBounds(600, 430, 80, 35);
        boton_enviar.addActionListener(enviar_mensaje);
        boton_enviar.setEnabled(false);
        
        boton_limpiar_ventana = new JButton("LIMPIAR");
        boton_limpiar_ventana.setBounds(590, 255, 90, 35);
        boton_limpiar_ventana.setEnabled(false);
        boton_limpiar_ventana.addActionListener(limpiar_pantalla);
                
        label_puertos = new JLabel("Puertos:");
        label_puertos.setBounds(20, 13, 60, 35);
        label_puertos.setFont(new Font("Arial", Font.BOLD, 14));

        label_consola = new JLabel("Consola:");
        label_consola.setBounds(380, 15, 100, 35);
        label_consola.setFont(new Font("Arial", Font.BOLD, 14));

        label_mensaje = new JLabel("Ingrese su mensaje:");
        label_mensaje.setBounds(380, 290, 200, 35);
        label_mensaje.setFont(new Font("Arial", Font.BOLD, 14));
        
        label_baudios = new JLabel("Número de baudios:");
        label_baudios.setBounds(20, 260, 200, 35);
        label_baudios.setFont(new Font("Arial", Font.BOLD, 14));

        lista_baudios = new JComboBox();
        lista_baudios.setBounds(20, 290, 200, 35);
        lista_baudios.setModel(new DefaultComboBoxModel<>(opciones_baudios));
        lista_baudios.setFont(new Font("Arial", Font.PLAIN, 14));
        lista_baudios.setSelectedItem("9600");
        lista_baudios.setEditable(true);
        
        campo_mensaje = new JTextArea();
        campo_mensaje.setBounds(380, 290, 300, 90);
        campo_mensaje.setBorder(borde);
        campo_mensaje.setLineWrap(true);
        campo_mensaje.setWrapStyleWord(true);

        JScrollPane scroll_campo_mensaje = new JScrollPane(campo_mensaje, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll_campo_mensaje.setBounds(380, 320, 300, 90);
        scroll_campo_mensaje.setBorder(BorderFactory.createEmptyBorder());

        campo_consola = new JTextArea();
        campo_consola.setBounds(380, 45, 300, 200);
        campo_consola.setBorder(borde);
        campo_consola.setEditable(false);
        campo_mensaje.setLineWrap(true);
        campo_mensaje.setWrapStyleWord(true);
        campo_mensaje.setEnabled(false);

        JScrollPane scroll_campo_consola = new JScrollPane(campo_consola, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll_campo_consola.setBounds(380, 45, 300, 200);
        scroll_campo_consola.setBorder(BorderFactory.createEmptyBorder());

        this.add(panel);

        panel.add(boton_abrir);
        panel.add(tabla);
        panel.add(boton_enviar);
        panel.add(scroll_campo_mensaje);
        panel.add(label_puertos);
        panel.add(scroll_campo_consola);
        panel.add(label_consola);
        panel.add(label_mensaje);
        panel.add(label_baudios);
        panel.add(lista_baudios);
        panel.add(boton_limpiar_ventana);
        
        this.setSize(700, 520);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}