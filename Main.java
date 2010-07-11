import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;



/**
 * Clase Principal encargada de resolver el problema de vialidad eficiente
 * planteado en el proyecto 4 de la cátedra del Laboratorio de Algoritmos y
 * Estructuras III, de la Universidad Simón Bolívar.
 *
 * MODELO DE REPRESENTACIÓN:
 *
 * Representamos con la siguiente estructura de datos tanto la instancia
 * del caso de prueba en evaluación, como los datos que utilizamos para la reso-
 * lución del problema para esa instancia. Dado que el archivo de entrada puede
 * contener muchas instancias del problema, decidimos almacenar sólo una instan-
 * cia a la vez, para evitar sobrecargas de memoria. En cuanto al problema, lo
 * resolvemos de la manera sugerida por la cátedra: Construyendo cláusulas que
 * representen las condiciones necesarias para que el problema se solucione,
 * luego transformamos esas cláusulas en fórmulas lógicas en la forma 2CNF, en
 * base a éstas construimos un grafo de implicaciones y hallamos las componentes
 * fuertemente conexas de dicho grafo. El problema tiene solución en caso de que
 * no exista ningun "literal" de las fórmulas junto con su complemento (negado)
 * en una misma componente fuertemente conexa. De lo contrario, el problema no
 * tendrá solución.
 *
 * Para representar los literales hemos usado el conjunto de los números natura-
 * les; usando los números pares para representar los literales sin negación, y
 * los impares para representar los literales negados
 * 
 * Dado que en la manera en que nuestro Digraph representa sus nodos (usando los
 * números naturales comenzando en el cero), no podemos usar números negativos
 * para representar los complementos (negados) de nuestros literales.
 * 
 * Para resolver éste problema, al representar una instancia del problema con
 * 'c' calles y 'a' avenidas, usamos los primeros c enteros pares (empezando por
 * el cero) para representar los literales sin negacion de las calles, los pri-
 * meros c enteros impares para representar los literales negados de las calles,
 * los siguientes a enteros pares para representar los literales sin negación de
 * las avenidas y los a enteros impares (después de los de las calles) para re-
 * presentar los literales negados de las avenidas.
 *
 * Los literales correspondientes a las calles, sin negación, corresponden a la
 * orientación W-E; sus complementos corresponden a la orientacion E-W. Análoga-
 * mente, los literales correspondientes a las avenidas, sin negación, corres-
 * ponden a la orientación N-S, y sus complementos corresponden a la orientación
 * S-N, con:
 * 
 *                              N
 *
 *                              |
 *                              |
 *                      W ------|------ E
 *                              |
 *                              |
 *
 *                              S
 *
 * Ejemplo:
 *
 * Sea una instancia de caso de prueba en la que se tenga una grilla de 6 calles
 * y 7 avenidas y se quieren construir las cláusulas para ir del punto (2,2) al
 * (4,5).
 *
 * Luego, los literales de la orientación de las calles y avenidas serán repre-
 * sentados de la siguiente manera:
 *
 * 2da Calle con orientación W-E: 2
 * 4ta Calle con orientación W-E: 6
 *
 * 2da Avenida con orientación N-S: 14
 * 5ta Avenida con orientación N-S: 20
 *
 * Los respectivos complementos serán representados de la siguiente forma:
 *
 * 2da Calle con orientación E-W: 3
 * 4ta Calle con orientación E-W: 7
 *
 * 2da Avenida con orientación S-N: 15
 * 5ta Avenida con orientación S-N: 21
 * 
 * Luego, las cláusulas se almacenarán en arreglos de enteros correspondientes a
 * la representación de los literales usados:
 * 
 * la primera cláusula correspondiente al viaje entre estos dos puntos se alma-
 * nará en un arreglo de tamaño 4, como sigue:
 * 
 * clausula[0] == 2
 * clausula[1] == 21
 * clausula[2] == 15
 * clausula[3] == 6
 * 
 * Y esto representa la cláusula:
 * 
 *      ( c2 /\ !a5 ) \/ ( !a2 /\ c4 )
 * 
 * donde los operadores son:
 * 
 *  /\ : conjunción lógica
 * 
 *  \/ : disjunción lógica
 * 
 *  !  : negación lógica
 *
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 */
public class Main {


    /**
     * Nombre del archivo de entrada.
     */
    private String          inputFile; 

    /**
     * Nombre del archivo de salida.
     */
    private String          outputFile;

