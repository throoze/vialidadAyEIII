import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * DiGraphList es una clase concreta que representa un digrafo utilizando la
 * estructura Lista.
 * Los arcos son almacenados como una lista y son almacenados en un
 * arreglo donde la posicion i representa el nodo en el que inciden.
 *
 * @author Les profs
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 * @version 2.0
 * @since 1.6
 */
public class DiGraphList extends DiGraph {

    // Modelo de representación:
    
    // Arreglo de lista de los arcos, inArc[i] contine la lista
    // de los arcos que cuyo destino es el nodo i
    private List<Arc> inArcs[];
    
    // Arreglo de lista de los arcos, outArc[i] contine la lista
    // de los arcos que cuyo fuente es el nodo i
    private List<Arc> outArcs[];

    // Constructores:

    /**
     * Construye un nuevo DiGraphList vacio
     * <b>Pre</b>: true
     * <b>Post</b>: Se construyó un nuevo DiGraphList vacio
     */
    public DiGraphList() {
        this.inArcs = new List[0];
        this.outArcs = new List[0];
        this.numArcs = 0;
        this.numNodes = 0;
    }

    /**
     * Crea un DiGraphList con n nodos y sin arcos.
     * <b>Pre</b>: {@code n} &lt; {@code 0}
     * <b>Post</b>: este DiGraphList tiene {@code n} nodos y ningún arco.
     * @param n el número de nodos con los que se inicializa este DiGraphList.
     */
    public DiGraphList(int n) {
        this.inArcs = new List[n];
        this.outArcs = new List[n];
        for (int i = 0; i < n; i++) {
            this.inArcs[i] = new Lista();
            this.outArcs[i] = new Lista();
        }
        this.numNodes = n;
        this.numArcs = 0;
    }

    /**
     * Crea un DiGraphList a partir del contenido del archivo.
     * <blockquote>
     * <p><b>Sintaxis</b>:</p>
     * <p>numNodos numArcos</p>
     * <p>nodoSrc nodoDst</p>
     * <p>nodoSrc nodoDst</p>
     * <p>nodoSrc nodoDst</p>
     * <p>   .       .   </p>
     * <p>   .       .   </p>
     * <p>   .       .   </p>
     * <p>nodoSrc nodoDst</p>
     * </blockquote>
     * <b>Pre</b>: {@code fileName} debe existir, ser un archivo, poder leerse,
     * no puede tener errores de formato ni inconsistencias en el número de
     * nodos o arcos.
     * <b>Post</b>: Este DiGraphList se inicializa exitosamente con el DiGraph
     * representado en el archivo {@code fileName}.
     * @param fileName Nombre del archivo a leer
     * @throws IOException En caso de que {@code fileName} no exista, no sea un
     * archivo, no se pueda leer, tenga un error de formato, o alguna
     * inconsistencia en cuanto al numero de arcos o el numero de nodos
     */
    public DiGraphList(String fileName) throws IOException {
        this();
        this.read(fileName);
    }

    /**
     * Crea un DiGraphList a partir del DiGraph g
     * <b>Pre</b>: {@code true;}
     * <b>Post</b>: {@code this.equals(g)}
     *
     * @param g el grafo fuente.
     */
    public DiGraphList(DiGraph g) {
        this();
        this.inArcs = new List[g.numNodes];
        this.outArcs = new List[g.numNodes];
        for (int i = 0; i < g.numNodes; i++) {
            this.inArcs[i] = new Lista();
            this.outArcs[i] = new Lista();
        }
        this.numNodes = g.numNodes;
        this.numArcs = 0;
        for (int i = 0; i < g.numNodes; i++) {
            for  (int j = 0; j < g.numNodes; j++) {
                if (g.isArc(i, j)) {
                    this.addArc(i, j);
                }
            }
        }
    }

