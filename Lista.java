import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Representa el objeto Lista(E), una lista de elementos del tipo E. implementa
 * la interfaz List(E).
 *
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class Lista<E> implements List<E>{

    // Modelo de representación:
    protected Nodo head;
    protected Nodo tail;
    protected int  tam;

    // Constructores:

    /**
     * Construye una lista vacia.\n
     * pre: {@code true;}\n
     * post: {@code this.isEmpty();}
     */
    public Lista() {
        this.head = new Nodo();
        this.tail = this.head;
        this.tam = 0;
    }

    /**
     * Agrega <i>element</i> a la lista.
     * pre: {@code true;}
     * post: {@code this.contains(element);}
     *
     * @param element Elemento a insertar de tipo E, con el que se declaro el
     * objeto lista particular.
     * @return true si el elemento fue insertado, false en caso contrario
     */
    public boolean add(E element) {
        if (this.isEmpty()) {
            Nodo nuevo = new Nodo(this.head,element);
            this.head.next = nuevo;
            this.head.prev = null;
            this.tail = nuevo;
        } else {
            Nodo nuevo = new Nodo(this.tail,element);
            this.tail.next = nuevo;
            this.tail = nuevo;
        }
        this.tam++;
        return true;
    }

    /**
     * Agrega <i>element</i> a la lista en la posicion <i>index</i>, si index
     * &gt; this.size() el elemento se agrega al final de lista.
     * pre: {@code 0 <= index && index <= this.tam;}
     * post: {@code this.contains(element) && this.indexOf(element) == index;}
     * tambien;
     * pre: {@code this.tam < index;}
     * post: {@code this.contains(element) && }
     * {@code this.indexOf(element) == this.tam-1;}
     *
     * @param element Elemento de tipo E, con el que se declaro el objeto
     * lista particular,
     * @return true si el elemento fue insertado, false en caso contrario.
     */
    public boolean add(int index, E element) {
        if (0 <= index && index <= this.tam) {
            if (index < this.tam) {
                Nodo aux;
                if (index < (this.tam/2)) {
                    int k = 0;
                    aux = this.head.next;
                    while (k < index) {
                        aux = aux.next;
                        k++;
                        System.out.println("busco en la posicion " + k);
                    }
                    System.out.println("aux esta en la posicion " + k);
                } else {
                    int k = this.tam - 1;
                    aux = this.tail;
                    while (k > index) {
                        aux = aux.prev;
                        k--;
                        System.out.println("busco en la posicion " + k);
                    }
                    System.out.println("aux esta en la posicion " + k);
                }
                Nodo nuevo = new Nodo(aux.prev, element, aux);
                this.tam++;
            } else if (index == this.tam) {
                if (this.isEmpty()) {
                    this.add(element);
                } else {
                    Nodo nuevo = new Nodo(this.tail, element);
                    this.tail.next = nuevo;
                    this.tail = nuevo;
                    this.tail.next = null;
                    this.tam++;
                }
            }
            return true;
        } else if (this.tam < index) {
            this.add(element);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Vacia la lista {@code this;}
     * pre: {@code true;}
     * post: {@code this.isEmpty();}
     */
    public void clear() {
        this.head = new Nodo();
        this.tail = this.head;
        this.tam = 0;
    }

    @Override
    public List clone() {
        List<E> laux = new Lista();
        Nodo aux = this.head.next;
        while (aux != null) {
            laux.add((E)aux.elem);
            aux = aux.next;
        }
        return laux;
    }

    /**
     * Permite saber si {@code this} lista contiene el elemento {@code o}
     * pre: {@code true;}
     * post: el resultado es true si {@code o} esta en esta lista, false en caso
     * contrario.
     * 
     * @param o Objeto a consultar
     * @return true si {@code o} esta en esta lista, false en caso contrario.
     */
    public boolean contains(Object o) {
        Nodo aux = this.head;
        while (aux.next != null) {
            aux =  aux.next;
            if (aux.elem.equals((E)o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Permite saber si una lista es igual a {@code this} lista.
     * pre: {@code true;}
     * post: el resultado es true si {@code this.size() == o.size()} && todos
     * los elementos que estan en {@code this} tambien estan en {@code o}, en la
     * misma posición; false en caso contrario.
     *
     * @param o Lista a comparar con {@code this}
     * @return true si {@code this.size() == o.size()} && todos los elementos
     * que estan en {@code this} tambien estan en {@code o}, en la misma
     * posición; false en caso contrario.
     */
    public boolean equals(List<E> o) {
        Object[] esta = this.toArray();
        Object[] otra = o.toArray();
        if (esta.length != otra.length) {
            return false;
        }
        for (int k = 0; k < esta.length; k++) {
            if (!(esta[k].equals(otra[k]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Devuelve el elemento almacenado en la posicion {@code index} de la lista
     * {@code this}.
     * pre: {@code true;}
     * post: el resultado es el elemento {@code e} tal que
     * {@code this.contains(e) y this.indexOf(e) == index si} {@code index} &le;
     * this.size(); null si index &gt; size()
     *
     * @param index posicion del elemento a devolver.
     * @return el elemento {@code e} tal que
     * {@code this.contains(e) && this.indexOf(e) == index} si
     * {@code index &le; this.size()}; null si {@code index &gt; this.size()}
     */
    public E get(int index) {
        if (0 <= index && index < this.size()) {
            if (index == 0) {
                return (E) this.head.next.elem;
            } else if (index == this.size() -1) {
                return (E) this.tail.elem;
            } else {
                Object[] lista = this.toArray();
                return (E) lista[index];
            }
        } else {
            return null;
        }
    }

    /**
     * Determina la posicion del elemento <i>o</i> en la lista
     * pre: {@code true;}
     * post: si el elemento esta en la lista retorna y su posicion, en caso
     * contario retorna -1
     *
     * @param o el objeto
     * @return si el elemento esta en la lista retorna y su posicion, en caso
     * contario retorna -1
     */
    public int indexOf(Object o) {
        Object[] lista = this.toArray();
        for ( int k = 0; k < lista.length; k++) {
            if ( lista[k].equals((E)o) ) {
                return k;
            }
        }
        return -1;
    }

    /**
     * Determina si la lista no tiene elementos.
     * pre: {@code true;}
     * post: el resultado es true si size() &eq; 0. falso en caso contrario
     *
     * @return true si size() &eq; 0. falso en caso contrario
     */
    public boolean isEmpty() {
        return (this.size() == 0);
    }

    /**
     * Elimina el elemento en la posicion index.
     * pre: {@code true;}
     * post: el resultado es el elemento eliminado, si no se elimino ningun
     * elemento retorna null.
     *
     * @param index la posicion del elemento a eliminar, 0 &le; index &lt;
     * size()
     * @return el elemento eliminado, si no se elimino ningun elemento retorna
     * null
     */
    public E remove(int index) {
        if (index < this.tam -1) {
            Nodo aux = this.head;
            for (int k = -1; k < index && aux.next != null; k++) {
                aux = aux.next;
            }
            aux.prev.next = aux.next;
            if (aux.next != null) {
                aux.next.prev = aux.prev;
            }
            this.tam--;
            return (E) aux.elem;
        } else if (index == this.tam -1) {
            Nodo nodo = this.tail;
            this.tail = this.tail.prev;
            this.tail.next = null;
            this.tam--;
            return (E) nodo.elem;
        } else {
            return null;
        }
    }

    /**
     * Elimina el elemento <i>o</i> de la lista <i>this</i>.
     * pre: {@code true;}
     * post: el resultado es true si el elemento existia y fue eliminado, false
     * en caso contrario.
     *
     * @param o el elemento a eliminar.
     * @return true si el elemento existia y fue eliminado, false en caso
     * contrario.
     */
    public boolean remove(Object o) {
        if (this.isEmpty()) {
            return false;
        } else {
            Nodo aux = this.head;
            boolean stop = false;
            while ( aux.next != null && !stop) {
                aux = aux.next;
                if (aux.elem.equals((E)o)) {
                    stop = true;
                }
            }
            if (aux.elem.equals((E)o)){
                if (this.tail.equals(aux)) {
                    this.tail = this.tail.prev;
                    this.tail.next = null;
                } else {
                    aux.prev.next = aux.next;
                    aux.next.prev = aux.prev;
                }
                this.tam--;
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna el numero de elementos enla lista.
     * pre: {@code true;}
     * post:  el resultado es el numero de elementos en la lista
     *
     * @return el numero de elementos en la lista
     */
    public int size() {
        return this.tam;
    }

    /**
     * Retorna un nuevo arreglo que contiene todos los elementos
     * en esta lista {@code List}.
     * pre: {@code true;}
     * post: el resultado es un arreglo con los elementos contenidos en la lista
     * {@code this}
     *
     * @return un arreglo con los elementos contenidos en la lista {@code this}
     */
    public Object[] toArray() {
        Object[] lista = new Object[this.size()];
        Nodo aux = this.head;
        for (int k = 0; k < lista.length; k++) {
            lista[k] = aux.next.elem;
            aux = aux.next;
        }
        return lista;
    }

    /**
     * Retorna la representacion en String de esta {@code Lista}
     * pre: {@code true;}
     * post: el String resultado es la representacion en String de esta
     * {@code Lista}
     *
     * @return la representacion en String de esta {@code Lista}
     */
    @Override
    public String toString() {
        Object[] lista = this.toArray();
        String s = "";
        for (int k = 0; k < lista.length; k++) {
            s += ((E)lista[k]).toString() + "\n";
        }
        return s;
    }

    /**
     * Método que construye un nuevo iterador sobre {@code this Lista}
     * <b>Pre</b>: true;
     * <b>Post</b>: Se genera un nuevo {@code Iterator} definido sobre
     * {@code this Lista}
     * @return Un Iterador sobre {@code this Lista}.
     */
    public Iterator iterator() {
        return new GeneradorDeElementos(this);
    }

    /**
     * Clase interna que representa cada uno de los nodos de la lista.
     * @param <E> El tipo de elementos que guarda este nodo.
     */
    protected class Nodo <E>{

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

    /**
     * Clase interna que implementa la interfaz Iterator para esta Lista
     * @param <E> El tipo sobre el cual está definida esta Lista
     */
    private class GeneradorDeElementos <E> implements Iterator {

        // El siguiente elemento a devolver
        private Nodo prox;

        /**
         * Construye un nuevo {@code GeneradorDeElementos} definido sobre
         * {@code this Lista}
         * <b>Pre</b>: true;
         * <b>Post</b>: Se genera un nuevo {@code GeneradorDeElementos} definido
         * sobre la {@code Lista lista}
         * @param lista la {@code Lista} sobre la cual se construirá el nuevo
         * {@code GeneradorDeElementos}
         */
        public GeneradorDeElementos(Lista<E> lista) {
            if (lista != null) {
                this.prox = lista.head;
            } else {
                this.prox = null;
            }
        }

        /**
         * Determina si hay un proximo elemento a devolver por
         * {@code this GeneradorDeElementos}
         * <b>Pre</b>: true;
         * <b>Post</b>: El resultado es true si se puede devolver un nuevo
         * elemento, false en caso contrario.
         * @return true si se puede devolver un nuevo elemento, false en
         * caso contrario.
         */
        public boolean hasNext() {
            return ((this.prox != null) &&
                    (this.prox.next != null) &&
                    (this.prox.next.elem != null));
        }

        /**
         * Devuelve el siguiente elemento de {@code this GeneradorDeElementos}
         * <b>Pre</b>: {@code this.hasNext()}
         * <b>Post</b>: si {@code this.hasNext()}, entonces se devuelve el
         * siguiente elemento de {@code this GeneradorDeElementos}. Si no, se
         * arroja la excepción {@code NoSuchElementException}
         * @return El siguiente elemento a devolver por
         * {@code this GeneradorDeElementos}
         * @throws NoSuchElementException En caso de que
         * {@code !this.hasNext()}
         */
        public E next() throws NoSuchElementException {
            if (this.prox != null &&
                this.prox.next != null &&
                this.prox.next.elem != null) {
                E siguiente = (E)this.prox.next.elem;
                this.prox = this.prox.next;
                return siguiente;
            } else {
                throw new NoSuchElementException("Esta Lista no contiene más " +
                        "elementos...");
            }
        }

        /**
         * Este método no se implementara en esta clase por el momento
         */
        public void remove() {
            throw new UnsupportedOperationException("Método no implementado" +
                    " todavía");
        }
    }
}