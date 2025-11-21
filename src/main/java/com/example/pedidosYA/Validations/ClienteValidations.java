package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Tarjeta;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.DireccionRepository;
import com.example.pedidosYA.Repository.TarjetaRepository;
import com.example.pedidosYA.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.pedidosYA.Exceptions.BusinessException;

@Component
public class ClienteValidations {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private TarjetaRepository tarjetaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Cliente validarExistencia(Long id)
    {
        return clienteRepository.findById(id).orElseThrow(()-> new BusinessException("No existe ningun cliente con ese id"));
    }

    public void validarDireccion(Long idDireccion, Long idCliente)
    {
        if(!direccionRepository.existsByIdAndClienteId(idDireccion, idCliente)){
            throw new BusinessException("No existe esa direccion en ese cliente");
        }
    }

    public void validarContraseniaActual(Long id, String contrasenia){

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado con id: " + id));

        if (!passwordEncoder.matches(contrasenia, cliente.getContrasenia())) {
            throw new BusinessException("La contraseña actual es incorrecta.");
        }
    }

    public void validarNombreExistente(String nombre)throws BusinessException{
        if(!clienteRepository.existsByNombreYapellido(nombre)){
            throw new BusinessException("El nombre de este cliente no existe");
        }
    }

    public void validarNombreNoDuplicadoConID(Long id, String nombre)throws BusinessException {
        Cliente c = clienteRepository.findByNombreYapellido(nombre);

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }
        if (nombre.length() > 25) {
            throw new BusinessException("El nombre no puede tener más de 25 caracteres.");
        }
        if (c != null && clienteRepository.existsByNombreYapellido(nombre) && c.getId() != id) {
            throw new BusinessException("El nombre ya pertenece a otro restaurante.");
        }
    }

    public void validarNombreCrear(String nombre)throws BusinessException{
        Cliente c = clienteRepository.findByNombreYapellido(nombre);

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }
        if (nombre.length() > 25) {
            throw new BusinessException("El nombre no puede tener más de 25 caracteres.");
        }
    }

    public void validarUsuario(String usuario) {


        if (usuario == null || usuario.trim().isEmpty()) {
            throw new BusinessException("El usuario es obligatorio.");
        }
        if (!usuario.matches("^[a-zA-Z0-9]{3,18}$")) {
            throw new BusinessException("El usuario debe tener entre 3 y 18 caracteres y solo puede contener letras o números.");
        }

        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new BusinessException("El usuario ya existe en el sistema.");
        }
    }

    public void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("El email ya existe en el sistema.");
        }
    }

    public void validarEmailModificacion(Long id, String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }
        Usuario usuarioConEmail = usuarioRepository.findByEmail(email).orElse(null);
        if (usuarioConEmail != null && !usuarioConEmail.getId().equals(id)) {
            throw new BusinessException("El email ya existe en el sistema.");
        }
    }

    public void validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new BusinessException("La contraseña es obligatoria.");
        }
        if (!contrasenia.matches("^[a-zA-Z0-9._]{3,15}$")) {
            throw new BusinessException("La contraseña debe tener entre 3 y 15 caracteres y solo puede contener letras, números, _ o .");
        }
    }

    public void validarPagoEnCliente (Tarjeta pago, Cliente cliente){
        if (!pago.getCliente().getId().equals(cliente.getId())) {
            throw new BusinessException("El pago no pertenece a este cliente");
        }
    }

    public void validarExistenciaTarjeta(Tarjeta tarjeta, Cliente cliente) {
        boolean yaExiste = cliente.getTarjetas().stream().anyMatch(t -> t.getNumero().equals(tarjeta.getNumero()));

        if (yaExiste) {
            throw new BusinessException("Ya tenés una tarjeta registrada con ese número");
        }
    }

}
