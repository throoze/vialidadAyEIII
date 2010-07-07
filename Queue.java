
import java.util.NoSuchElementException;

/**
 * Una colección diseñada para manejar elementos con prioridad de procesamiento.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 */
public interface Queue<E> {

    /**
     * Inserta el elemento especificado en esta cola, si es posible hacerlo 
     * inmediatamente sin violar las restricciones de capacidad, retornando true
     * si se tiene éxito y lanzando una IllegalStateException si no hay espacio 
     * disponible actualmente.
     * @param e Elemento a insertar
     * @return true si se logra encolar el elemento, en caso contrario arroja 
     * una excepcion.
     * @throws IllegalStateException en caso de que no haya espacio disponible 
     * en esta cola
     */
    public boolean add(E e) throws IllegalStateException;

    /**
     * Devuelve, pero no remueve, la cabeza de esta cola. Este método difiere de
     * {@link peek} sólo en que arroja una excepción en caso de que la cola esté
     * vacía.
     * @return El elemento en la cabeza de esta cola.
     */
    public E element() throws NoSuchElementException;

    /**
     * Determina si la cola no tiene elementos.
     * pre: {@code true;}
     * post: el resultado es true si size() &eq; 0. falso en caso contrario
     *
     * @return true si size() &eq; 0. falso en caso contrario
     */
    public boolean isEmpty();

    /**
     * Se comporta igual que {@link add}, pero difiere en que no arroja la
     * excepción en caso de que no haya espacio disponible, sino que,
     * simplemente, devuelve false y no agrega el elemento.
     * @param e El elemento a encolar
     * @return true en caso de haber encolado {@code e}, y false en caso
     * contrario.
     */
    public boolean offer(E e);

    /**
     * Devuelve, pero no remueve la cabeza de esta cola, o devuelve {@code null}
     * en caso de que la cola esté vacía.
     * @return el elemento en la cabeza de esta cola, o null, en caso de que
     * esta cola esté vacía.
     */
    public E peek();

    /**
     * Devuelve y remueve la cabeza de esta cola, o retorna {@code null} en caso
     * de que la cola esté vacía.
     * @return el elemento en la cabeza de esta cola, o {@code null} en caso de
     * que la cola esté vacía.
     */
    public E poll();

    /**
     * Devuelve y remueve la cabeza de ésta cola. Difiere de {@link poll} sólo
     * en que arroja una excepción en caso de que esta cola esté vacía.
     * @return el elemento en la cabeza de esta cola, o arroja una excepción si
     * esta cola está vacía.
     * @throws NoSuchElementException Si la cola está vacía
     */
    public E remove() throws NoSuchElementException;

    /**
     * Retorna el numero de elementos en la cola.
     * pre: {@code true;}
     * post:  el resultado es el numero de elementos en la cola
     *
     * @return el numero de elementos en la cola
     */
    public int size();
}