    /**
     * Buffer de entrada (Lectura).
     */
    private BufferedReader  in;

    /**
     * Flujo de salida (Escritura).
     */
    private PrintStream     out;

    /**
     * Numero de instancias del problema a resolver.
     */
    private int             nInstancias;
    
    /**
     * Numero de casos que se han estraido del archivo de entrada
     */
    private int             nCasosProbados;

    /**
     * Numero de calles del caso en uso.
     */
    private int             c;

    /**
     * Numero de avenidas del caso en uso.
     */
    private int             a;

    /**
     * Numero de pares de lugares del caso en uso.
     */
    private int             p;

    /**
     * Número en el que termina la representación de las calles y comienza la de
     * las avenidas
     */
    private int             offset;

    /**
     * Lista de pares de puntos entre los cuales se debe verificar si se puede
     * viajar de manera eficiente.
     */
    private List<int[]>     viajes;

    /**
     * Cláusulas iniciales que modelan el problema.
     */
    private List<int[]>     clausulas;

    /**
     * Cláusulas que modelan el problema en la forma 2CNF.
     */
    private List<int[]>     dosCNF;

    /**
     * Grafo de implicaciones que resolverá esta instancia del problema
     */
    private DiGraph         digrafo;

    /**
     * Número de nodos que tendrá el grafo de implicaciones, excluyendo nodos
     * dummy para el trato de los casos en los que se viaja sobre la misma calle
     * o la misma avenida.
     */
    private int             nNodos;

    /**
     * Cantidad de variables dummy que serán necesarias para tratar los casos en
     * los que se viaja sobre la misma calle o la misma avenida
     */
    private int             nDummies;

    // CONSTRUCTOR:

    /**
     *
     * @param inFile
     * @param outFile
     * @throws IOException
     */
    public Main (String inFile, String outFile) throws IOException {
        this.inputFile = inFile;
        this.outputFile = outFile;

        // Se crea un objeto de tipo archivo para hacer el código más legible
        File file =  new File(this.inputFile);

        // Se verifica que el archivo este en condiciones de ser procesado
        if (file.exists() && file.isFile() && file.canRead())  {
            
            // Se inicializan la entrada y la salida del programa
            try {
                in = new BufferedReader(new FileReader(this.inputFile));
                out = new PrintStream(this.outputFile);
            } catch (FileNotFoundException ex) {
                throw new ExcepcionArchivoNoExiste("Problema al leer el " +
                        "archivo \"" + this.inputFile +"\": EL ARCHIVO NO " +
                        " SE ENCUENTRA!!!");
            }

            /* Se lee la primera línea para saber cuántas instancias del proble-
             * ma se resolverán
             */
            String primera = this.in.readLine();
            try {
                this.nInstancias = Integer.parseInt(primera);
            } catch (NumberFormatException nfe) {
                throw new ExcepcionFormatoIncorrecto("\nProblema leyendo la " +
                        "primera linea:\nSe esperaba un entero y se encontró:"
                        + "\n\n\t\t" + primera );
            }
            this.nCasosProbados = 0;
        }
    }

    // MÉTODOS ESTÁTICOS:

    /**
     *
     * @param clausula
     * @return
     */
    private static int[][] distributiva(int[] clausula) {

        // Disjunciones a crear:

        int[] d1 = new int[2];
        int[] d2 = new int[2];
        int[] d3 = new int[2];
        int[] d4 = new int[2];

        /* Se efectua la distributividad sobre la representación de la fórmula
         * lógica.
         */

        d1[0] = clausula[0];
        d1[1] = clausula[2];
        d2[0] = clausula[0];
        d2[1] = clausula[3];
        d3[0] = clausula[1];
        d3[1] = clausula[2];
        d4[0] = clausula[1];
        d4[1] = clausula[3];

        // Se almacenan las disjunciones para su entrega:

        int[][] disjunciones = new int[2][4];

        disjunciones[0][0] = d1[0]; disjunciones[1][0] = d1[1];
        disjunciones[0][1] = d2[0]; disjunciones[1][1] = d2[1];
        disjunciones[0][2] = d3[0]; disjunciones[1][2] = d3[1];
        disjunciones[0][3] = d4[0]; disjunciones[1][3] = d4[1];


        return disjunciones;
    }

    // MÉTODOS NO-ESTÁTICOS:
    
    /**
     * 
     * @return
     */
    public boolean hasNext() {
        return (this.nCasosProbados < this.nInstancias);
    }

