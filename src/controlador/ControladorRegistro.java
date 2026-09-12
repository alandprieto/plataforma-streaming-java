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
            String nom = vistaReg.txtNombre.getText().trim();
            String ape = vistaReg.txtApellido.getText().trim();
            String email = vistaReg.txtEmail.getText().trim();
            String pass = new String(vistaReg.txtPassword.getPassword());
            String confirm = new String(vistaReg.txtConfirmarPassword.getPassword());

            if (nom.isEmpty() || ape.isEmpty()) {
                mostrarAdvertencia("Nombre y apellido son obligatorios.");
                return;
            }
            if (email.length() < 5 || !email.contains("@") || email.indexOf("@") != email.lastIndexOf("@")) {
                mostrarAdvertencia("Ingresá un email válido.");
                return;
            }
            if (pass.length() < 6) {
                mostrarAdvertencia("La contraseña debe tener al menos 6 caracteres.");
                return;
            }
            if (!pass.equals(confirm)) {
                mostrarAdvertencia("Las contraseñas no coinciden.");
                return;
            }

            try {
                long dni = Long.parseLong(vistaReg.txtDNI.getText().trim());
                if (dni <= 0) {
                    mostrarAdvertencia("Ingresá un DNI válido.");
                    return;
                }

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

    /**
     * Muestra una advertencia de validación al usuario.
     */
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(vistaReg, mensaje, "Datos inválidos", JOptionPane.WARNING_MESSAGE);
    }
}