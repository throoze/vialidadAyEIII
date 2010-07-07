import java.io.IOException;

/**
 * Excepcion que es lanzada en caso de que en el archivo pasado como parámetro
 * haya una inconsistencia entre el número de nodos indicados y los nodos
 * utilizados.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class ExcepcionInconsistenciaNumeroDeNodos extends IOException {

    /**
     * Crea una nueva {@code ExcepcionInconsistenciaNumeroDeNodos} vacía.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionInconsistenciaNumeroDeNodos} esta
     * vacia.
     */
    public ExcepcionInconsistenciaNumeroDeNodos() {
        super();
    }


    /**
     * Crea una nueva {@code ExcepcionInconsistenciaNumeroDeNodos} con un
     * mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionInconsistenciaNumeroDeNodos} contiene
     * el mensaje {@code msg}.
     * @param msg mensaje lanzado al usuario
     */
    public ExcepcionInconsistenciaNumeroDeNodos(String msg) {
        super(msg);
    }

    /**
     * Crea una nueva {@code ExcepcionInconsistenciaNumeroDeNodos} con un
     * mensaje y una causa asociados.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionInconsistenciaNumeroDeNodos} contiene
     * el mensaje {@code msg} y la causa {@code cause}.
     * @param msg mensaje lanzado al usuario
     * @param cause la causa de la excepción
     */
    public ExcepcionInconsistenciaNumeroDeNodos(String msg, Throwable cause) {
        super(msg,cause);
    }
}