    /**
     *
     * @param ca1
     * @param av1
     * @param ca2
     * @param av2
     * @return
     */
    private int[] avenida1Menor(int ca1, int av1, int ca2, int av2) {

        int[] clausula = new int[4];

        if (ca1 < ca2) {
            // Primer posible camino
            clausula[0]= ca1; clausula[1]= av2;
            // Segundo posible camino
            clausula[2]= ca2; clausula[3]= av1;
        } else if (ca1 == ca2) {
            /* Sólo hay un camino, relleno el resto con el mismo "literal"
             * ya que: (!p \/ p) <==> (p ==> p) <==> true
             * Se agrega un nodo dummy al grafo fr implicaciones y se construye
             * la disjuncion con este nuevo literal dummy
             */

            // Único posible camino
            clausula[0]= ca1; clausula[1]= this.nNodos + (2*this.nDummies);
            // Único posible camino
            clausula[2]= ca2; clausula[3]= this.nNodos + (2*this.nDummies) + 1;

            this.nDummies++;
        } else {
            // Primer posible camino
            clausula[0]= ca1; clausula[1]= -av2;
            // Segundo posible camino
            clausula[2]= ca2; clausula[3]= -av1;
        }

        return clausula;
    }

    /**
     *
     * @param ca1
     * @param av1
     * @param ca2
     * @param av2
     * @return
     */
    private int[] avenida1Mayor(int ca1, int av1, int ca2, int av2) {

        int[] clausula = new int[4];

        if (ca1 < ca2) {
            // Primer posible camino
            clausula[0]= -ca1; clausula[1]= av2;
            // Segundo posible camino
            clausula[2]= -ca2; clausula[3]= av1;
        } else if (ca1 == ca2) {
            /* Sólo hay un camino, relleno el resto con el mismo "literal"
             * ya que: (!p \/ p) <==> (p ==> p) <==> true
             * Se agrega un nodo dummy al grafo fr implicaciones y se construye
             * la disjuncion con este nuevo literal dummy
             */

            // Único posible camino
            clausula[0]= -ca1; clausula[1]= this.nNodos + (2*this.nDummies);
            // Único posible camino
            clausula[2]= -ca2; clausula[3]= this.nNodos + (2*this.nDummies) + 1;

            this.nDummies++;
        } else {
            // Primer posible camino
            clausula[0]= -ca1; clausula[1]= -av2;
            // Segundo posible camino
            clausula[2]= -ca2; clausula[3]= -av1;
        }

        return clausula;
    }

    /**
     *
     * @param ca1
     * @param av1
     * @param ca2
     * @param av2
     * @return
     */
    private int[] avenida1Igual(int ca1, int av1, int ca2, int av2) {

        int[] clausula = new int[4];

        if (ca1 < ca2) {
            /* Sólo hay un camino, relleno el resto con el mismo "literal"
             * ya que: (!p \/ p) <==> (p ==> p) <==> true
             * Se agrega un nodo dummy al grafo fr implicaciones y se construye
             * la disjuncion con este nuevo literal dummy
             */

            // Único posible camino
            clausula[0]= av1; clausula[1]= this.nNodos + (2*this.nDummies);
            // Único posible camino
            clausula[2]= av2; clausula[3]= this.nNodos + (2*this.nDummies) + 1;

            this.nDummies++;
        } else if (ca2 < ca1) {
            /* Sólo hay un camino, relleno el resto con el mismo "literal"
             * ya que: (!p \/ p) <==> (p ==> p) <==> true
             * Se agrega un nodo dummy al grafo fr implicaciones y se construye
             * la disjuncion con este nuevo literal dummy
             */

            // Único posible camino
            clausula[0]= -av1; clausula[1]= this.nNodos + (2*this.nDummies);
            // Único posible camino
            clausula[2]= -av2; clausula[3]= this.nNodos + (2*this.nDummies) + 1;

            this.nDummies++;
        } else {
            /* No se trata el caso cual las calles son iguales, ya que se
             * trataría de el mismo punto y no habría nada que hacer. En este
             * caso, se retorna null.
             */
            return null;
        }

        return clausula;
    }

