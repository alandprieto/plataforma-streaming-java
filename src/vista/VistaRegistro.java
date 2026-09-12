package vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Ventana de registro de nuevos usuarios.
 */
public class VistaRegistro extends JFrame {

    public JTextField txtNombre;
    public JTextField txtApellido;
    public JTextField txtDNI;
    public JTextField txtEmail;
    public JPasswordField txtPassword;

    public JButton btnGuardar;
    public JButton btnCancelar;

    /**
     * Constructor que inicializa la interfaz de registro.
     */
    public VistaRegistro() {
        this.setTitle("Nuevo Usuario - Streaming TDL2");
        this.setSize(400, 450);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Crear Cuenta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        this.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 15));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panelForm.add(txtApellido);

        panelForm.add(new JLabel("DNI:"));
        txtDNI = new JTextField();
        panelForm.add(txtDNI);

        panelForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelForm.add(txtEmail);

        panelForm.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panelForm.add(txtPassword);

        this.add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(50, 150, 50));
        btnGuardar.setForeground(Color.WHITE);

        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        this.add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Inicializa y muestra la ventana de registro.
     */
    public void iniciar() {
        this.setVisible(true);
    }

    /**
     * Cierra la ventana de registro.
     */
    public void cerrar() {
        this.dispose();
    }
}