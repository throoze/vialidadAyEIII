import java.io.IOException;

/**
 * Excepcion que es lanzada en caso de que en el archivo pasado como parámetro
 * haya un arco repetido.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class ExcepcionArcoRepetido extends IOException {

    /**
     * Crea una nueva {@code ExcepcionArcoRepetido} vacía.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArcoRepetido} esta
     * vacia.
     */
    public ExcepcionArcoRepetido() {
        super();
    }


    /**
     * Crea una nueva {@code ExcepcionArcoRepetido} con un
     * mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArcoRepetido} contiene
     * el mensaje {@code msg}.
     * @param msg mensaje lanzado al usuario
     */
    public ExcepcionArcoRepetido(String msg) {
        super(msg);
    }

    /**
     * Crea una nueva {@code ExcepcionArcoRepetido} con un
     * mensaje y una causa asociados.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArcoRepetido} contiene
     * el mensaje {@code msg} y la causa {@code cause}.
     * @param msg mensaje lanzado al usuario
     * @param cause la causa de la excepción
     */
    public ExcepcionArcoRepetido(String msg, Throwable cause) {
        super(msg,cause);
    }
}
