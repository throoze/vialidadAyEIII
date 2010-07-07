import java.io.IOException;

/**
 * Excepcion que es lanzada en caso de que el archivo pasado como parámetro no
 * se pueda leer.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class ExcepcionArchivoNoSePuedeLeer extends IOException{

    /**
     * Crea una nueva {@code ExcepcionArchivoNoSePuedeLeer} vacía.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArchivoNoSePuedeLeer} esta
     * vacia.
     */
    public ExcepcionArchivoNoSePuedeLeer(){
        super();
    }

    /**
     * Crea una nueva {@code ExcepcionArchivoNoSePuedeLeer} con un
     * mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArchivoNoSePuedeLeer} contiene
     * el mensaje {@code message}.
     * @param message mensaje lanzado al usuario
     */
    public ExcepcionArchivoNoSePuedeLeer(String message){
        super(message);
    }

    /**
     * Crea una nueva {@code ExcepcionArchivoNoSePuedeLeer} con un
     * mensaje y una causa asociados.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArchivoNoSePuedeLeer} contiene
     * el mensaje {@code message} y la causa {@code cause}.
     * @param message mensaje lanzado al usuario
     * @param cause la causa de la excepción
     */
    public ExcepcionArchivoNoSePuedeLeer(String message, Throwable cause){
        super(message,cause);
    }
}