import java.io.IOException;

/**
 * Excepcion que es lanzada en caso de que el archivo pasado como parámetro no
 * seaun archivo.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class ExcepcionNoEsArchivo extends IOException {

    /**
     * Crea una nueva {@code ExcepcionNoEsArchivo} vacía.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionNoEsArchivo} esta vacia.
     */
    public ExcepcionNoEsArchivo(){
        super();
    }

    /**
     * Crea una nueva {@code ExcepcionNoEsArchivo} con un mensaje asociado.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionNoEsArchivo} contiene el mensaje
     * {@code message}.
     * @param message mensaje lanzado al usuario
     */
    public ExcepcionNoEsArchivo(String message){
        super(message);
    }

    /**
     * Crea una nueva {@code ExcepcionNoEsArchivo} con un mensaje y una causa
     * asociados.
     * <b>Pre</b>: {@code true}
     * <b>Post</b>: esta {@code ExcepcionNoEsArchivo} contiene el mensaje
     * {@code message} y la causa {@code cause}.
     * @param message mensaje lanzado al usuario
     * @param cause la causa de la excepción
     */
    public ExcepcionNoEsArchivo(String message, Throwable cause){
        super(message,cause);
    }
}