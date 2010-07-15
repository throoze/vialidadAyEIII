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
 */
public class Main {


    /**
     * Nombre del archivo de entrada.
     */
    private String              inputFile;

    /**
     * Nombre del archivo de salida.
     */
    private String              outputFile;

    /**
     * Buffer de entrada (Lectura).
     */
    private BufferedReader      in;

    /**
     * Flujo de salida (Escritura).
     */
    private PrintStream         out;

    /**
     * Numero de instancias del problema a resolver.
     */
    private int                 nInstancias;
    
    /**
     * Numero de casos que se han extraido del archivo de entrada
     */
    private int                 nCasosProbados;

    /**
     * Numero de calles del caso en uso.
     */
    private int                 c;

    /**
     * Numero de avenidas del caso en uso.
     */
    private int                 a;

    /**
     * Numero de pares de lugares del caso en uso.
     */
    private int                 p;

    /**
     * Número en el que termina la representación de las calles y comienza la de
     * las avenidas
     */
    private int                 offset;

    /**
     * Lista de pares de puntos entre los cuales se debe verificar si se puede
     * viajar de manera eficiente.
     */
    private List<int[]>         viajes;

    /**
     * Cláusulas iniciales que modelan el problema.
     */
    private List<int[]>         clausulas;

    /**
     * Lista que almacena los literales "solitarios" o cláusulas unitarias que
     * representan los casos en los que se viaja en la misma calle o avenida.
     */
    private Stack<Integer>      literalesSolos;

    /**
     * Cláusulas que modelan el problema en la forma 2CNF.
     */
    private List<int[]>         dosCNF;

    /**
     * Grafo de implicaciones que resolverá esta instancia del problema
     */
    private DiGraph             digrafo;

    /**
     * Número de nodos que tendrá el grafo de implicaciones
     */
    private int                 nNodos;

    /**
     * Guarda la lista de las componentes fuertemente conexas halladas
     */
    private List<List<Integer>> compsFuertConexas;

    /**
     * Dice si el caso de prueba tratado tiene solucion o no. La primera posi-
     * cion indica si se ha confirmado una desicion sobre el resultado del pro-
     * blema o no, y la segunda posicion indica la desicion propiamente dicha.
     */
    private boolean[]           tieneSolucion;

    // CONSTRUCTOR:

    /**
     * Constructor de la Clase Main. Se encarga de inicializar todos los valores
     * al inicio del programa.
     * pre: true
     * post: Se genera una instancia de la clase Main preparada para resolver
     * una nueva instancia del problema.
     * @param inFile Archivo de entrada
     * @param outFile Archivo de Salida
     * @throws IOException En caso de que ocurra un error al leer o escribir, o
     * un error de formato
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
                this.in = new BufferedReader(new FileReader(this.inputFile));
                this.out = new PrintStream(this.outputFile);
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
     * Distribuye las clausulas conseguidas en la primera fase.
     * pre: clausula.length == 4;
     * post: Se genera un arreglo de tamaño 8 que representa la clausula
     * recibida de manera distribuida (4 disjunciones, 8 literales).
     * @param clausula La cláusula a distribuir
     * @return Arreglo que representa la distribucion de los literales de la
     * clausula
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

    /**
     * Invierte los contenidos de las cajas de un arreglo de tamaño 2.
     * pre: disj.length == 2
     * post: se devuelve un arreglo con las posiciones de disj invertidas.
     * @param disj arreglo a invertir
     * @return un arreglo con los contenidos invertidos a los de entrada.
     */
    private static int[] simetrico(int[] disj) {
        if (disj.length == 2) {
            int[] aux = new int[2];
            aux[0] = disj[1];
            aux[1] = disj[0];
            return aux;
        }
        return null;
    }

    // MÉTODOS NO-ESTÁTICOS:
    
    /**
     * Indica si queda algun caso por probar
     * pre: true
     * post: se devuelve el resultado de evaluar la expresion:
     * (this.nCasosProbados &lt; this.nInstancias)
     * @return el resultado de evaluar la expresion:
     * (this.nCasosProbados &lt; this.nInstancias)
     */
    public boolean hasNext() {
        return (this.nCasosProbados < this.nInstancias);
    }

