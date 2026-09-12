package controlador;

import modelo.Usuario;
import servicio.AppImple;
import vista.VistaLogin;
import vista.VistaPrincipal;
import vista.VistaRegistro;
import excepciones.CredencialesInvalidasException;
import database.AutoCargaPeliculas;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorLogin implements ActionListener {
    private AppImple servicio;
    private VistaLogin vistaLogin;

    /**
     * Construye el controlador de login e inicializa los listeners.
     */
    public ControladorLogin(AppImple servicio, VistaLogin vistaLogin) {
        this.servicio = servicio;
        this.vistaLogin = vistaLogin;
        this.vistaLogin.btnIngresar.addActionListener(this);
        this.vistaLogin.btnRegistrar.addActionListener(this);
    }

    /**
     * Maneja los eventos de los botones de login y registro.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.btnIngresar) {
            procesarLogin();
        } else if (e.getSource() == vistaLogin.btnRegistrar) {
            VistaRegistro vReg = new VistaRegistro();
            new ControladorRegistro(servicio, vReg, vistaLogin);
            vistaLogin.setVisible(false);
            vReg.setVisible(true);
        }
    }

    /**
     * Procesa el intento de login del usuario.
     */
    private void procesarLogin() {
        String email = vistaLogin.txtEmail.getText();
        String pass = new String(vistaLogin.txtPassword.getPassword());

        try {
            Usuario u = servicio.autenticarUsuario(email, pass);
            JOptionPane.showMessageDialog(vistaLogin, "Bienvenido " + u.getNombre());

            if (servicio.hayPeliculasCargadas()) {
                vistaLogin.cerrar();
                VistaPrincipal vp = new VistaPrincipal();
                new ControladorPrincipal(servicio, vp, u);
                vp.setVisible(true);
                return;
            }

            final JDialog dialog = new JDialog(vistaLogin, "Cargando, por favor aguarde...", true);
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialog.setSize(300, 100);
            dialog.setLocationRelativeTo(vistaLogin);
            JProgressBar progress = new JProgressBar();
            progress.setIndeterminate(true);
            dialog.add(progress);

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    AutoCargaPeliculas.cargarSiExiste(servicio);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    dialog.dispose();
                    vistaLogin.cerrar();
                    VistaPrincipal vp = new VistaPrincipal();
                    new ControladorPrincipal(servicio, vp, u);
                    vp.setVisible(true);
                }
            };

            worker.execute();
            dialog.setVisible(true);

        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(vistaLogin, ex.getMessage(), "Error Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}