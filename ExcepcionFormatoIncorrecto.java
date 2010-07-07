import java.io.IOException;

/**
 * Excepcion que es lanzada en caso de que en el archivo pasado como parámetro
 * haya un error de formato.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class ExcepcionFormatoIncorrecto extends IOException {

    /**
     * Crea una nueva {@code ExcepcionFormatoIncorrecto} vacía.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionFormatoIncorrecto} esta
     * vacia.
     */
    public ExcepcionFormatoIncorrecto() {
        super();
    }

    /**
     * Crea una nueva {@code ExcepcionFormatoIncorrecto} con un
     * mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionFormatoIncorrecto} contiene
     * el mensaje {@code message}.
     * @param message mensaje lanzado al usuario
     */
    public ExcepcionFormatoIncorrecto(String message) {
        super(message);
    }

    /**
     * Crea una nueva {@code ExcepcionFormatoIncorrecto} con un
     * mensaje y una causa asociados.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionFormatoIncorrecto} contiene
     * el mensaje {@code message} y la causa {@code cause}.
     * @param message mensaje lanzado al usuario
     * @param cause la causa de la excepción
     */
    public ExcepcionFormatoIncorrecto(String message, Throwable cause) {
        super(message,cause);
    }
}