    /**
     * Construye la clausula en caso de que el viaje sea hacia la derecha del
     * mapa
     * pre: ca1, ca2, av3 y av4 deben ser enteros dentro del rango de calles y
     * avenidas de esta instancia del problema;
     * post: Se devuelve la clausula que representa el caso de ir hacia la
     * derecha en el mapa
     * @param ca1 calle del punto 1
     * @param av1 avenida del punto 1
     * @param ca2 calle del punto 2
     * @param av2 avenida del punto 2
     * @return Clausula  en forma de arreglo de enteros generada para este caso
     */
    private int[] haciaLaDerecha(int ca1, int av1, int ca2, int av2) {

        int[] clausula = new int[4];

        if (ca1 < ca2) {
            // Primer posible camino
            clausula[0]= this.calle(ca1); clausula[1]= this.avenida(av2);
            // Segundo posible camino
            clausula[2]= this.calle(ca2); clausula[3]= this.avenida(av1);
        } else if (ca1 == ca2) {
            /* Sólo hay un camino posible. Almaceno este literal en la lista de
             * literales solos.
             */
            if (!this.literalesSolos.contains(new Integer(this.calle(-ca1)))) {
                if (!this.literalesSolos.contains
                                (new Integer(this.calle(ca1)))) {
                    this.literalesSolos.push(new Integer(this.calle(ca1)));
                }
            } else {
                this.tieneSolucion[0] = true;
                this.tieneSolucion[1] = false;
            }
            return null;
        } else {
            // Primer posible camino
            clausula[0]= this.calle(ca1); clausula[1]= this.avenida(-av2);
            // Segundo posible camino
            clausula[2]= this.calle(ca2); clausula[3]= this.avenida(-av1);
        }

        return clausula;
    }

    /**
     * Construye la clausula en caso de que el viaje sea hacia la izquierda del
     * mapa.
     * pre: ca1, ca2, av3 y av4 deben ser enteros dentro del rango de calles y
     * avenidas de esta instancia del problema;
     * post: Se devuelve la clausula que representa el caso de ir hacia la
     * izquierda en el mapa
     * @param ca1 calle del punto 1
     * @param av1 avenida del punto 1
     * @param ca2 calle del punto 2
     * @param av2 avenida del punto 2
     * @return Clausula  en forma de arreglo de enteros generada para este caso
     */
    private int[] haciaLaIzquierda(int ca1, int av1, int ca2, int av2) {

        int[] clausula = new int[4];

        if (ca1 < ca2) {// hacia abajo
            // Primer posible camino
            clausula[0]= this.calle(-ca1); clausula[1]= this.avenida(av2);
            // Segundo posible camino
            clausula[2]= this.calle(-ca2); clausula[3]= this.avenida(av1);
        } else if (ca1 == ca2) {
            /* Sólo hay un camino posible. Almaceno este literal en la lista de
             * literales solos.
             */
            if (!this.literalesSolos.contains(new Integer(this.calle(ca1)))) {
                if (!this.literalesSolos.contains
                                (new Integer(this.calle(-ca1)))) {
                    this.literalesSolos.push(new Integer(this.calle(-ca1)));
                }
            } else {
                this.tieneSolucion[0] = true;
                this.tieneSolucion[1] = false;
            }
            return null;
        } else { // hacia arriba
            // Primer posible camino
            clausula[0]= this.calle(-ca1); clausula[1]= this.avenida(-av2);
            // Segundo posible camino
            clausula[2]= this.calle(-ca2); clausula[3]= this.avenida(-av1);
        }

        return clausula;
    }

    /**
     * Construye la clausula en caso de que el viaje sea en la misma avenida
     * pre: ca1, ca2, av3 y av4 deben ser enteros dentro del rango de calles y
     * avenidas de esta instancia del problema;
     * post: Se devuelve la clausula que representa el caso de viajar en la
     * misma avenida
     * @param ca1 calle del punto 1
     * @param av1 avenida del punto 1
     * @param ca2 calle del punto 2
     * @param av2 avenida del punto 2
     * @return Clausula  en forma de arreglo de enteros generada para este caso
     */
    private int[] mismaAvenida(int ca1, int av1, int ca2, int av2) {

        if (ca1 < ca2) { // hacia abajo
            /* Sólo hay un camino posible. Almaceno este literal en la lista de
             * literales solos.
             */
            if (!this.literalesSolos.contains(new Integer(this.avenida(-av1)))){
                if (!this.literalesSolos.contains
                                (new Integer(this.avenida(av1)))) {
                    this.literalesSolos.push(new Integer(this.avenida(av1)));
                }
            } else {
                this.tieneSolucion[0] = true;
                this.tieneSolucion[1] = false;
            }
        } else if (ca2 < ca1) { // hacia arriba
            /* Sólo hay un camino posible. Almaceno este literal en la lista de
             * literales solos.
             */
            if (!this.literalesSolos.contains(new Integer(this.avenida(av1)))){
                if (!this.literalesSolos.contains
                                (new Integer(this.avenida(-av1)))) {
                    this.literalesSolos.push(new Integer(this.avenida(-av1)));
                }
            } else {
                this.tieneSolucion[0] = true;
                this.tieneSolucion[1] = false;
            }
        } else {
            /* No se trata el caso cual las calles son iguales, ya que se
             * trataría de el mismo punto y no habría nada que hacer. En este
             * caso, se retorna null.
             */
        }
        return null;
    }