    /**
     *
     * @param pair
     * @return
     */
    private int[] construirClausula(int[] pair) {

        int[] clausula = null;

        int av1 = pair[0];
        int ca1 = pair[1];
        int av2 = pair[2];
        int ca2 = pair[3];

        /* Esto quiere decir que estamos yendo desde el punto (av1,ca1) hacia
         * el punto (av2,ca2).
         */

        if (av1 < av2) {
            clausula = this.avenida1Menor(ca1, av1, ca2, av2);
        } else if (av1 == av2) {
            clausula = this.avenida1Igual(ca1, av1, ca2, av2);
        } else {
            clausula = this.avenida1Mayor(ca1, av1, ca2, av2);
        }

        return clausula;
    }


    /**
     *
     * @param disj
     * @param digrafo
     */
    private void implicacionPoQ(int p, int q, DiGraph digrafo) {
        int noP = p + 1;
        int noQ = q + 1;
        
        if (!digrafo.isArc(noP, q)) {
            this.digrafo.addArc(noP, q);
        }
        if (!digrafo.isArc(noQ, p)) {
            this.digrafo.addArc(noQ, p);
        }
    }

    /**
     *
     * @param disj
     * @param digrafo
     */
    private void implicacionNoPoNoQ(int noP, int noQ, DiGraph digrafo) {
        int pe = noP - 1;
        int q = noQ - 1;

        if (!digrafo.isArc(pe, noQ)) {
            this.digrafo.addArc(pe, noQ);
        }
        if (!digrafo.isArc(q, noP)) {
            this.digrafo.addArc(q, noP);
        }
    }

    /**
     *
     * @param disj
     * @param digrafo
     */
    private void implicacionPoNoQ(int pe, int noQ, DiGraph digrafo) {
        int noP = pe + 1;
        int q = noQ - 1;

        if (!digrafo.isArc(noP, noQ)) {
            this.digrafo.addArc(noP, noQ);
        }
        if (!digrafo.isArc(q, pe)) {
            this.digrafo.addArc(q, pe);
        }
    }

    /**
     *
     * @param disj
     * @param digrafo
     */
    private void implicacionNoPoQ(int noP, int q, DiGraph digrafo) {
        int pe = noP - 1;
        int noQ = q + 1;

        if (!digrafo.isArc(pe, q)) {
            this.digrafo.addArc(pe, q);
        }
        if (!digrafo.isArc(noQ, noP)) {
            this.digrafo.addArc(noQ, noP);
        }
    }

    /**
     *
     * @param nCalle
     * @return
     */
    public int calle(int nCalle) {
        int absNCalle = (0 <= nCalle ? nCalle - 1 : (nCalle * (-1)) );
        int calle = ( (absNCalle * 2) - ( nCalle < 0 ? 1 : 0 ) );
        return calle;
    }

    /**
     *
     * @param nAvenida
     * @return
     */
    public int avenida(int nAvenida) {
        int absNAvenida = (0 <= nAvenida ? nAvenida - 1 : (nAvenida * (-1)));
        int avenida = ((absNAvenida * 2) - ( nAvenida < 0 ? 1 : 0));
        avenida = avenida + this.offset;
        return avenida;
    }

    /**
     *
     * @param caso
     * @param linea
     * @param tokens
     * @throws IOException
     */
    public void inicializacionesMisc(String linea, String[] tokens)
                                                            throws IOException
    {
        // Se verifica que se lean números enteros
        try {
            this.c = Integer.parseInt(tokens[0]);
            this.a = Integer.parseInt(tokens[1]);
            this.p = Integer.parseInt(tokens[2]);
        } catch (NumberFormatException nfe) {
            throw new ExcepcionFormatoIncorrecto("\nProblema leyendo la " +
                    "primera linea del caso " + this.nCasosProbados + ":\nSe " +
                    "esperaban 3 enteros y se encontró:\n\n\t\t" + linea );
        }

        /* Se calcula el desplazamiento: número en el que comienza la repre-
         * sentación de las avenidas
         */
        this.offset = 2 * this.c;

        /* Se inicializa el numero de nodos que tendra el digrafo que usaremos
         * como grafo de implicaciones para resolver esta instancia del problema
         */
        this.nNodos = 2 * (this.a + this.c);
    }

