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
    private int             nInstances;

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
    private List<int[]>     formulas;

    /**
     * Cláusulas que modelan el problema en la forma 2CNF.
     */
    private List<int[]>     dosCNF;

    /**
     * Almacena la salida del programa
     */
    private String          salida;



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
                this.nInstances = Integer.parseInt(primera);
            } catch (NumberFormatException nfe) {
                throw new ExcepcionFormatoIncorrecto("\nProblema leyendo la " +
                        "primera linea:\nSe esperaba un entero y se encontró:"
                        + "\n\n\t\t" + primera );
            }
        }
    }


    // MÉTODOS:

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
     * @throws IOException
     */
    public void sigCasoDePrueba(int caso) throws IOException {
        // Se lee la primera linea del caso en uso
        String linea = this.in.readLine();
        String[] tokens = linea.split(" ");

        // Se verifica que haya exactamente 3 números
        if (tokens.length != 3) {
            throw new ExcepcionFormatoIncorrecto("\nProblema leyendo la " +
                    "primera linea del caso numero " + caso + ". Se esperaban" +
                    "3 números y se encontraron " + tokens.length);
        } else {

            // Se verifica que se lean números enteros
            try {
                this.c = Integer.parseInt(tokens[0]);
                this.a = Integer.parseInt(tokens[1]);
                this.p = Integer.parseInt(tokens[2]);
            } catch (NumberFormatException nfe) {
                throw new ExcepcionFormatoIncorrecto("\nProblema leyendo la " +
                        "primera linea del caso " + caso + ":\nSe esperaban " +
                        "3 enteros y se encontró:\n\n\t\t" + linea );
            }

            /* Se calcula el desplazamiento: número en el que comienza la repre-
             * sentación de las avenidas
             */
            this.offset = 2 * this.c;

            // Se almacenan los pares de lugares a visitar
            for (int i = 0; i < this.p; i++) {
                linea = this.in.readLine();
                tokens  = linea.split(" ");
                if (tokens.length != 4) {
                    throw new ExcepcionFormatoIncorrecto("\nProblema leyendo " +
                            "la linea " + (i+2) + " del caso " + caso + ":\n" +
                            "Se esperaban 4 enteros y se encontró:\n\n\t\t" +
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
                                + caso + ": Se esperaban enteros y se " +
                                "encontró:\n\n\t\t" + linea);
                    }

                    // Se agrega a la lista de pares
                    this.viajes.add(pares);
                }
            }
        }
    }

    /**
     *
     */
    public void construirClausulas() {
        Iterator iterador = this.viajes.iterator();
        while (iterador.hasNext()) {
            int[] par = (int[])iterador.next();
            int[] propos = construirClausula(par);
            this.formulas.add(propos);
        }
    }

    private static int[] construirClausula(int[] pair) {
        int[] formula = new int[4];
        int a = pair[0];
        int b = pair[1];
        int c = pair[2];
        int d = pair[3];



        // Esto quiere decir que estamos yendo del punto (a,b) al punto (c,d)

        if (a < c) {
            if (b < d) {

            } else if (b == d) {

            } else {

            }
        } else if (a == c) {
            if (b < d) {

            } else if (b == d) {

            } else {

            }
        } else {
            if (b < d) {

            } else if (b == d) {

            } else {

            }
        }
        return formula;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
