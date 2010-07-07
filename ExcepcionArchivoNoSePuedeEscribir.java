import java.io.IOException;

/**
 * Excepcion que es lanzada en caso de que el archivo pasado como parámetro no
 * se pueda escribir.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class ExcepcionArchivoNoSePuedeEscribir extends IOException {

    /**
     * Crea una nueva {@code ExcepcionArchivoNoSePuedeEscribir} vacía.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArchivoNoSePuedeEscribir} esta
     * vacia.
     */
    public ExcepcionArchivoNoSePuedeEscribir(){
        super();
    }

    /**
     * Crea una nueva {@code ExcepcionArchivoNoSePuedeEscribir} con un
     * mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArchivoNoSePuedeEscribir} contiene
     * el mensaje {@code message}.
     * @param message mensaje lanzado al usuario
     */
    public ExcepcionArchivoNoSePuedeEscribir(String message){
        super(message);
    }

    /**
     * Crea una nueva {@code ExcepcionArchivoNoSePuedeEscribir} con un
     * mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionArchivoNoSePuedeEscribir} contiene
     * el mensaje {@code message}.
     * @param message mensaje lanzado al usuario
     */
    public ExcepcionArchivoNoSePuedeEscribir(String message, Throwable cause){
        super(message,cause);
    }
}