    /**
     *
     * @param caso
     * @param linea
     * @param tokens
     * @throws IOException
     */
    public void almacenamientoDeViajes(String linea, String[] tokens)
                                                            throws IOException
    {
        // Se almacenan los pares de lugares a visitar
        for (int i = 0; i < this.p; i++) {
            linea = this.in.readLine();
            tokens  = linea.split(" ");
            if (tokens.length != 4) {
                throw new ExcepcionFormatoIncorrecto("\nProblema leyendo " +
                        "la linea " + (i+2) + " del caso " + this.nCasosProbados
                        + ":\nSe esperaban 4 enteros y se encontró:\n\n\t\t" +
                        linea);
            } else {

                // Se calcula el i-ésimo par de lugares
                int[] pares = new int[4];
                try {
                    pares[0] = Integer.parseInt(tokens[0]);
                    pares[1] = Integer.parseInt(tokens[1]);
                    pares[2] = Integer.parseInt(tokens[2]);
                    pares[3] = Integer.parseInt(tokens[3]);
                } catch (NumberFormatException nfe) {
                    throw new ExcepcionFormatoIncorrecto("\nProblema " +
                            "leyendo la linea " + (i+2) + " del caso " +
                            + this.nCasosProbados + ": Se esperaban enteros " +
                            "y se encontró:\n\n\t\t" + linea);
                }

                // Se agrega a la lista de pares
                this.viajes.add(pares);
            }
        }
    }

    /**
     *
     * @param caso
     * @throws IOException
     */
    public void sigCasoDePrueba() throws IOException {
        this.nCasosProbados++;
        // Se lee la primera linea del caso en uso
        String linea = this.in.readLine();
        String[] tokens = linea.split(" ");

        // Se verifica que haya exactamente 3 números
        if (tokens.length != 3) {
            throw new ExcepcionFormatoIncorrecto("\nProblema leyendo la " +
                    "primera linea del caso numero " + this.nCasosProbados +
                    ". Se esperaban" + "3 números y se encontraron " +
                    tokens.length);
        } else {
            this.inicializacionesMisc(linea, tokens);

            this.almacenamientoDeViajes(linea, tokens);
        }
    }

    /**
     *
     */
    public void construirClausulas() {
        Iterator iterador = this.viajes.iterator();
        while (iterador.hasNext()) {
            int[] par = (int[])iterador.next();

            par[0] = calle(par[0]);
            par[1] = avenida(par[1]);
            par[2] = calle(par[2]);
            par[3] = avenida(par[3]);

            int[] clausula = this.construirClausula(par);

            if (clausula != null) {
                this.clausulas.add(clausula);
            }
        }
    }

    /**
     * 
     */
    public void construir2CNF() {
        Iterator iterador = this.clausulas.iterator();
        while (iterador.hasNext()) {
            int[] clausula = (int[])iterador.next();

            int[][] disjunciones = Main.distributiva(clausula);

            // Se agrega la lista de disjunciones para formar la fórmula 2CNF:

            this.dosCNF.add(disjunciones[0]);
            this.dosCNF.add(disjunciones[1]);
            this.dosCNF.add(disjunciones[2]);
            this.dosCNF.add(disjunciones[3]);
        }
    }

    /**
     *
     * @return
     */
    public void construirGrafoDeImplicaciones() {
        
        /* Por razones de eficiencia, se escogió la implementación
         * DiGraphList, pero se deja el otro constructor comentado para
         * facilmente cambiar de implementación.
         */

        this.digrafo = new DiGraphList(this.nNodos + this.nDummies);
        //this.digrafo = new DiGraphMatrix(this.nNodos + this.nDummies);

        Iterator iter = this.dosCNF.iterator();

        while (iter.hasNext()) {
            int[] disj = (int[])iter.next();

            if (disj[0] % 2 == 0) {
                if (disj[1] % 2 == 0) {
                    this.implicacionPoQ(disj[0], disj[1], digrafo);
                } else {
                    this.implicacionPoNoQ(disj[0], disj[1], digrafo);
                }
            } else {
                if (disj[1] % 2 == 0) {
                    this.implicacionNoPoQ(disj[0], disj[1], digrafo);
                } else {
                    this.implicacionNoPoNoQ(disj[0], disj[1], digrafo);
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        Main vialidad = null;
        if (args.length == 2) {
            vialidad = new Main(args[0], args[1]);
        } else {
            throw new ExcepcionFormatoIncorrecto("Error de sintaxis en la " +
                    "llamada del programa.\n\nUSO:\n\n\tjava Main " +
                    "archivo_entrada.input archivo_salida.output\n\n");
        }

        while (vialidad.hasNext()) {
            vialidad.sigCasoDePrueba();
            vialidad.construirClausulas();
            vialidad.construirGrafoDeImplicaciones();
        }

    }

}