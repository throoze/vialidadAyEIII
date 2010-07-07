import java.io.IOException;

/**
 * Excepcion que es lanzada en caso de que en el archivo pasado como parámetro
 * haya una inconsistencia entre el número de arcos indicados y el número de
 * arcos utilizados.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class ExcepcionInconsistenciaNumeroDeArcos extends IOException {

    /**
     * Crea una nueva {@code ExcepcionInconsistenciaNumeroDeArcos} vacía.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionInconsistenciaNumeroDeArcos} esta
     * vacia.
     */
    public ExcepcionInconsistenciaNumeroDeArcos() {
        super();
    }

    /**
     * Crea una nueva {@code ExcepcionInconsistenciaNumeroDeArcos} con un
     * mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionInconsistenciaNumeroDeArcos} contiene
     * el mensaje {@code msg}.
     * @param msg mensaje lanzado al usuario
     */
    public ExcepcionInconsistenciaNumeroDeArcos(String msg) {
        super(msg);
    }

    /**
     * Crea una nueva {@code ExcepcionInconsistenciaNumeroDeArcos} con un
     * mensaje y una causa asociados.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionInconsistenciaNumeroDeArcos} contiene
     * el mensaje {@code msg} y la causa {@code cause}.
     * @param msg mensaje lanzado al usuario
     * @param cause la causa de la excepción
     */
    public ExcepcionInconsistenciaNumeroDeArcos(String msg, Throwable cause) {
        super(msg,cause);
    }
}