    /**
     * Agrega un arco a este DiGraphArcMatrix
     * <b>Pre</b>: {@code arco} no debe pertenecer a {@code this} y los nodos de
     * {@code arco} deben estar en {@code this}; {@code arco} no debe ser vacio
     * <b>Post</b>: El DiGraphArcMatrix contendra el arco {@code arco}
     *
     * @param arco El arco a agregar
     * @return El arco agregado, o null en caso de que los nodos src y dst no se
     * encuentren en el DiGraphArcMatrix
     *
     */
    public Arc addArc(Arc arco) {
        int src = arco.getSrc();
        int dst = arco.getDst();
        if ((0 <= src && src < this.numNodes) &&
            (0 <= dst && dst < this.numNodes)) {
            if (!this.isArc(arco)) {
                this.inArcs[dst].add(arco);
                this.outArcs[src].add(arco);
                this.numArcs++;
                return (arco);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Agrega un arco a este DiGraphList.
     * <b>Pre</b>: Los nodos src y dst deben encontrase en el DigraphList y no
     * debe existir un arco entre ellos.
     * <b>Post</b>: El DigraphList contendra un nuevo arco que tendra a src y
     * dst como nodos fuente y destino respectivamente.
     *
     * @param src nodo fuente del arco
     * @param dst nodo destino del arco
     * @return El arco agregado y null en caso de que los nodos src y dst no se
     * encuentren en el DiGraphList.
     *
     */
    public Arc addArc(int src, int dst) {
        if ((0 <= src && src < this.numNodes) &&
            (0 <= dst && dst < this.numNodes)) {
            if (!(this.isArc(src, dst))) {
                Arc nuevo = new Arc(src, dst);
                this.inArcs[dst].add(nuevo);
                this.outArcs[src].add(nuevo);
                this.numArcs++;
                return (nuevo);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Agrega un arco a este DiGraphList
     * <b>Pre</b>: Los nodos src y dst deben encontrase en el DigraphList y no
     * debe existir un arco entre ellos.
     * <b>Post</b>: El DigraphList contendra un nuevo arco que tendra a src y
     * dst como nodos fuente y destino respectivamente y el costo {@code costo}.
     *
     * @param src nodo fuente del arco
     * @param dst nodo destino del arco
     * @param costo costo del arco
     * @return El arco agregado y null en caso de que los nodos src y dst no se
     * encuentren en el DiGraphList.
     *
     */
    public Arc addArc(int src, int dst, double costo) {
        if ((0 <= src && src < this.numNodes) &&
            (0 <= dst && dst < this.numNodes)) {
            if (!(this.isArc(src, dst))) {
                Arc nuevo = new Arc(src, dst, costo);
                this.inArcs[dst].add(nuevo);
                this.outArcs[src].add(nuevo);
                this.numArcs++;
                return (nuevo);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Agrega un arco a este DiGraph
     * <b>Pre</b>: true
     * <b>Post</b>: etse arco va de {@code src} a {@code dst} y su identificador
     * es {@code ident}
     *
     * @param src nodo fuente del arco
     * @param dst nodo destino del arco
     * @param  ident identificador del arco
     * @return El arco agregado
     */
    @Override
    public Arc addArc(int src, int dst, String ident) {
        if ((0 <= src && src < this.numNodes) &&
            (0 <= dst && dst < this.numNodes)) {
            if (!(this.isArc(src, dst))) {
                Arc nuevo = new Arc(src, dst, ident);
                this.inArcs[dst].add(nuevo);
                this.outArcs[src].add(nuevo);
                this.numArcs++;
                return (nuevo);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Agrega un arco a este DiGraph
     * <b>Pre</b>: true
     * <b>Post</b>: etse arco va de {@code src} a {@code dst}, su costo es
     * {@code costo} y su identificador es {@code ident}
     *
     * @param src nodo fuente del arco
     * @param dst nodo destino del arco
     * @param costo costo del arco
     * @param  ident identificador del arco
     * @return El arco agregado
     */
    @Override
    public Arc addArc(int src, int dst, double costo, String ident) {
        if ((0 <= src && src < this.numNodes) &&
            (0 <= dst && dst < this.numNodes)) {
            if (!(this.isArc(src, dst))) {
                Arc nuevo = new Arc(src, dst, costo, ident);
                this.inArcs[dst].add(nuevo);
                this.outArcs[src].add(nuevo);
                this.numArcs++;
                return (nuevo);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Permite agregar <i>num</i> nuevos nodos a este DiGraphList.
     * <b>Pre</b>: Debe existir un DigraphList.
     * <b>Post</b>: El DigraphList contendrá <i>num</i> nodos nuevos.
     *
     * @param num numero de nodos a agregar
     */
    public void addNodes(int num) {
        if (0 < num) {
            List<Arc>[] arcosDeEntrada = new List[this.numNodes + num];
            List<Arc>[] arcosDeSalida = new List[this.numNodes + num];
            for (int k = 0; k < this.numNodes; k++) {
                arcosDeEntrada[k] = this.inArcs[k];
                arcosDeSalida[k] = this.outArcs[k];
            }
            for (int k = this.numNodes; k < this.numNodes + num; k++) {
                arcosDeEntrada[k] = new Lista();
                arcosDeSalida[k] = new Lista();
            }
            this.numNodes = this.numNodes + num;
            this.inArcs = (List<Arc>[])arcosDeEntrada;
            this.outArcs = (List<Arc>[])arcosDeSalida;
        }
    }

    /**
     * Retorna un Digraph que es la clausura transitiva de este DiGraph
     * calculada usando un algoritmo analogo al de Roy-Warshal.
     * <b>Pre</b>: Debe existir un DigraphList.
     * <b>Post</b>: Se obtendra un Digraph relacionado con la lista de arcos
     * adyacentes del DigraphList, calculada usando un algoritmo analogo al
     * de Roy -Warshal
     *
     * @return un Digraph que es la clausura transitiva de este DiGraph
     * calculada usando un algoritmo analogo al de Roy-Warshal
     */
    @Override
    public DiGraph alcance() {
        DiGraphList salida = this.clone();
        for (int i = 0; i < salida.numNodes; i++) {
            salida.addArc(i, i);
        }
        int flag;
        do {
            boolean stop = false;
            flag = salida.numArcs;
            for (int a = 0; a < salida.numNodes && !stop; a++) {
                for (int i = 0; i < salida.outArcs[a].size() && !stop; i++) {
                    int b = salida.outArcs[a].get(i).getDst();
                    for (int j = 0; j < salida.outArcs[b].size() && !stop; j++){
                        int c = salida.outArcs[b].get(j).getDst();
                        if (!salida.isArc(a, c)) {
                            salida.addArc(a, c);
                            stop = true;
                        }
                    }
                }
            }
        } while (salida.numArcs > flag);
        return salida;
    }

    /**
     * Genera una copia de este DiGraphList.
     * <b>Pre</b>: Debe existir un DiGraphList.
     * <b>Post</b>: El DiGraphList tendra una copia exacta.
     *
     * @return una copia de este DiGraphList.
     */
    @Override
    public DiGraphList clone() {
        DiGraphList nuevo = new DiGraphList(this);
        return nuevo;
    }

    /**
     * Elimina un arco de este DiGraphList.
     * <b>Pre</b>: Los nodos fuente y destino, es decir nodeIniId y nodeFinId
     * deben existir en el DigraphList.
     * <b>Post</b>: No existira arco entre los nodos nodeIniId y nodeFinId que
     * pertencen al DigraphList.
     *
     * @param nodeIniId nodo fuente del arco
     * @param nodeFinId nodo destino del arco
     * @return El arco eliminado y null en caso de que los nodos nodeIniId y
     * nodeFinId no se encuentren en el DiGraphList.
     */
    public Arc delArc(int nodeIniId, int nodeFinId) {
        if ((0 <= nodeIniId && nodeIniId < this.numNodes) &&
            (0 <= nodeFinId && nodeFinId < this.numNodes)) {
            if (this.isArc(nodeIniId, nodeFinId)) {
                Arc arco = new Arc(nodeIniId, nodeFinId);
                this.inArcs[nodeFinId].remove(arco);
                this.outArcs[nodeIniId].remove(arco);
                this.numArcs--;
                return arco;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Determina si el DiGraph g es igual a este DiGraphList.
     * <b>Pre</b>: debe existir un DigraphList y un Digraph g.
     * <b>Post</b>: Se obtendra true en caso de que los grafos relacionados sean
     * iguales y false en caso contrario.
     *
     * @param g el grafo con el que se quiere comparar
     * @return true si los dos DiGraph contienen los mismos nodos y los mismos
     * arcos, return false en caso contrario.
     */
    public boolean equals(DiGraph g) {
        if (this.numArcs == g.numArcs && this.numNodes == g.numNodes) {
            boolean out = true;
            for (int i = 0; i < this.numNodes && out; i++) {
                Object[] arrArcs = this.outArcs[i].toArray();
                for (int j = 0; j < arrArcs.length; j++) {
                    out = g.isArc(i, ((Arc)arrArcs[j]).getDst());
                }
            }
            return out;
        } else {
            return false;
        }
    }

    /**
     * Busca el Arco cuyo nodo fuente es nodoSrc y nodo destino es nodoDst.
     * <b>Pre</b>: Los nodos nodoSrc y nodoDst deben pertenecer al DigraphList
     * y debe existir un arco entre ellos.
     * <b>Post</b>: Se obtendra, en caso de que exista, el arco cuyos nodos
     * fuente y destino son nodoSrc y nodoDst respectivamente.
     *
     * @param nodoSrc nodo fuente
     * @param nodoDst nodo destino
     *
     * @return el Arco cuyo nodo fuente es nodoSrc y nodo destino es nodoDst.
     */
    public Arc getArc(int nodoSrc, int nodoDst) {
        if (this.isArc(nodoSrc, nodoDst)) {
            return (new Arc(nodoSrc,nodoDst));
        } else {
            return null;
        }
    }

    /**
     * Retorna el grado de un nodo en este DiGraphList.
     * <b>Pre</b>: El nodo nodeId debe pertencer al DigraphList.
     * <b>Post</b>: Se obtendra el numero de arcos que llegan y salen de nodeId,
     * es decir el grado.
     *
     * @param nodeId identificacion del nodo
     * @return el grado del nodo nodeId en este Grafo
     */
    public int getDegree(int nodeId) {
        return this.getInDegree(nodeId) + this.getOutDegree(nodeId);
    }

    /**
     * Retorna el grado interno de un nodo en este DiGraphList.
     * <b>Pre</b>: El nodo nodeId debe pertenecer al DigraphList.
     * <b>Post</b>: Se obtendra el numero de arcos que llegan a NodeId, es decir
     * el grado interno.
     *
     * @param nodeId identificacion del nodo
     * @return el grado interno del nodo nodeId en este Grafo.
     */
    public int getInDegree(int nodeId) {
        return this.inArcs[nodeId].size();
    }

    /**
     * Retorna la lista de arcos que tienen a nodeId como destino.
     * <b>Pre</b>: El nodoId debe pertencer al DigraphList.
     * <b>Post</b>: Se obtendra la lista de arcos que tienen a nodeId como nodo
     * final.
     *
     * @param nodeId identificador del nodo
     * @return la lista de arcos que tienen a nodeId como destino.
     */
    public List<Arc> getInEdges(int nodeId) {
        return this.inArcs[nodeId];
    }

    /**
     * Retorna el numero de arcos en el DigraphList.
     * <b>Pre</b>: Debe existir un DigraphList.
     * <b>Post</b>: Se obtendra el numero de arcos que pertencen al
     * DigraphList.
     *
     * @return numero de arcos que hay en el DigraphList.
     */
    public int getNumberOfArcs() {
        return this.numArcs;
    }

    /**
     * Retorna el numero de nodos que hay en el DigraphList.
     * <b>Pre</b>: Debe existir un DigraphList.
     * <b>Post</b>: Se obtendra el numero de nodos que pertencen al
     * DigraphList.
     *
     * @return numero de nodos en el grafo
     */
    public int getNumberOfNodes() {
        return this.numNodes;
    }

    /**
     * Retorna el grado externo de un nodo en este DiGraphList.
     * <b>Pre</b>: El nodo nodeId debe pertenecer al DigraphList.
     * <b>Post</b>: Se obtendra el numero de arcos que salen de nodeId, es decir
     * el grado externo.
     *
     * @param nodeId identificacion del nodo
     * @return el grado externo del nodo nodeId en este Grafo
     */
    public int getOutDegree(int nodeId) {
        return this.outArcs[nodeId].size();
    }

    /**
     * Retorna la lista de arcos que tienen a nodeId como fuente
     * <b>Pre</b>: El nodo nodeId debe pertenecer al DigraphList.
     * <b>Post</b>:Se obtendra la lista de arcos que tienen a nodeId como nodo
     * inicial.
     *
     * @param nodeId identificador del nodo
     * @return la lista de arcos que tienen a nodeId como fuente
     */
    public List<Arc> getOutEdges(int nodeId) {
        return this.outArcs[nodeId];
    }

    /**
     * Retorna la lista de predecesores del nodo nodeId
     * <b>Pre</b>: El nodo nodeId debe pertenecer al DigraphList.
     * <b>Post</b>: Se obtendra la lista de nodos que tienen a nodeId como nodo
     * de destino.
     *
     * @param nodeId el id del nodo del que se quieren los predecesores
     * @return lista de predecesores de nodeId
     */
    public List<Integer> getPredecesors(int nodeId) {
        List<Integer> predecesors = new Lista();
        Object[] arrArcs = this.inArcs[nodeId].toArray();
        for (int k = 0; k < arrArcs.length; k++) {
            Arc arco = (Arc)arrArcs[k];
            predecesors.add(new Integer(arco.getSrc()));
        }
        return predecesors;
    }

    /**
     * Retorna la lista de sucesores del nodo nodeId
     * <b>Pre</b>: El nodoId debe pertenecer al DigraphList.
     * <b>Post</b>: Se obtendra la lista de nodos que tienen a nodeId como nodo
     * fuente.
     *
     * @param nodeId el id del nodo del que se quieren los sucesores
     * @return lista de sucesores de nodeId
     */
    public List<Integer> getSucesors(int nodeId) {
        List<Integer> sucesors = new Lista();
        Object[] arrArcs = this.outArcs[nodeId].toArray();
        for (int k = 0; k < arrArcs.length; k++) {
            Arc arco = (Arc)arrArcs[k];
            sucesors.add(new Integer(arco.getDst()));
        }
        return sucesors;
    }

    /**
     * Indica si un arco existe en este DiGraphArcMatrix.
     * <b>Pre</b>: Los nodos de {@code arco} deben pertenecer a {@code this]
     * <b>Post</b>: Se obtendra true en caso de que el arco exista y false si
     * ocurre lo contrario.
     *
     * @param src el id del nodo origen del arco
     * @param dst el id del nodo destino del arco
     * @return true si exite un arco desde el nodo src hasta el nodo dst.
     * false en caso contrario
     */
    public boolean isArc(Arc arco) {
        boolean es = false;
        int src = arco.getSrc();
        int dst = arco.getDst();
        es = this.isArc(src, dst);
        return es;
    }

    /**
     * Indica si un arco existe en este DiGraphList.
     * <b>Pre</b>: Los nodos src y dst deben pertenecer al DigraphList.
     * <b>Post</b>: Se obtendra true en caso de que el arco exista y false si
     * ocurre lo contrario.
     *
     * @param src el id del nodo origen del arco
     * @param dst el id del nodo destino del arco
     * @return true si exite un arco desde el nodo src hasta el nodo dst.
     * false en caso contrario
     */
    @Override
    public boolean isArc(int src, int dst) {
        boolean es = false;
        if ((0 <= src && src < this.numNodes) &&
            (0 <= dst && dst < this.numNodes)) {
            es = (this.outArcs[src].contains(new Arc(src,dst)) &&
                  this.inArcs[dst].contains(new Arc(src,dst)));
        }
        return es;
    }

    /**
     * Inicializa este DiGraphList en el DiGraph representado en el contenido
     * del archivo {@code fileName}.
     * <blockquote>
     * <p><b>Sintaxis</b>:</p>
     * <p>numNodos numArcos</p>
     * <p>nodoSrc nodoDst</p>
     * <p>nodoSrc nodoDst</p>
     * <p>nodoSrc nodoDst</p>
     * <p>   .       .   </p>
     * <p>   .       .   </p>
     * <p>   .       .   </p>
     * <p>nodoSrc nodoDst</p>
     * </blockquote>
     * <b>Pre</b>: {@code fileName} debe existir, ser un archivo, poder leerse,
     * no puede tener errores de formato ni inconsistencias en el número de
     * nodos o arcos.
     * <b>Post</b>: Este DiGraphList se inicializa exitosamente con el DiGraph
     * representado en el archivo {@code fileName}.
     *
     * @param fileName Nombre del archivo a leer
     * @throws IOException En caso de que {@code fileName} no exista, no sea un
     * archivo, no se pueda leer, tenga un error de formato, o alguna
     * inconsistencia en cuanto al numero de arcos o el numero de nodos
     */
    public void read(String fileName) throws IOException {
        if ((new File(fileName)).exists() &&
            (new File(fileName)).isFile() &&
            (new File(fileName)).canRead())  {
            BufferedReader inbuff = null;
            try {
                inbuff = new BufferedReader(new FileReader(fileName));
            } catch (FileNotFoundException fnfe) {
                System.out.println("Esto no deberia pasar, contacte" +
                        " al programador...");
                System.out.println("MENSAJE:" + fnfe.getMessage() + "\n" +
                        "CAUSA:" + fnfe.getCause().toString() + "\n");
                throw new ExcepcionArchivoNoSePuedeLeer("Problema Leyendo el" +
                        " archivo \"" + fileName +
                        "\" al momento de crear el buffer lector...\n");
            }
            String linea = null;
            try {
                 linea = inbuff.readLine();
            } catch (IOException ioe) {
                System.out.println("Esto no deberia pasar, contacte" +
                        " al programador...");
                System.out.println("MENSAJE:" + ioe.getMessage() + "\n" +
                        "CAUSA:" + ioe.getCause().toString() + "\n");
                throw new ExcepcionArchivoNoSePuedeLeer("Problema Leyendo la" +
                        "primera linea del archivo \"" + fileName +
                        "\"");
            }
            String[] tokens = linea.split(" ");
            if (tokens.length == 2) {
                if (tokens[0].matches("[0-9]+?") &&
                    tokens[1].matches("[0-9]+?")) {
                    /* Aqui empiezan las diferencias en este constructor entre
                     * DiGraphList y DiGraphMatrix
                     */
                    this.inArcs = new List[new Integer(tokens[0]).intValue()];
                    this.outArcs = new List[new Integer(tokens[0]).intValue()];
                    for (int k = 0; k < this.inArcs.length; k++) {
                        this.inArcs[k] = new Lista();
                        this.outArcs[k] = new Lista();
                    }
                    /* Fin de las diferencias en este constructor entre
                     * DiGraphList y DiGraphMatrix
                     */
                    this.numNodes = new Integer(tokens[0]).intValue();
                    int nArcos = new Integer(tokens[1]).intValue();
                    this.fillFromFile(inbuff, fileName, nArcos);
                } else {
                    throw new ExcepcionFormatoIncorrecto("En la primera linea" +
                            " hay un error de sintaxis: Se esperaba un numero" +
                            " seguido de otro numero (numNodos numArcos) y se" +
                            " encontro: " + tokens[0] + " " + tokens[1] + "\n");
                }
            } else {
                throw new ExcepcionFormatoIncorrecto("En la primera linea hay" +
                        "un error de sintaxis: Se esperaban dos elementos (" +
                        "numNodos numArcos), y se encontro:\n\t"+
                        tokens.toString());
            }
        } else if (!(new File(fileName)).exists()) {
            throw new ExcepcionArchivoNoExiste("Problema al leer el archivo " +
                    "\"" + fileName +"\": EL ARCHIVO NO EXISTE!!!");
        } else if (!(new File(fileName)).isFile()) {
            throw new ExcepcionNoEsArchivo("Problema al leer el archivo \"" +
                    fileName +"\": NO ES UN ARCHIVO!!!");
        } else if (!(new File(fileName)).canRead()) {
            throw new ExcepcionArchivoNoSePuedeLeer("Problema al leer el ar" +
                    "chivo \"" + fileName +"\": ESTE ARCHIVO NO SE PUEDE" +
                    " LEER!!!");
        }
    }

    /**
     * Remueve todos los arcos de este grafo
     * <b>Pre</b>: Debe existir un DigraphList, y sus nodos deben estar
     * conectados mediante arcos.
     * <b>Post</b>: Se obtendra la lista de los arcos que fueron eliminados.
     *
     * @return lista de arcos eliminados
     */
    public List<Arc> removeAllArcs() {
        List<Arc> lista = new Lista();
        for (int i = 0; i < this.numNodes; i++) {
            Object[] arrArcs = this.outArcs[i].toArray();
            for (int j = 0; j < arrArcs.length; j++) {
                lista.add((Arc)arrArcs[j]);
            }
        }
        this.inArcs = new List[this.numNodes];
        this.outArcs = new List[this.numNodes];
        return lista;
    }

    /**
     * Invierte la direccion de un arco
     * <b>Pre</b>: Los nodos nodeIniId y nodeFinId deben pertenecer al
     * DigraphList, y seran el nodo fuente y destino respectivamente en caso
     * de que exista un arco entre ellos.
     * <b>Post</b>: Se obtendra true en caso de que el arco haya sido invertido
     * y false en caso contrario.
     *
     * @param nodeIniId nodo fuente del arco antes de invertirlo
     * @param nodeFinId nodo destino del arco antes de invertirlo
     * @return true si el arco fue invertido, false en caso contrario
     */
    public boolean reverseArc(int nodeIniId, int nodeFinId) {
        if ((0 <= nodeIniId && nodeIniId < this.numNodes) &&
            (0 <= nodeFinId && nodeFinId < this.numNodes)) {
            if (this.isArc(nodeIniId, nodeFinId)) {
                if (!this.isArc(nodeFinId, nodeIniId)) {
                    this.delArc(nodeIniId, nodeFinId);
                    this.addArc(nodeFinId, nodeIniId);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Invierte todos los arcos del DiGraphList.
     * <b>Pre</b>: Debe existir un DiGraphList.
     * <b>Post</b>: Se obtiene true en caso de que se hayan invertido todos los
     * arcos y false en caso contrario.
     *
     * @return true si todos los arcos fueron invertidos, false en caso
     * contrario. En caso de que algun nodo no puede ser invertido, el grafo
     * debe quedar sin alteraciones.
     */
    public boolean reverseArcs() {
        DiGraphList aux = new DiGraphList(this.numNodes);
        for (int i = 0; i < this.numNodes; i++) {
            for (int j = 0; j < this.numNodes; j++) {
                if (this.isArc(i, j)) {
                    aux.addArc(j, i);
                }
            }
        }
        this.inArcs = aux.inArcs;
        this.outArcs = aux.outArcs;
        return true;
    }

    /**
     * Retorna la representacion en String de este DiGraphList.
     * <b>Pre</b>: Debe existir un DigraphList.
     * <b>Post</b>: Se obtendra la representacion en String del DigraphList.
     *
     * @return la representacion en String de este DiGraphList.
     */
    @Override
    public String toString() {
        String string = this.numNodes + " " + this.numArcs;
        for (int i = 0; i < this.numNodes; i++) {
            for (int j = 0; j < this.outArcs[i].size(); j++) {
                string += "\n" + this.outArcs[i].get(j).getSrc() + " " +
                        this.outArcs[i].get(j).getDst();
            }
        }
        return string;
    }

    /**
     * Escribe la representacion de este DiGraph en el archivo {@code fileName},
     * usando el formato siguiente:
     * <blockquote>
     * <p><b>Sintaxis</b>:</p>
     * <p>numNodos numArcos</p>
     * <p>nodoSrc nodoDst</p>
     * <p>nodoSrc nodoDst</p>
     * <p>nodoSrc nodoDst</p>
     * <p>&nbsp;.&nbsp;.</p>
     * <p>&nbsp;.&nbsp;.</p>
     * <p>&nbsp;.&nbsp;.</p>
     * <p>nodoSrc nodoDst</p>
     * </blockquote>
     * <b>pre</b>: {@code fileName} debe existir, ser un archivo, y poder
     * escribirse.
     * <b>post</b>: El archivo {@code fileName} contiene la representación de
     * este DiGraph.
     * @param fileName Archivo a escribir
     * @throws IOException En caso de que el archivo {@code fileName} no exista,
     * no sea un archivo o no se pueda escribir en el.
     */
    public void write(String fileName) throws IOException {
        File salida = new File(fileName);
        if (!salida.exists() || !salida.isFile() || !salida.canWrite())
        {
            salida.delete();
            salida.createNewFile();
        }
        salida.setWritable(true);
        PrintStream out;
        try {
            out = new PrintStream(salida);
            out.println(this.numNodes + " " + this.numArcs);
            for (int i = 0; i < this.numNodes; i++) {
                /* A partir de aqui es diferente entre DiGraphList y
                 * DiGraphMatrix
                 */
                Object[] arrArcs = this.outArcs[i].toArray();
                for (int j = 0; j < arrArcs.length; j++) {
                    out.println(i + " " + ((Arc)arrArcs[j]).getDst());
                }
                // Fin de las diferencias
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Esto no deberia pasar, contacte" +
                    " al programador...");
            System.out.println("MENSAJE:" + fnfe.getMessage() + "\n" +
                    "CAUSA:" + fnfe.getCause().toString() + "\n");
            throw new ExcepcionArchivoNoSePuedeEscribir("\nProblema" +
                    " escribiendo en el archivo \"" + fileName + "\"");
        }
    }

    // METODOS PRIVADOR AUXILIARES:

    /**
     * Método auxiliar para llenar este DiGraph leyendo desde el archivo de
     * nombre {@code fileName}.
     * <b>Pre</b>: {@code fileName} debe existir, ser un archivo, poder leerse,
     * no puede tener errores de formato ni inconsistencias en el número de
     * nodos o arcos. {@code inbuff} debe estar abierto y haberse leido solo la
     * primera linea. {@code nArc} debe ser el numero de arcos leido en la
     * primera linea de {@code fileName}.
     * <b>Post</b>: Este DiGraphList se inicializa exitosamente con el DiGraph
     * representado en el archivo {@code fileName}.
     * @param inBuff Buffer de lectura a travez del cual se lee {@code fileName}
     * @param fileName Nombre del archivo a leer
     * @param nArc Número de arcos que tiene este DiGraph.
     * @throws ExcepcionArchivoNoSePuedeLeer En caso de que {@code fileName} no
     * se pueda leer
     * @throws ExcepcionFormatoIncorrecto En caso de que el formato especificado
     * no se cumpla
     * @throws ExcepcionArcoRepetido En caso de que haya un arco repetido
     * @throws ExcepcionInconsistenciaNumeroDeNodos En caso de que se consiga un
     * nodo que no pertenezca a este DiGraph
     * @throws ExcepcionInconsistenciaNumeroDeArcos en caso de que haya más o
     * menos arcos que los indicados en la primera linea ({@code nArc})
     */
    private void fillFromFile(BufferedReader inBuff, String fileName, int nArc)
                                throws ExcepcionArchivoNoSePuedeLeer,
                                       ExcepcionFormatoIncorrecto,
                                       ExcepcionArcoRepetido,
                                       ExcepcionInconsistenciaNumeroDeNodos,
                                       ExcepcionInconsistenciaNumeroDeArcos
    {
        String linea = "";
        String[] tokens;
        int k = 2;

        // Se lee la primera linea de la parte correspondiente a los arcos
        try {
            linea = inBuff.readLine();
        } catch (IOException ioe) {
            System.out.println("Esto no deberia pasar, contacte"
                    + " al programador...");
            throw new ExcepcionArchivoNoSePuedeLeer("\nProblema Leyendo la"
                        + "linea " + k + " del archivo \"" + fileName + "\"");
        }

        // Se sigue revisando el archivo hasta que se terminen las líneas
        while (linea != null) {
            tokens = linea.split(" ");

            // Se verifica que sólo tenga 2 elementos en la línea
            if (tokens.length == 2) {

                // Se chequea que sean numeros
                if (tokens[0].matches("[0-9]+?") &&
                    tokens[1].matches("[0-9]+?")) {
                    int src = (new Integer(tokens[0]).intValue());
                    int dst = (new Integer(tokens[1]).intValue());

                    /* Se chequea que estos números sean nodos que estén en este
                     * DiGraph
                     */
                    if ((src < 0 || this.numNodes <= src)) {
                        throw new ExcepcionInconsistenciaNumeroDeNodos("\nEl " +
                                "grafo de entrada tiene un numero de nodos " +
                                "distinto al indicado al principio del ar" +
                                "chivo:\nEncontrado \"" + tokens[0] + "\" en" +
                                " la linea " + k + ", y se esperaba en el" +
                                " intervalo [0 - " + nArc + "]");
                    } else if (dst < 0 || this.numNodes <= dst) {
                        throw new ExcepcionInconsistenciaNumeroDeNodos("\nEl " +
                                "grafo de entrada tiene un numero de nodos " +
                                "distinto al indicado al principio del ar" +
                                "chivo:\nEncontrado \"" + tokens[1] + "\" en" +
                                " la linea " + k + ", y se esperaba en el" +
                                " intervalo [0 - " + nArc + "]");
                    }

                    // Por último, se agrega el arco leído.
                    this.addArc(src,dst);
                } else {
                    throw new ExcepcionFormatoIncorrecto("\nEn la linea " + k +
                            " del archivo \"" + fileName + "\"" +
                            " hay un error de sintaxis:\nSe esperaba un numero"+
                            " seguido de otro numero (src dst) y se encontró" +
                            ": \n\t\"" + tokens[0] + " " + tokens[1] + "\"\n");
                }
            } else {
                throw new ExcepcionFormatoIncorrecto("\nEn la linea " + k +
                        " del archivo \"" + fileName + "\" hay" +
                        " un error de sintaxis:\nSe esperaban dos elementos" +
                        " (src dst), y se encontro:\n\t\"" + linea + "\"\n");
            }

            // Se incrementa un numero de líneas leídas
            k++;

            // Se lee una nueva línea
            try {
                linea = inBuff.readLine();
            } catch (IOException ioe) {
                System.out.println("Esto no deberia pasar, contacte"
                        + " al programador...");
                throw new ExcepcionArchivoNoSePuedeLeer("\nProblema Leyendo la"
                        + "linea " + k + " del archivo \"" + fileName + "\"");
            }
        }

        /* Se verifica que el archivo no tenga más líneas que las especificadas
         * en la primera línea del archivo.
         */
        if (linea == null && k-2 != nArc) {
            throw new ExcepcionInconsistenciaNumeroDeArcos("El grafo de entra" +
                    "da tiene menos arcos que los indicados al principio del " +
                    "archivo:\nTiene " + (k-2) + " arco(s), y se esperaba(n) " +
                    nArc + " arco(s)");
        }
    }
}