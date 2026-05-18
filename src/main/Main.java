package main;

import java.util.Scanner;
import subsistema_alimentos_medicamentos.ControladorSubsistemaAlimMed;
import subsistema_patrullas.ControladorSubsistemaPatrullas;
import subsistema_taller.ControladorSubsistema_Talleres;

/**
 * Punto de entrada principal del sistema CODE4HOPE.
 *
 * Integra los tres subsistemas disponibles a traves de un menu central:
 *   - Subsistema de Alimentos y Medicamentos
 *   - Subsistema de Patrullas
 *   - Subsistema de Talleres de Formacion
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Main {

    // =========================================================================
    // PRESENTACION DEL MENU PRINCIPAL
    // =========================================================================

    /**
     * Imprime en consola el menu central de seleccion de subsistemas.
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("\n============================================================");
        System.out.println("         CODE4HOPE - Sistema de Gestion Integral            ");
        System.out.println("============================================================");
        System.out.println("  Seleccione el subsistema al que desea acceder:");
        System.out.println();
        System.out.println("  1. Subsistema de Alimentos y Medicamentos");
        System.out.println("  2. Subsistema de Patrullas");
        System.out.println("  3. Subsistema de Talleres de Formacion");
        System.out.println("  0. Salir del sistema");
        System.out.println("------------------------------------------------------------");
        System.out.print("  Opcion: ");
    }

    // =========================================================================
    // METODO PRINCIPAL
    // =========================================================================

    /**
     * Metodo principal del sistema.
     * Presenta el menu central y delega en el controlador correspondiente
     * segun la opcion elegida por el usuario.
     *
     * @param args Argumentos de linea de comandos (no se utilizan)
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        int opcion = -1;

        System.out.println();
        System.out.println("  ============================================================");
        System.out.println("         Bienvenido al Sistema de Gestion CODE4HOPE           ");
        System.out.println("  ============================================================");

        while (!salir) {

            mostrarMenuPrincipal();
            try {
                opcion = sc.nextInt();
                sc.nextLine();
            } catch (NumberFormatException e) {
                System.out.println("  Entrada no valida. Introduzca un numero.");
            }

            switch (opcion) {

                case 1:
                    System.out.println(" >> Accediendo al Subsistema de Alimentos y Medicamentos...");
                    try {
                        ControladorSubsistemaAlimMed.iniciarSubsistemaAlimMed();
                    } catch (Exception e) {
                        System.out.println("  Error inesperado en el subsistema de Alimentos y Medicamentos.");
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    System.out.println(" >> Accediendo al Subsistema de Patrullas...");
                    ControladorSubsistemaPatrullas.iniciarSubsistemaPatrullas();
                    break;

                case 3:
                    System.out.println(" >> Accediendo al Subsistema de Talleres de Formacion...");
                    try {
                        ControladorSubsistema_Talleres.main(new String[0]);
                    } catch (Exception e) {
                        System.out.println("  Error inesperado en el subsistema de Talleres.");
                        e.printStackTrace();
                    }
                    break;

                case 0:
                    salir = true;
                    break;

                default:
                    System.out.println("  Opcion no valida. Elija entre 1 y 3, o 0 para salir.");
                    break;
            }
        }

        System.out.println("\n  El sistema ha finalizado. Hasta pronto.");
        sc.close();
    }
}
