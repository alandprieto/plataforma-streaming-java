package vista;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
 * Ventana de inicio de sesión.
 */
public class VistaLogin extends JFrame {

    public JTextField txtEmail;
    public JPasswordField txtPassword;
    public JButton btnIngresar;
    public JButton btnRegistrar;

    public JLabel lblMensaje;

    /**
     * Constructor que inicializa la interfaz de login.
     */
    public VistaLogin() {
        this.setTitle("Login - Streaming TDL2");
        this.setSize(400, 350);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));

        lblMensaje = new JLabel(" ");
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(new Color(240, 240, 240));
        lblMensaje.setForeground(new Color(240, 240, 240));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 14));
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lblMensaje.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblTitulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panelNorte.add(lblMensaje);
        panelNorte.add(lblTitulo);

        this.add(panelNorte, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        panelForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelForm.add(txtEmail);

        panelForm.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panelForm.add(txtPassword);

        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel(""));

        this.add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setBackground(new Color(50, 150, 250));
        btnIngresar.setForeground(Color.WHITE);

        btnRegistrar = new JButton("Registrarse");

        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrar);

        this.add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Inicializa y muestra la ventana de login.
     */
    public void iniciar() {
        this.setVisible(true);
    }

    /**
     * Cierra la ventana de login.
     */
    public void cerrar() {
        this.dispose();
    }

    /**
     * Muestra un mensaje de éxito en color verde.
     */
    public void mostrarExito(String texto) {
        lblMensaje.setText(texto);
        lblMensaje.setBackground(new Color(40, 167, 69));
        lblMensaje.setForeground(Color.WHITE);
    }
}