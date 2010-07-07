import java.util.NoSuchElementException;

/**
 * Una colección diseñada para manejar elementos con prioridad de procesamiento.
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 */
public class Cola<E> implements Queue<E>{

    // Modelo de representación:
    private Nodo head;
    private Nodo tail;
    private int  tam;

    /**
     * Construye una cola vacia.\n
     * pre: {@code true;}\n
     * post: {@code this.isEmpty();}
     */
    public Cola() {
        this.head = new Nodo();
        this.tail = this.head;
        this.tam = 0;
    }

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
    public boolean add(E e) throws IllegalStateException {
        if (this.isEmpty()) {
            Nodo nuevo = new Nodo(this.head,e);
            this.head.next = nuevo;
            this.head.prev = null;
            this.tail = nuevo;
        } else {
            Nodo nuevo = new Nodo(this.tail,e);
            this.tail.next = nuevo;
            this.tail = nuevo;
        }
        this.tam++;
        return true;
    }

    /**
     * Devuelve, pero no remueve, la cabeza de esta cola. Este método difiere de
     * {@link peek} sólo en que arroja una excepción en caso de que la cola esté
     * vacía.
     * @return El elemento en la cabeza de esta cola.
     */
    public E element() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Esta cola está vacía. Imposible" +
                    "retornar un elemento.");
        } else {
            return (E)this.head.next.elem;
        }
    }

    /**
     * Determina si la cola no tiene elementos.
     * pre: {@code true;}
     * post: el resultado es true si size() &eq; 0. falso en caso contrario
     *
     * @return true si size() &eq; 0. falso en caso contrario
     */
    public boolean isEmpty() {
        return (this.size() == 0);
    }

    /**
     * Se comporta igual que {@link add}, pero difiere en que no arroja la
     * excepción en caso de que no haya espacio disponible, sino que,
     * simplemente, devuelve false y no agrega el elemento.
     * @param e El elemento a encolar
     * @return true en caso de haber encolado {@code e}, y false en caso
     * contrario.
     */
    public boolean offer(E e) {
        if (this.isEmpty()) {
            Nodo nuevo = new Nodo(this.head,e);
            this.head.next = nuevo;
            this.head.prev = null;
            this.tail = nuevo;
        } else {
            Nodo nuevo = new Nodo(this.tail,e);
            this.tail.next = nuevo;
            this.tail = nuevo;
        }
        this.tam++;
        return true;
    }

    /**
     * Devuelve, pero no remueve la cabeza de esta cola, o devuelve {@code null}
     * en caso de que la cola esté vacía.
     * @return el elemento en la cabeza de esta cola, o null, en caso de que
     * esta cola esté vacía.
     */
    public E peek() {
        if (this.isEmpty()) {
            return null;
        } else {
            return (E)this.head.next.elem;
        }
    }

    /**
     * Devuelve y remueve la cabeza de esta cola, o retorna {@code null} en caso
     * de que la cola esté vacía.
     * @return el elemento en la cabeza de esta cola, o {@code null} en caso de
     * que la cola esté vacía.
     */
    public E poll() {
        if (!this.isEmpty()) {
            Nodo aux = this.head.next;
            if (aux.next != null) {
                aux.next.prev = this.head;
                this.head.next = aux.next;
            } else {
                this.head = new Nodo();
                this.tail = this.head;
            }
            this.tam--;
            return (E) aux.elem;
        } else {
            return null;
        }
    }

    /**
     * Devuelve y remueve la cabeza de ésta cola. Difiere de {@link poll} sólo
     * en que arroja una excepción en caso de que esta cola esté vacía.
     * @return el elemento en la cabeza de esta cola, o arroja una excepción si
     * esta cola está vacía.
     * @throws NoSuchElementException Si la cola está vacía
     */
    public E remove() throws NoSuchElementException {
        if (!this.isEmpty()) {
            Nodo aux = this.head.next;
            if (aux.next != null) {
                aux.next.prev = this.head;
                this.head.next = aux.next;
            } else {
                this.head = new Nodo();
                this.tail = this.head;
            }
            this.tam--;
            return (E) aux.elem;
        } else {
            throw new NoSuchElementException("La cola está vacía. Es imposible"+
                    "remover un elemento.");
        }
    }

    /**
     * Retorna el numero de elementos en la cola.
     * pre: {@code true;}
     * post:  el resultado es el numero de elementos en la cola
     *
     * @return el numero de elementos en la cola
     */
    public int size() {
        return this.tam;
    }
    
    /**
     * Clase interna que representa cada uno de los nodos de la lista.
     * @param <E> El tipo de elementos que guarda este nodo.
     */
    private class Nodo <E>{

        // Modelo de representación:
        public E elem;
        public Nodo next;
        public Nodo prev;

        // Constructores:

        /**
         * Crea un Nodo vacio.
         * pre: {@code true;}
         * post: este Nodo esta vacio
         */
        public Nodo() {
            this.elem = null;
            this.next = null;
            this.prev = null;
        }

        /**
         * Crea un nuevo Nodo enlazado a {@code ant} y con el elemento
         * {@code elem}
         * pre: {@code true;}
         * post: {@code this} esta enlazado con {@code ant} y almacena
         * {@code elem}
         * @param ant Nodo a enlazar en la posicion previa
         * @param elem elemento a almacenar
         */
        public Nodo(Nodo ant, E elem) {
            this.elem = elem;
            this.prev = ant;
            this.next = null;
            ant.next = this;
        }

        /**
         * Crea un nuevo Nodo enlazado a {@code sig} y con el elemento
         * {@code elem}
         * pre: {@code true;}
         * post: {@code this} esta enlazado con {@code sig} y almacena
         * {@code elem}
         * @param elem elemento a almacenar
         * @param sig Nodo enlazado en la siguiente posición
         */
        public Nodo(E elem, Nodo sig) {
            this.elem = elem;
            this.prev = null;
            this.next = sig;
            sig.prev = this;
        }

        /**
         * Crea un nuevo Nodo enlazado a {@code ant}  y {@code sig} y con el
         * elemento {@code elem}
         * pre: {@code true;}
         * post: {@code this} esta enlazado con {@code ant} y {@code sig} y
         * almacena {@code elem}
         * @param ant Nodo a enlazar en la posición previa
         * @param elem elemento a almacenar
         * @param sig Nodo a enlazar en la siguiente posición
         */
        public Nodo (Nodo ant, E elem, Nodo sig) {
            this.elem = elem;
            this.prev = ant;
            this.next = sig;
            ant.next = this;
            sig.prev = this;
        }

        /**
         * Hace un clon de {@code this} Nodo.
         * pre: {@code true;}
         * post: el resultado es un Nodo idéntico a {@code this}
         * @return el resultado es un Nodo idéntico a {@code this}
         */
        @Override
        public Nodo clone() {
            Nodo nuevo = new Nodo();
            nuevo.elem = this.elem;
            nuevo.next = this.next;
            nuevo.prev = this.prev;
            return nuevo;
        }

        /**
         * Determina si el objeto {@code o} es igual a {@code this}
         * pre: {@code true;}
         * post: el resultado es true si el objeto {@code o} es igual a
         * {@code this}, false en caso contrario.
         * @param o el objeto a comparar
         * @returntrue si el objeto {@code o} es igual a {@code this}, false en
         * caso contrario.
         */
        @Override
        public boolean equals (Object o) {
            if (o instanceof Nodo) {
                Nodo nuevo = (Nodo) o;
                return (this.next == nuevo.next && this.prev == nuevo.prev &&
                        this.elem.equals(nuevo.elem));
            } else {
                return false;
            }
        }
    }

}