    /**
     * Llama a las funciones que consruiran la clausula representada en pair
     * pre: pair.length == 4
     * post: Se devuelve un arreglo de enteros que representa la clausula co-
     * rrespondiente al viaje entre los puntos representados en pair.
     * @param pair Arreglo que representa el par de puntos entre los que se
     * viaja.
     * @return un arreglo que representa la clausula del viaje.
     */
    private int[] construirClausula(int[] pair) {

        int[] clausula = null;

        int ca1 = pair[0];
        int av1 = pair[1];
        int ca2 = pair[2];
        int av2 = pair[3];

        /* Esto quiere decir que estamos yendo desde el punto (av1,ca1) hacia
         * el punto (av2,ca2).
         */

        if (av1 < av2) {
            clausula = this.haciaLaDerecha(ca1, av1, ca2, av2);
        } else if (av1 == av2) {
            clausula = this.mismaAvenida(ca1, av1, ca2, av2);
        } else {
            clausula = this.haciaLaIzquierda(ca1, av1, ca2, av2);
        }

        return clausula;
    }


    /**
     * Crea un arco en el grafo de implicaciones correspondiente a una disjun-
     * cion del tipo (p \/ q)
     * pre: la disjuncion debe ser del tipo (p \/ q); p y q deben pertenecer a
     * digrafo
     * post: Se añade un arco que representa esta disjuncion
     * @param disj disjuncion  a procesar
     * @param digrafo digrafo de implicaciones
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
     * Crea un arco en el grafo de implicaciones correspondiente a una disjun-
     * cion del tipo (!p \/ !q)
     * pre: la disjuncion debe ser del tipo (!p \/ !q); p y q deben pertenecer a
     * digrafo
     * post: Se añade un arco que representa esta disjuncion
     * @param disj disjuncion  a procesar
     * @param digrafo digrafo de implicaciones
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
     * Crea un arco en el grafo de implicaciones correspondiente a una disjun-
     * cion del tipo (p \/ !q)
     * pre: la disjuncion debe ser del tipo (p \/ !q); p y q deben pertenecer a
     * digrafo
     * post: Se añade un arco que representa esta disjuncion
     * @param disj disjuncion  a procesar
     * @param digrafo digrafo de implicaciones
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
     * Crea un arco en el grafo de implicaciones correspondiente a una disjun-
     * cion del tipo (!p \/ q)
     * pre: la disjuncion debe ser del tipo (!p \/ q); p y q deben pertenecer a
     * digrafo
     * post: Se añade un arco que representa esta disjuncion
     * @param disj disjuncion  a procesar
     * @param digrafo digrafo de implicaciones
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
     * Mapea los numeros de calle a su correspondiente numero en la representa-
     * cion.
     * pre: nCalle debe estar dentro del rango de numero de calles de esta
     * instancia del problema;
     * post: se retorna la representacion de nCalle
     * @param nCalle numero de calle a mapear
     * @return la representacion de nCalle
     */
    private int calle(int nCalle) {
        int absNCalle = (0 <= nCalle ? nCalle - 1 : (nCalle * (-1)) );
        int calle = ( (absNCalle * 2) - ( nCalle < 0 ? 1 : 0 ) );
        return calle;
    }

    /**
     * Mapea los numeros de avenida a su correspondiente numero en la represen-
     * tacion.
     * pre: nAvenida debe estar dentro del rango de numero de avenidas de esta
     * instancia del problema;
     * post: se retorna la representacion de nAvenida
     * @param nAvenida numero de avenida a mapear
     * @return la representacion de nAvenida
     */
    private int avenida(int nAvenida) {
        int absNAvenida = (0 <= nAvenida ? nAvenida - 1 : (nAvenida * (-1)));
        int avenida = ((absNAvenida * 2) - ( nAvenida < 0 ? 1 : 0));
        avenida = avenida + this.offset;
        return avenida;
    }

