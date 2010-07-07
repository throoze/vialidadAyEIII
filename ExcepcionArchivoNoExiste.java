import java.io.IOException;

/**
 * Excepcion que es lanzada en caso de que el archivo pasado como parámetro no
 * exista.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class ExcepcionArchivoNoExiste extends IOException {

    /**
     * Crea una nueva {@code ExcepcionArchivoNoExiste} vacía.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArchivoNoExiste} esta
     * vacia.
     */
    public ExcepcionArchivoNoExiste(){
        super();
    }

    /**
     * Crea una nueva {@code ExcepcionArchivoNoExiste} con un
     * mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArchivoNoExiste} contiene
     * el mensaje {@code message}.
     * @param message mensaje lanzado al usuario
     */
    public ExcepcionArchivoNoExiste(String message){
        super(message);
    }

    /**
     * Crea una nueva {@code ExcepcionArchivoNoExiste} con un
     * mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArchivoNoExiste} contiene
     * el mensaje {@code message}.
     * @param message mensaje lanzado al usuario
     */
    public ExcepcionArchivoNoExiste(String message, Throwable cause){
        super(message,cause);
    }
}