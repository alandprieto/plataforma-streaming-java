package controlador;

import comparador.ComparadorPeliculaGenero;
import comparador.ComparadorPeliculaTitulo;
import excepciones.DatoInvalidoException;
import modelo.Pelicula;
import modelo.Resenia;
import modelo.Usuario;
import servicio.AppImple;
import servicio.ConsultaPeliculasOMDb;
import vista.VistaDetallesPelicula;
import vista.VistaLogin;
import vista.VistaPrincipal;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

public class ControladorPrincipal implements ActionListener {
    private AppImple servicio;
    private VistaPrincipal vista;
    private Usuario usuario;
    private List<Pelicula> peliculasActuales;

    /**
     * Inicializa el controlador principal con la vista y el servicio.
     * Muestra el Top 10 en la primera entrada del usuario o películas aleatorias si ya lo vio.
     */
    public ControladorPrincipal(AppImple servicio, VistaPrincipal vista, Usuario usuario) {
        this.servicio = servicio;
        this.vista = vista;
        this.usuario = usuario;

        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnExplorar.addActionListener(this);
        this.vista.btnCerrarSesion.addActionListener(this);
        this.vista.btnOrdenarGenero.addActionListener(this);
        this.vista.btnOrdenarTitulo.addActionListener(this);
        this.vista.setTitle("Streaming - Usuario: " + usuario.getNombre());

        boolean yaVio = false;
        try {
            yaVio = servicio.haVistoTop10(usuario.getID()) || usuario.isVistoTop10();
        } catch (Exception ex) {
            yaVio = usuario.isVistoTop10();
        }

        if (!yaVio) {
            String mensaje = "BIENVENIDO: sabemos que te gustan las peliculas, aca estan las 10 mejores de nuestra plataforma ¡calificalas!";
            try {
                if (vista.lblBienvenida != null) {
                    vista.lblBienvenida.setText(mensaje);
                    vista.lblBienvenida.setVisible(true);
                }
            } catch (Exception ex) {
                System.err.println("No se pudo mostrar lblBienvenida: " + ex.getMessage());
            }

            cargarPeliculas(servicio.obtenerTop10Peliculas());
            try {
                servicio.marcarVioTop10(usuario.getID());
                usuario.setVistoTop10(true);
            } catch (Exception ex) {
                System.err.println("No se pudo marcar VioTop10: " + ex.getMessage());
            }
        } else {
            if (vista.lblBienvenida != null) {
                vista.lblBienvenida.setVisible(false);
            }
            cargarPeliculas(servicio.obtenerPeliculasExplorar());
        }
    }

    /**
     * Maneja los eventos de los botones principales: Buscar, Explorar, Cerrar Sesión y Ordenamiento.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnBuscar) {
            if (vista.lblBienvenida != null && vista.lblBienvenida.isVisible()) {
                vista.lblBienvenida.setVisible(false);
            }
            String q = vista.txtBusqueda.getText().trim();
            
            if (q.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor ingresa un término de búsqueda.");
                return;
            }
            
            JDialog dialogoCarga = new JDialog(vista, "Buscando en OMDb...", true);
            dialogoCarga.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialogoCarga.setSize(300, 100);
            dialogoCarga.setLocationRelativeTo(vista);
            JProgressBar progress = new JProgressBar();
            progress.setIndeterminate(true);
            dialogoCarga.add(progress);
            
            new Thread(() -> {
                JSONObject datosPelicula = ConsultaPeliculasOMDb.consultarPelicula(q);
                
                SwingUtilities.invokeLater(() -> {
                    dialogoCarga.dispose();
                    
                    if (datosPelicula != null) {
                        String titulo = ConsultaPeliculasOMDb.obtenerTitulo(datosPelicula);
                        String anio = ConsultaPeliculasOMDb.obtenerAnio(datosPelicula);
                        String sinopsis = ConsultaPeliculasOMDb.obtenerSinopsis(datosPelicula);
                        String rating = ConsultaPeliculasOMDb.obtenerRating(datosPelicula);
                        
                        VistaDetallesPelicula ventanaDetalles = new VistaDetallesPelicula(
                                vista, titulo, anio, sinopsis, rating);
                        ventanaDetalles.mostrar();
                    } else {
                        JOptionPane.showMessageDialog(vista,
                                "No se encontró la película '" + q + "' en OMDb.",
                                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            }).start();
            
            dialogoCarga.setVisible(true);
        } else if (e.getSource() == vista.btnExplorar) {
            if (vista.lblBienvenida != null && vista.lblBienvenida.isVisible()) {
                vista.lblBienvenida.setVisible(false);
            }
            cargarPeliculas(servicio.obtenerPeliculasExplorar());
        } else if (e.getSource() == vista.btnOrdenarGenero) {
            if (peliculasActuales != null) {
                peliculasActuales.sort(new ComparadorPeliculaGenero());
                cargarPeliculas(peliculasActuales);
            }
        } else if (e.getSource() == vista.btnOrdenarTitulo) {
            if (peliculasActuales != null) {
                peliculasActuales.sort(new ComparadorPeliculaTitulo());
                cargarPeliculas(peliculasActuales);
            }
        } else if (e.getSource() == vista.btnCerrarSesion) {
            try {
                VistaLogin vLogin = new VistaLogin();
                new ControladorLogin(servicio, vLogin);
                vLogin.iniciar();
            } catch (Exception ex) {
                System.err.println("Error al abrir login: " + ex.getMessage());
            }
            vista.dispose();
        }
    }

    /**
     * Carga una lista de películas en la vista principal.
     * Muestra el poster, título, género, rating y botón para calificar de cada película.
     */
    private void cargarPeliculas(List<Pelicula> peliculas) {
        this.peliculasActuales = peliculas;
        vista.panelResultados.removeAll();
        for (Pelicula p : peliculas) {
            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
            tarjeta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            tarjeta.setPreferredSize(new Dimension(180, 350));

            JLabel lblImg = new JLabel("Cargando...");
            lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
            cargarImagenAsync(p.getPosterURL(), lblImg);

            JLabel lblTitulo = new JLabel("<html><center>" + p.getTitulo() + "</center></html>");
            lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));

