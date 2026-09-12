package vista;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Ventana modal que muestra detalles de una película desde OMDb
 */
public class VistaDetallesPelicula extends JDialog {

    /**
     * Construye una ventana modal que muestra detalles de una película desde OMDb.
     */
    public VistaDetallesPelicula(JFrame parent, String titulo, String anio, String sinopsis, String rating) {
        super(parent, "Detalles de la Película", true);

        this.setSize(600, 400);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(10, 10));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setBackground(new Color(50, 100, 150));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAnio = new JLabel("Año: " + anio);
        lblAnio.setFont(new Font("Arial", Font.PLAIN, 14));
        lblAnio.setForeground(new Color(200, 200, 200));
        lblAnio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRating = new JLabel("Rating IMDb: " + rating + "/10");
        lblRating.setFont(new Font("Arial", Font.PLAIN, 14));
        lblRating.setForeground(new Color(255, 200, 0));
        lblRating.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelTitulo.add(lblTitulo);
        panelTitulo.add(Box.createVerticalStrut(5));
        panelTitulo.add(lblAnio);
        panelTitulo.add(Box.createVerticalStrut(5));
        panelTitulo.add(lblRating);

        this.add(panelTitulo, BorderLayout.NORTH);

        JPanel panelSinopsis = new JPanel();
        panelSinopsis.setLayout(new BorderLayout());
        panelSinopsis.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblSinopsisTitulo = new JLabel("Sinopsis:");
        lblSinopsisTitulo.setFont(new Font("Arial", Font.BOLD, 14));

        JTextArea txtSinopsis = new JTextArea(sinopsis);
        txtSinopsis.setEditable(false);
        txtSinopsis.setLineWrap(true);
        txtSinopsis.setWrapStyleWord(true);
        txtSinopsis.setFont(new Font("Arial", Font.PLAIN, 12));
        txtSinopsis.setBackground(new Color(245, 245, 245));
        txtSinopsis.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollSinopsis = new JScrollPane(txtSinopsis);
        scrollSinopsis.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelSinopsis.add(lblSinopsisTitulo, BorderLayout.NORTH);
        panelSinopsis.add(scrollSinopsis, BorderLayout.CENTER);

        this.add(panelSinopsis, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCerrar.addActionListener(e -> this.dispose());

        panelBotones.add(btnCerrar);
        this.add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Muestra la ventana modal.
     */
    public void mostrar() {
        this.setVisible(true);
    }
}
