package controlador;

import modelo.Cliente;
import servicio.AppImple;
import vista.VistaRegistro;
import vista.VistaLogin;
import excepciones.UsuarioYaExisteException;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorRegistro implements ActionListener {
    private AppImple servicio;
    private VistaRegistro vistaReg;
    private VistaLogin vistaLogin;

    /**
     * Construye el controlador de registro e inicializa los listeners.
     */
    public ControladorRegistro(AppImple servicio, VistaRegistro vistaReg, VistaLogin vistaLogin) {
        this.servicio = servicio;
        this.vistaReg = vistaReg;
        this.vistaLogin = vistaLogin;
        this.vistaReg.btnGuardar.addActionListener(this);
        this.vistaReg.btnCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaReg.btnGuardar) {
            try {
                long dni = Long.parseLong(vistaReg.txtDNI.getText());
                String nom = vistaReg.txtNombre.getText();
                String ape = vistaReg.txtApellido.getText();
                String email = vistaReg.txtEmail.getText();
                String pass = new String(vistaReg.txtPassword.getPassword());

                if(nom.isEmpty() || email.isEmpty()) throw new Exception("Campos vacíos");

                Cliente c = new Cliente(dni, nom, ape, email, pass);
                servicio.registrarCliente(c);

                JOptionPane.showMessageDialog(vistaReg, "Registrado!");
                vistaReg.cerrar();
                vistaLogin.iniciar();

            } catch (UsuarioYaExisteException ex) {
                JOptionPane.showMessageDialog(vistaReg, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vistaReg, "DNI Inválido");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vistaReg, "Error: " + ex.getMessage());
            }
        } else {
            vistaReg.cerrar();
            vistaLogin.iniciar();
        }
    }
}