/**
 * Clase que representa el objeto Arco de un Digrafo. Puede almacenar un
 * identificador para el arco, el costo de usar ese arco, el nodo fuente y el
 * nodo destino del arco
 *
 * @author Les profs
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class Arc {

    // Modelo de representación:
    private String id = "";
    private double cost;
    private int src = -1;
    private int dst = -1;

    // Constructores:

    /**
     * Crea un arco vacio.
     * pre: true;
     * post: this.isEmpty();
     */
    public Arc() {
        this.id = null;
        this.cost = -1.0;
    }

    /**
     * Crea un arco entre los nodos {@code src} y {@code dst}
     * pre: true, aunque en el programa llamador, probablemente tanto
     * {@code src} como {@code dst} deban pertenecer al Digrafo en uso.
     * post: este arco ira desde el nodo {@code src} hasta el nodo {@code dst}
     *
     * @param src nodo origen del arco
     * @param dst nodo destino del arco
     */
    public Arc(int src, int dst) {
       this.src = src;
       this.dst = dst;
    }

    /**
     * Crea un arco entre los nodos {@code src} y {@code dst} con el costo
     * {@code cost}
     * pre: true, aunque en el programa llamador, probablemente tanto
     * {@code src} como {@code dst} deban pertenecer al Digrafo en uso.
     * post: este arco ira desde el nodo {@code src} hasta el nodo {@code dst} y
     * su costo sera {@code cost}
     * @param src nodo origen del arco
     * @param dst nodo destino del arco
     * @param cost costo de usar el arco
     */
    public Arc(int src, int dst, double cost) {
       this.src = src;
       this.dst = dst;
       this.cost = cost;
    }

    /**
     * Crea un arco entre los nodos {@code src} y {@code dst} con el
     * identificador {@code ident}
     * pre: true, aunque en el programa llamador, probablemente tanto
     * {@code src} como {@code dst} deban pertenecer al Digrafo en uso.
     * post: este arco ira desde el nodo {@code src} hasta el nodo {@code dst} y
     * su identificador sera {@code ident}
     * @param src nodo origen del arco
     * @param dst nodo destino del arco
     * @param ident identificador del arco
     */
    public Arc(int src, int dst, String ident) {
       this.src = src;
       this.dst = dst;
       this.id = ident;
    }

    /**
     * Crea un arco entre los nodos {@code src} y {@code dst} con el costo
     * {@code cost}
     * pre: true, aunque en el programa llamador, probablemente tanto
     * {@code src} como {@code dst} deban pertenecer al Digrafo en uso.
     * post: este arco ira desde el nodo {@code src} hasta el nodo {@code dst},
     * su costo sera {@code cost} y su identificados sera {@code id}
     * @param src nodo origen del arco
     * @param dst nodo destino del arco
     * @param cost costo de usar el arco
     * @param id identificador del arco
     */
    public Arc(int src, int dst, double cost, String id) {
       this.src = src;
       this.dst = dst;
       this.cost = cost;
       this.id = id;
    }

    /**
     * Retorna un nuevo {@code Arc} con el mismo fuente y el mismo destino que
     * este Arc.
     * pre: true;
     * post: {@code resultado}.equals({@code this});
     *
     * @return un arco tal que {@code resultado}.equals({@code this})
     * @see java.lang.Cloneable
     */
    @Override
    public Object clone() {
       return new Arc(src, dst);
    }

    /**
     * Indica si el Arc a es igual a este Arc
     * pre: a instanceof Arc
     * post: el resultado equivale a que {@code this} arco es igual al arco
     * {@code a}
     * @param a Arc con el que se desea comparar.
     * @return true si los fuentes y destinos de los dos arcos son iguales;
     * false en caso contrario.
     */
    @Override
    public boolean equals(Object a) {
       if (a instanceof Arc) {
           Arc nuevo = (Arc) a;
           return (this.dst == nuevo.dst &&
                   this.src == nuevo.src);
       } else {
           return false;
       }
    }

    /**
     * Pertmite obtener el costo de un arco: de ir de arco fuente al arco
     * destino.
     * pre: {@code true;}
     * post: {@code resultado == this.cost;}
     *
     * @return costo del Arco.
     */
    public double getCost() {
       return cost;
    }

    /**
     * Pertmite establecer el costo a este Arc.
     * pre: {@code true;}
     * post: {@code this.cost == c;}
     *
     * @param c el costo a ser asignado a este Arco.
     */
    public void setCost(double c) {
       cost = c;
    }

    /**
     * Devuelve la etiqueta del arco.
     * pre: {@code true;}
     * post: {@code resultado.equals(this.id);}
     *
     * @return La etiqueta del arco
     * @since 2.0
     */
    public String getId() {
        return this.id;
    }

    /**
     * Permite establecer la etiqueta del arco {@code this}.
     * pre: {@code true;}
     * post: {@code this.id.equals(s);}
     * @param s Nueva etiqueta para el arco.
     * @since 2.0
     */
    public void setId(String s) {
        this.id = s;
    }

    /**
     * Permite obtener el nodo fuente de {@code this} Arc
     * pre: {@code true;}
     * post: {@code resultado == this.src;}
     *
     * @return el nodo fuente de {@code this Arc}
     */
    public int getSrc() {
        return this.src;
    }

    /**
     * Permite saber si un arco esta vacio o no;
     * <b>Pre</b>: true;
     * <b>Post</b>: devuelve true si este arco esta vacio, false en caso
     * contrario.
     * @return true si este arco esta vacio, false en caso contrario.
     */
    public boolean isEmpty() {
        return ((this.cost == -1.0 &&
                 this.dst == -1 &&
                 this.id.equals("") &&
                 this.src == -1) ||
                 this == null);
    }

    /**
     * Permite establecer el nodo fuente de {@code this Arc}
     * pre: {@code true;}, aunque en el programa llamador probablemente
     * {@code fuente} deba pertenecer al Digrafo en uso.
     * post: {@code this.src == fuente;}
     *
     * @param fuente Nuevo nodo fuente para el arco
     */
    public void setSrc(int fuente) {
        this.src = fuente;
    }

    /**
     * Permite obtener el nodo destino de {@code this} Arc
     * pre: {@code true;}
     * post: {@code resultado == this.dst;}
     *
     * @return el nodo destino de {@code this Arc}
     */
    public int getDst() {
        return this.dst;
    }

    /**
     * Permite establecer el nodo destino de {@code this Arc}
     * pre: {@code true;}, aunque en el programa llamador probablemente
     * {@code destino} deba pertenecer al Digrafo en uso.
     * post: {@code this.dst == destino;}
     *
     * @param destino Nuevo nodo destino para el arco
     */
    public void setDst(int destino) {
        this.dst = destino;
    }

    /**
     * Retorna la representacion en String del Arc
     * pre: {@code true;}
     * post: el {@code String} de resultado es la representacion en String de
     * {@code this Arc}, con el formato {@code (src, dst)}
     *
     * @return la representacion en String de este Arc
     */
    @Override
    public String toString() {
       return "(" + src + ", "+ dst+")";
    }
}