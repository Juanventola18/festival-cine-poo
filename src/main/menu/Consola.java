package main.menu;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class Consola {
    private static final Scanner teclado = new Scanner(System.in);

    public static String leer(String prompt) {
        System.out.print(prompt + ": ");
        return teclado.nextLine();
    }

    public static int leerInt(String prompt) {
        return Integer.parseInt(leer(prompt));
    }

    public static double leerDouble(String prompt) {
        return Double.parseDouble(leer(prompt));
    }

    public static <T> void listar(List<T> lista, Function<T, String> etiqueta) {
        for (int i = 0; i < lista.size(); i++)
            System.out.println("  " + i + ". " + etiqueta.apply(lista.get(i)));
    }

    public static int nextId(List<?> lista) {
        return lista.size() + 1;
    }

    public static Scanner getTeclado() {
        return teclado;
    }
}
