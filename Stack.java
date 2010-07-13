/**
 * La clase Stack representa la pila en la cual el ultimo elemento en entrar es
 * el primero en salir. Extiende la clase Lista, asi que utiliza su mismo modelo
 * de representación, provee un método iterator, entre otros muy útiles. Muchos
 * métodos propios de la clase Stack actuan igual que otros de Lista, pero se
 * reescriben por cuestión de abstraccion, y para apegarse al modelo de pila
 * ofrecido por java.
 *
 * @author Victor De Ponte, carnet 05-38087
 * @see Lista, List
 */
public class Stack<E> extends Lista<E> implements List<E>{

    /* MODELO DE REPRESENTACIÓN:
     * Se cambió el encapsulamiento del modelo de representacion de la lista a
     * "protected", para que pudiese ser visto desde aqui, ya que es muy útil
     * poder acceder a él, sobre todo en el método search.
     *
     * Por la misma razón, se cambio el encapsulamiento de la clase Nodo
     */

    // CONSTRUCTOR:

    /**
     * Constructor que llama al constructor de la superclase (Lista). Referirse
     * a esa especificación.
     */
    public Stack() {
        super();
    }

    // MÉTODOS:

    /**
     * Se comporta exactamente igual que la función List.isEmpty()
     * @return true si y solo si esta pila está vacía, false en caso contrario
     */
    public boolean empty() {
        return (this.tam == 0);
    }

    /**
     * Muestra el objeto en el tope de la pila, sin removerlo de ésta.
     * <b>Pre</b>: true
     * <b>Post</b>: se devuelve el elemento que esta en el tope de la pila, sin
     * alterar la misma; o null en caso de que esta pila este vacia.
     * @return el elemento que esta en el tope de la pila; null en caso de que
     * esta pila esté vacia.
     */
    public E peek() {
        if (!this.isEmpty()) {
            return (E) get(this.size()-1);
        } else {
            return null;
        }
    }

    /**
     * Remueve el elemento en el tope de esta pila, y lo devuelve como valor de
     * la funcion.
     * <b>Pre</b>: true
     * <b>Post</b>: Se elimina el elemento en el tope de esta pila, y se
     * devuelve éste. En caso de que ésta pila esté vacía, se devuelve null.
     * @return El elemento en el tope de ésta pila. En caso de que la pila esté
     * vacía, se devuelve null.
     */
    public E pop() {
        if (!this.isEmpty()) {
            return (E) this.remove(this.size() -1);
        } else {
            return null;
        }
    }

    /**
     * Se comporta de la misma manera que el método List.add(E elem), excepto
     * por que ésta devuelve el elemento insertado. Referirse
     * a esa especificación.
     * @param item El elemento a ser insertado.
     * @return El elemento insertado {@code item}
     */
    public E push(E item) {
        this.add(item);
        return item;
    }

    /**
     * Si el objeto {@code o} ocurre en esta pila, éste método retorna la
     * distancia desde el tope de la pila de la ocurrencia más cercana al tope
     * de la pila. El elemento justo en el tope esta considerado a una distancia
     * 1. En caso de que el elemento {@code o} no ocurra  en la pila, se retorna
     * el valor {@code -1}. Se utiliza el método {@code equals} para comparar
     * los elementos en la pila.
     * <b>Pre</b>: true
     * <b>Post</b>: Se devolverá la distancia a la que se encuentra la
     * ocurrencia más cercana al tope de la pila del elemento {@code o},
     * empezando a contar desde 1. Si el elemento no ocurre, se devolverá -1.
     * @param o El elemento a buscar
     * @return La distancia desde el tope de la pila del objeto o. -1 si o no
     * ocurre en la pila.
     */
    public int search(Object o) {
        Nodo aux = this.tail;
        int position = 1;
        while (aux.prev != null && aux != null) {
            if (aux.elem != null && aux.elem.equals(o)) {
                return position;
            }
            aux = aux.prev;
            position++;
        }
        return -1;
    }
}