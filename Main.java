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
 * @author Victor De Ponte, 05-38087
 * @author Karina Valera, 06-40414
 */
public class Main {

    private String          inputFile;  // Nombre del archivo de entrada.

    private String          outputFile; // Nombre del archivo de salida.

    private BufferedReader  in;         // Buffer de entrada (Lectura)
    private PrintStream     out;        // Flujo de salida (Escritura)

    private int             nInstances; // Numero de instancias del problema a
                                        // resolver.

    private int             c;          // Numero de calles del caso en uso

    private int             a;          // Numero de avenidas del caso en uso

    private int             p;          // Numero de pares de lugares del caso
                                        // en uso.

    private List<int[]>     viajes;     // Lista de pares de puntos entre los
                                        // cuales se debe verificar si se puede
                                        // viajar de manera eficiente.

    private String          salida;     // Almacena la salida del programa

    private List<int[]>     formulas;   // Cláusulas iniciales que modelan el
                                        // problema.

    private List<int[]>     dosCNF;     // Cláusulas que modelan el problema en
                                        // la forma 2CNF.

    private int             cero;       // Cero simbólico para poder representar
                                        // los complementos de las cláusulas.


    /**
     *
     * @param nCalle
     * @return
     */
    public int calle(int nCalle) {
        int absNCalle = (0 <= nCalle ? nCalle : (nCalle*(-1)) );
        int offset = ((absNCalle * 2) - 1);
        int calle = 0;
        if (0 <= nCalle) {
            calle = this.cero + offset;
        } else {
            calle = this.cero - offset;
        }
        return calle;
    }

    /**
     *
     * @param nAvenida
     * @return
     */
    public int avenida(int nAvenida) {
        int absNAvenida = (0 <= nAvenida ? nAvenida : (nAvenida*(-1)));
        int offset = (absNAvenida * 2);
        int avenida = 0;
        if (0 <= nAvenida) {
            avenida = this.cero + offset;
        } else {
            avenida = this.cero - offset;
        }
        return avenida;
    }

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

            this.cero = this.a + this.c;

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
    public void construirFormulas() {
        Iterator iterador = this.viajes.iterator();
        while (iterador.hasNext()) {
            int[] par = (int[])iterador.next();
            int[] propos = construirFormula(par);
            this.formulas.add(propos);
        }
    }

    private static int[] construirFormula(int[] pair) {
        int[] formula = new int[4];

        return formula;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
