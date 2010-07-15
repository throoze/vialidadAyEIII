
import java.util.Iterator;

/**
 * Clase encargada de hacer las llamadas recursivas al algoritmo de Tarjan. Es
 * útil para pasar las variables a las llamadas recursivas y que se conserven
 * los cambios
 * @author Victor De Ponte, 05-38087
 */
public class Tarjan {


    // "Variables globales" necesarias para la ejecución del algoritmo de Tarjan

    /**
     * Indica el indice de cada nodo
     */
    public int[]                indice;

    /**
     * Indica el lowLink de cada nodo
     */
    public int[]                lowLink;
    public Stack<Integer>       stack;
    public List<Integer>        compFuertCon;
    public DiGraph              digrafo;
    public List<List<Integer>>  compsFuertCon;
    public int                  index;

    /**
     * dice si un nodo esta empilado o no, para una consulta rápida y eficiente
     */
    public boolean[]            empilado;

    // CONSTRUCTOR:

    /**
     * Crea una nueva instancia de la clase Tarjan
     * pre: true;
     * post: Se inicializan todos los campos de la clase
     * @param digrafo
     */
    public Tarjan(DiGraph digrafo) {
        this.digrafo = digrafo;
        this.empilado = new boolean[digrafo.numNodes];
        this.indice = new int[digrafo.numNodes];
        this.lowLink = new int[digrafo.numNodes];
        this.index = 0;
        this.compsFuertCon = new Lista();

        for (int i = 0; i < indice.length; i++) {
            this.indice[i] = -1;
            this.lowLink[i] = -1;
            this.empilado[i] = false;
        }

        this.stack = new Stack();

        this.compFuertCon = new Lista();
    }

    // MÉTODOS:

    /**
     * Hace la llamada recursiva al algoritmo de tarjan para cada nodo no
     * visitado
     * pre: true
     * post: Se llama recursivamente a tarjan para cada nodo y se devuelve una
     * lista de las componentes fuertemente conexas de this.digrafo
     * @return una Lista de las componentes fuertemente conexas de this.digrafo
     */
    public List<List<Integer>> ejecutar() {
        for (int v = 0; v < this.digrafo.numNodes; v++) {
            if (this.indice[v] == -1) {
                this.tarjan(v);
            }
        }
        return this.compsFuertCon;
    }

    /**
     * Va almacenando cada una de las componentes fuertemente conexas de manera
     * recursiva.
     * pre: v debe pretenecer a this.digrafo
     * post: recursivamente se van almacenando las componentes fuertemente
     * conexas
     * @param v Nodo para el cual se va a calcular la recursion
     */
    private void tarjan(int v) {
        this.indice[v] = this.index;
        this.lowLink[v] = this.index;
        this.index++;

        this.stack.push(new Integer(v));
        this.empilado[v] = true;

        List<Integer> suces = this.digrafo.getSucesors(v);
        int[] suc = Tarjan.generarArrEnteros(suces);

        for (int i = 0; i < suc.length; i++) {
            if (this.indice[suc[i]] == -1) {
                this.tarjan(suc[i]);
                this.lowLink[v] = (this.lowLink[v] <= this.lowLink[suc[i]] ?
                                                          this.lowLink[v]  :
                                                          this.lowLink[suc[i]]);
            } else if (this.empilado[suc[i]]) {
                this.lowLink[v] = (this.lowLink[v] <= this.indice[suc[i]] ?
                                                           this.lowLink[v] :
                                                           this.indice[suc[i]]);
            }
        }


        if (this.lowLink[v] == this.indice[v]) {
            Integer w = null;
            this.compFuertCon = new Lista();
            do {
                w = (Integer)this.stack.pop();
                this.empilado[w.intValue()] = false;
                this.compFuertCon.add(w);
            } while (w.intValue() != v);
            this.compsFuertCon.add(this.compFuertCon);
        }
    }

    /**
     * Función auxiliar que genera un arreglo de int a partir de una lista de
     * Integer.
     * pre: true
     * post: Se devuelve un arreglo de int, con los valores enteros contenidos
     * en la lista {@code lista}
     * @param lista Lista de Integer a transformar
     * @return arreglo de int con los mismos elementos de lista.
     */
    public static int[] generarArrEnteros(List<Integer> lista) {
        int[] arr = new int[lista.size()];
        Iterator iter = lista.iterator();

        for (int i = 0; i < arr.length; i++) {
            int x = ((Integer)iter.next()).intValue();
            arr[i] = x;
        }

        return arr;
    }
}