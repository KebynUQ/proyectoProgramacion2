package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.SistemaEventos;

import java.util.ArrayList;

public class UsuariosController {

    @FXML
    private TextField txtBuscarUsuario;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, String> colIdUsuario;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colCorreo;

    @FXML
    private TableColumn<Usuario, String> colTelefono;

    @FXML
    private Label lblMensaje;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();

    @FXML
    public void initialize() {
        colIdUsuario.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdUsuario()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreCompleto()));
        colCorreo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorreo()));
        colTelefono.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));
        cargarUsuarios();
    }

    @FXML
    private void buscarUsuario() {
        String texto = txtBuscarUsuario.getText() == null ? "" : txtBuscarUsuario.getText().trim().toLowerCase();
        ArrayList<Usuario> resultado = new ArrayList<>();

        for (Usuario usuario : sistemaEventos.getUsuarios()) {
            if (usuario.getNombreCompleto().toLowerCase().contains(texto)
                    || usuario.getCorreo().toLowerCase().contains(texto)) {
                resultado.add(usuario);
            }
        }

        tablaUsuarios.setItems(FXCollections.observableArrayList(resultado));
        lblMensaje.setText("Usuarios encontrados: " + resultado.size());
    }

    @FXML
    private void cargarUsuarios() {
        tablaUsuarios.setItems(FXCollections.observableArrayList(sistemaEventos.getUsuarios()));
        lblMensaje.setText("Tabla actualizada.");
    }

    @FXML
    private void verComprasUsuario() {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        lblMensaje.setText(usuario == null ? "Seleccione un usuario." : "Compras registradas: " + usuario.getCompras().size());
    }
}
