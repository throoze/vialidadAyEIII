
import java.util.Iterator;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author victor
 */
public class Prueba {



    public static void main (String[] args) {
        DiGraph dig = new DiGraphList(4);
        dig.addArc(0, 1);
        dig.addArc(1, 0);
        dig.addArc(2, 3);
        dig.addArc(3, 2);
        dig.addArc(1, 2);
        List<List<Integer>> compsFuertConexas = new Lista();

        Tarjan tar = new Tarjan(dig);

        compsFuertConexas = tar.ejecutar();

        System.out.println("Componentes Fuertemente Conexas:\n");
            int[][] claus = new int[compsFuertConexas.size()][];
            Iterator auxIter = compsFuertConexas.iterator();
            for (int i = 0; i < claus.length; i++) {
                List<Integer> lis = (List<Integer>)auxIter.next();
                System.out.println("Componente "+i+" :\n" + lis.toString());
            }
    }
}