    // REVISAR LA DOCUMENTACION A PARTIR DE AQUI.
    /**
     * Se inicializan los campos de esta clase que dan los datos iniciales de
     * este caso de prueba.
     * pre: linea contiene la linea recien parseada y token sus elementos
     * post: se inicializan los campos de la clase con estos valores. Estos son:
     * this.a, this.c, this.p, this.offset, y this.nNodos
     *
     * @param linea linea parseada
     * @param tokens arreglo que contiene los elementos de linea
     * @throws IOException en caso de ocurrir un error de formato
     */
    private void inicializacionesMisc(String linea, String[] tokens)
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
     * Almacena los pares de puntos entre los que se viajara
     * pre: linea contiene la linea recien parseada y token sus elementos
     * post: this.viajes contendrá los valores obtenidos de parsear la parte del
     * archivo de entrada que corresponde a los pares entre los que se viaja en
     * esta instancia del problema.
     * @param linea linea parseada
     * @param tokens arreglo que contiene los elementos de linea
     * @throws IOException en caso de ocurrir un error de I/O o de formato
     */
    private void almacenamientoDeViajes(String linea, String[] tokens)
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
     * Carga el siguiente caso de prueba a analizar en los campos de esta clase
     * pre: se debe haber creado una nueva instancia de la clase Main;
     * post: se inicializan todos los campos de la clase que no requieren del
     * procesamiento de datos.
     * @throws IOException en caso de que haya un error de lectura o de formato
     * en el archivo de entrada
     */
    public void sigCasoDePrueba() throws IOException {
        this.nCasosProbados++;

        // REINICIALIZAR TODO:

        this.tieneSolucion = new boolean[2];
        this.tieneSolucion[0] = false;
        this.tieneSolucion[1] = false;

        this.clausulas = new Lista();
        this.compsFuertConexas = new Lista();
        this.dosCNF = new Lista();
        this.literalesSolos = new Stack();
        this.nNodos = 0;
        this.offset = 0;
        this.viajes = new Lista();

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
     * Construye las clausulas correspondientes al viaje entre cada punto para
     * esta instancia del problema.
     * pre: Se deben haber inicalizado todos los campos que no requieren
     * procesamiento de datos.
     * post: las clausulas que modelan esta innstancia del problema estan carga-
     * das en this.clausulas
     */
    public void construirClausulas() {
        Iterator iterador = this.viajes.iterator();
        while (iterador.hasNext() && !this.tieneSolucion[0]) {
            int[] par = (int[]) iterador.next();
            
            int[] clausula = this.construirClausula(par);

            if (clausula != null) {
                this.clausulas.add(clausula);
            }
        }
    }

    /**
     * Simplifica el modelo en forma 2CNF para eliminar las clausulas unitarias
     * pre: Se deben haber construido las clausulas que modelan los viajes
     * post: this.literalesSolos esta vacia, y las clausulas que dependian de su
     * anterior contenido se han procesado adecuadamente
     */
    private void simplificar2CNF() {

        if (!this.tieneSolucion[0]) {
            while (!this.literalesSolos.empty()) {
                int lit = ((Integer) this.literalesSolos.pop()).intValue();

                int[][] claus = new int[this.dosCNF.size()][];
                Iterator auxIter = this.dosCNF.iterator();
                for (int i = 0; i < claus.length; i++) {
                    int[] arr = (int[]) auxIter.next();
                    claus[i] = arr;
                }

                for (int i = 0; i < claus.length; i++) {
                    this.procesarLit(i, lit, claus);
                }
            }
            if (this.dosCNF.isEmpty()) {
                this.tieneSolucion[0] = true;
                this.tieneSolucion[1] = true;
            }
        }
    }

    /**
     * Procesa el literal pasado. Decide que hacer en funcion de la comparación
     * del literal con cada parte de la disjuncion pasada.
     * pre: i debe estar entre 0 y disjs.length y lit debe estar entre 0 y
     * this.nNodos; se deben haber creado las clausulas de forma 2CNF y disjs
     * debe contener dichas cláusulas.
     * post: se analizan todas las disjunciones y, si en alguna de ellas está
     * lit, ésta se elimina; y si está noLit, se elimina y el literal que lo
     * acompaña se agrega a la pila de literales solos.
     *
     * @param i Numero de disjunción que se está analizando
     * @param lit Representación del literal a comparar
     * @param disjs Todas las disjunciones a comparar
     */
    private void procesarLit(int i, int lit, int[][] disjs) {

        int noLit = (lit % 2 == 0 ? lit + 1 : lit - 1);
        int pe = disjs[i][0];
        int q = disjs[i][1];
        int noP = (pe % 2 == 0 ? pe + 1 : pe - 1);
        int noQ = (q % 2 == 0 ? q + 1 : q - 1);

        if (pe == noLit) {
            this.dosCNF.remove(disjs[i]);
            if (!this.literalesSolos.contains(new Integer(noQ))) {
                if (!this.literalesSolos.contains(new Integer(q))) {
                    this.literalesSolos.push(new Integer(q));
                }
            } else {
                this.tieneSolucion[0] = true;
                this.tieneSolucion[1] = false;
            }
        }

        if (q == noLit) {
            this.dosCNF.remove(disjs[i]);
            if (!this.literalesSolos.contains(new Integer(noP))) {
                if (!this.literalesSolos.contains(new Integer(pe))) {
                    this.literalesSolos.push(new Integer(pe));
                }
            } else {
                this.tieneSolucion[0] = true;
                this.tieneSolucion[1] = false;
            }
        }
    }

    /**
     * Transforma las clausulas calculadas a la forma 2CNF
     * pre: Se debe haber creado las clausulas que modelan los viajes.
     * post: se cargan las clausulas en la forma 2CNF en this.dosCNF
     */
    public void construir2CNF() {

        if (!this.tieneSolucion[0]) {
            Iterator iterador = this.clausulas.iterator();
            while (iterador.hasNext()) {
                int[] clausula = (int[]) iterador.next();

                int[][] disjunciones = Main.distributiva(clausula);

                /* Se agrega la lista de disjunciones para formar la fórmula
                 * de la forma 2CNF:
                 */

                int[] disj1 = new int[2];
                disj1[0] = disjunciones[0][0];
                disj1[1] = disjunciones[1][0];

                int[] disj2 = new int[2];
                disj2[0] = disjunciones[0][1];
                disj2[1] = disjunciones[1][1];

                int[] disj3 = new int[2];
                disj3[0] = disjunciones[0][2];
                disj3[1] = disjunciones[1][2];

                int[] disj4 = new int[2];
                disj4[0] = disjunciones[0][3];
                disj4[1] = disjunciones[1][3];

                if (!this.dosCNF.contains(disj1) &&
                    !this.dosCNF.contains(Main.simetrico(disj1))) {
                    this.dosCNF.add(disj1);
                }
                if (!this.dosCNF.contains(disj2) &&
                    !this.dosCNF.contains(Main.simetrico(disj2))) {
                    this.dosCNF.add(disj2);
                }
                if (!this.dosCNF.contains(disj3) &&
                    !this.dosCNF.contains(Main.simetrico(disj3))) {
                    this.dosCNF.add(disj3);
                }
                if (!this.dosCNF.contains(disj4) &&
                    !this.dosCNF.contains(Main.simetrico(disj4))) {
                    this.dosCNF.add(disj4);
                }
            }

            this.simplificar2CNF();
        }
    }

    /**
     * Construye el grafo de implicaciones que representa ésta instancia del
     * problema
     * pre: Debe de haberse construido y simplificado las clausulas de la forma
     * 2CNF
     * post: en caso de que no se haya conseguido ya la solucion, se construye
     * un grafo de implicaciones que modela esta instancia del problema.
     */
    public void construirGrafoDeImplicaciones() {

        if (!this.tieneSolucion[0]) {
            /* Por razones de eficiencia, se escogió la implementación
             * DiGraphList, pero se deja el otro constructor comentado para
             * facilmente cambiar de implementación.
             */

            this.digrafo = new DiGraphList(this.nNodos);
            //this.digrafo = new DiGraphMatrix(this.nNodos + this.nDummies);

            int[][] disj = new int[this.dosCNF.size()][];
            Iterator iter = this.dosCNF.iterator();

            for (int i = 0; i < disj.length; i++) {
                int[] arr = (int[]) iter.next();
                disj[i] = arr;
            }

            for (int i = 0; i < disj.length; i++) {

                if (disj[i][0] % 2 == 0) {
                    if (disj[i][1] % 2 == 0) {
                        this.implicacionPoQ(disj[i][0], disj[i][1], digrafo);
                    } else {
                        this.implicacionPoNoQ(disj[i][0], disj[i][1], digrafo);
                    }
                } else {
                    if (disj[i][1] % 2 == 0) {
                        this.implicacionNoPoQ(disj[i][0], disj[i][1], digrafo);
                    } else {
                        this.implicacionNoPoNoQ(disj[i][0], disj[i][1], digrafo);
                    }
                }
            }
        }
    }

    /**
     * calcula las componentes fuertemente conexas usando el algoritmo de Tarjan
     * pre: this.digrafo != null;
     * post: Se calculan las componentes fuertemente conexas del digrafo que
     * modela esta instancia del problema y se cargan en this.compsFuertConexas.
     */
    public void calcularComponentesFuertementeConexas() {

        if (!this.tieneSolucion[0]) {
            Tarjan tarjan = new Tarjan(this.digrafo);
            this.compsFuertConexas = tarjan.ejecutar();

            if (this.compsFuertConexas.size() == 1) {
                this.tieneSolucion[0] = true;
                this.tieneSolucion[1] = false;
            }

            if (this.compsFuertConexas.size() == this.nNodos) {
                this.tieneSolucion[0] = true;
                this.tieneSolucion[1] = true;
            }
        }
    }

    /**
     * Analiza una componente para ver si consigue literales que den contradic-
     * cion.
     * pre: componente debe contener una componente F.C. del grafo del problema
     * post: se retorna true si hay contrdiccion en esta componente, false en
     * caso contrario.
     * @param componente C.F.C. a analizar
     * @return true si no hay contradiccion en esta componente, false en caso
     * contrario.
     */
    private boolean analizarComponente(List<Integer> componente) {
        int[] comp = new int[componente.size()];
        Iterator iter = componente.iterator();
        boolean noHayProblema = true;
        
        for (int i = 0; i < comp.length; i++) {
            comp[i] = ((Integer)iter.next()).intValue();
        }
        
        for (int i = 0; i < comp.length && noHayProblema; i++) {
            if (comp[i] < ((2 * this.a ) + (2 * this.c ))) {
                for (int j = i + 1; j < comp.length && noHayProblema; j++) {
                    if (comp[j] < ((2 * this.a ) + (2 * this.c ))) {
                        if ( comp[i] % 2 == 0 ) {
                            noHayProblema = (comp[j] != comp[i] + 1);
                        } else {
                            noHayProblema = (comp[j] != comp[i] - 1);
                        }
                    }
                }
            }
        }
        return noHayProblema;
    }

    /**
     * Escribe el resultado de los calculos en el archivo de salida
     * pre: true;
     * post: En caso de que no se haya encontrado antes la solución, se analizan
     * las componentes fuertemente conexas para determinar la solución, y se
     * imprime la misma en el archivo de salida.
     */
    public void decision() {
        String desicion = "";

        if (!this.tieneSolucion[0]) {

            Iterator iter = this.compsFuertConexas.iterator();
            boolean existeVialidad = true;
            
            while (iter.hasNext() && existeVialidad) {
                List<Integer> aux = (List<Integer>)iter.next();
                existeVialidad = analizarComponente(aux);
            }

            this.tieneSolucion[0] = true;
            this.tieneSolucion[1] = existeVialidad;
        }

        desicion = ( this.tieneSolucion[1] ? "Si." : "No.");

        this.out.println(desicion);
    }

    /**
     * Programa trincipal. Controla el flujo del resto de los métodos.
     * pre: Se debe llamar con la sintaxis indicada, y el formato del archivo de
     * entrada debe estar acorde con el formato del enunciado del proyecto.
     * post: Se imprime en el archivo de salida el resultado de cada una de las
     * instancias del problema propuestas en el archivo de salida.
     *
     * @param args los argumentos pasados por linea de comandos. la sintaxis del
     * programa es: java Main archivo_entrada.input archivo_salida.output
     * @throws IOException En caso de que se produzca un error del tipo I/O, o
     * de formato en el archivo de entrada.
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
            vialidad.construir2CNF();
            vialidad.construirGrafoDeImplicaciones();
            vialidad.calcularComponentesFuertementeConexas();
            vialidad.decision();
        }

    }
}