            JLabel lblGenero = new JLabel("<html><center>" + (p.getGenero() != null ? p.getGenero().name().replace("_", " ") : "SIN GÉNERO") + "</center></html>");
            lblGenero.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblGenero.setFont(new Font("Arial", Font.ITALIC, 10));
            lblGenero.setForeground(new Color(100, 100, 100));

            JLabel lblRating = new JLabel("★ " + p.getRatingPromedio() + "/10");
            lblRating.setForeground(new Color(255, 140, 0));
            lblRating.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnVotar = new JButton("Calificar");
            btnVotar.setAlignmentX(Component.CENTER_ALIGNMENT);

            if (servicio.yaCalificoUsuario(usuario.getID(), p.getID())) {
                btnVotar.setText("Ya calificada");
                btnVotar.setEnabled(false);
            } else {
                btnVotar.addActionListener(ev -> mostrarDialogoCalificacion(p));
            }

            tarjeta.add(Box.createVerticalStrut(10));
            tarjeta.add(lblImg);
            tarjeta.add(Box.createVerticalStrut(5));
            tarjeta.add(lblTitulo);
            tarjeta.add(lblGenero);
            tarjeta.add(lblRating);
            tarjeta.add(Box.createVerticalStrut(5));
            tarjeta.add(btnVotar);

            vista.panelResultados.add(tarjeta);
        }
        vista.panelResultados.revalidate();
        vista.panelResultados.repaint();
    }

    /**
     * Muestra un diálogo para que el usuario califique una película.
     * Solicita puntaje (1-10) y comentario obligatorio.
     */
    private void mostrarDialogoCalificacion(Pelicula p) {
        JDialog dialogo = new JDialog(vista, "Calificar: " + p.getTitulo(), true);
        dialogo.setSize(500, 350);
        dialogo.setLocationRelativeTo(vista);
        dialogo.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Selecciona tu puntuación:", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialogo.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 5, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        ButtonGroup grupoBotones = new ButtonGroup();
        final int[] puntajeSeleccionado = { 0 };

        for (int i = 1; i <= 10; i++) {
            JToggleButton btn = new JToggleButton(String.valueOf(i));
            int valor = i;
            btn.addActionListener(e -> puntajeSeleccionado[0] = valor);
            grupoBotones.add(btn);
            panelBotones.add(btn);
        }
        dialogo.add(panelBotones, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JTextArea txtComentario = new JTextArea(4, 20);
        txtComentario.setBorder(BorderFactory.createTitledBorder("Tu comentario (Obligatorio)"));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        panelSur.add(new JScrollPane(txtComentario), BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Enviar Reseña");
        btnGuardar.setBackground(new Color(50, 150, 50));
        btnGuardar.setForeground(Color.WHITE);

        btnGuardar.addActionListener(e -> {
            if (puntajeSeleccionado[0] == 0) {
                JOptionPane.showMessageDialog(dialogo, "Por favor selecciona un puntaje del 1 al 10.");
                return;
            }

            String textoComentario = txtComentario.getText().trim();
            if (textoComentario.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "El comentario es obligatorio para enviar la reseña.",
                        "Falta información", JOptionPane.WARNING_MESSAGE);
                return;
            }

            guardarResena(p, textoComentario, puntajeSeleccionado[0], dialogo);
        });

        JPanel panelBotonWrapper = new JPanel();
        panelBotonWrapper.add(btnGuardar);
        panelSur.add(panelBotonWrapper, BorderLayout.SOUTH);

        dialogo.add(panelSur, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    /**
     * Guarda la reseña del usuario en la base de datos.
     * Actualiza el rating de la película y refresca la vista.
     */
    private void guardarResena(Pelicula p, String comentario, int puntaje, JDialog dialogo) {
        try {
            Resenia r = new Resenia(usuario, p.getID(), comentario, puntaje);
            servicio.registrarResenia(r);
            JOptionPane.showMessageDialog(vista, "¡Gracias! Tu reseña ha sido guardada.");
            dialogo.dispose();

            if (peliculasActuales != null) {
                cargarPeliculas(peliculasActuales);
            }

        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(dialogo, ex.getMessage(), "Error en datos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogo, "Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Carga el poster de una película de forma asíncrona en un hilo aparte.
     * Evita bloquear la interfaz mientras se descarga la imagen.
     */
    private void cargarImagenAsync(String urlString, JLabel labelDestino) {
        Thread hilo = new Thread(() -> {
            try {
                if (urlString == null || urlString.isEmpty())
                    throw new Exception("No URL");
                URL url = new URI(urlString).toURL();
                BufferedImage img = ImageIO.read(url);
                if (img != null) {
                    Image scaled = img.getScaledInstance(150, 225, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(scaled);
                    SwingUtilities.invokeLater(() -> {
                        labelDestino.setText("");
                        labelDestino.setIcon(icon);
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> labelDestino.setText("Sin Imagen"));
            }
        });
        hilo.start();
    }
}