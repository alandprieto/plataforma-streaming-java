package vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Ventana principal de la aplicación de streaming.
 */
public class VistaPrincipal extends JFrame {

    public JTextField txtBusqueda;
    public JButton btnBuscar;
    public JButton btnExplorar;
    public JButton btnCerrarSesion;
    public JButton btnOrdenarGenero;
    public JButton btnOrdenarTitulo;

    public JPanel panelResultados;
    public JScrollPane scrollResultados;
    public JLabel lblBienvenida;

    /**
     * Constructor que inicializa la interfaz principal.
     */
    public VistaPrincipal() {
        this.setTitle("ALTI - Tu Plataforma de Streaming favorita");
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(Color.DARK_GRAY);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(Color.WHITE);

        txtBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar");

        btnExplorar = new JButton("Explorar (Aleatorias)");

        btnOrdenarGenero = new JButton("Ordenar por Género");
        btnOrdenarTitulo = new JButton("Ordenar por Título");

        btnCerrarSesion = new JButton("Cerrar Sesión");

        panelSuperior.add(lblBuscar);
        panelSuperior.add(txtBusqueda);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnExplorar);
        panelSuperior.add(btnOrdenarGenero);
        panelSuperior.add(btnOrdenarTitulo);
        panelSuperior.add(btnCerrarSesion);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BorderLayout());
        topContainer.add(panelSuperior, BorderLayout.NORTH);

        lblBienvenida = new JLabel("", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.BLACK);
        lblBienvenida.setOpaque(true);
        lblBienvenida.setBackground(new Color(255, 255, 200));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblBienvenida.setVisible(false);
        topContainer.add(lblBienvenida, BorderLayout.SOUTH);

        panelResultados = new JPanel();
        panelResultados.setLayout(new GridLayout(0, 4, 10, 10));
        panelResultados.setBackground(new Color(240, 240, 240));
        panelResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollResultados.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(topContainer, BorderLayout.NORTH);
        this.add(scrollResultados, BorderLayout.CENTER);
    }

    /**
     * Inicializa y muestra la ventana principal.
     */
    public void iniciar() {
        this.setVisible(true);
    }
}