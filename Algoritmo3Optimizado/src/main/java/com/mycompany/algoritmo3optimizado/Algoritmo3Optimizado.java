/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.algoritmo3optimizado;

/**
 *
 * @author franc
 */
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Algoritmo3Optimizado {

    public static List<String> generarSecuenciasTransformadas(String secuenciaEntrada) {
        int largoSecuencia = secuenciaEntrada.length();
        int[] numeros = new int[(int) Math.pow(2, largoSecuencia)];
        for (int i = 0; i < Math.pow(2, largoSecuencia); i++) {
            numeros[i] = i;
        }

        List<String> secuenciasTransformadas = new ArrayList<>();

        for (int num : numeros) {
            String representacionBinaria = String.format("%" + largoSecuencia + "s", Integer.toBinaryString(num)).replace(' ', '0');
            StringBuilder nuevaSecuencia = new StringBuilder();
            for (int idx = 0; idx < largoSecuencia; idx++) {
                char caracter = secuenciaEntrada.charAt(idx);
                nuevaSecuencia.append((representacionBinaria.charAt(idx) == '1') ? 'X' : caracter);
            }

            int contadorX = 0;
            StringBuilder nuevaSecuenciaConNumeros = new StringBuilder();
            for (int i = 0; i < nuevaSecuencia.length(); i++) {
                char caracter = nuevaSecuencia.charAt(i);
                if (caracter == 'X') {
                    contadorX++;
                } else {
                    if (contadorX > 0) {
                        nuevaSecuenciaConNumeros.append(contadorX);
                        contadorX = 0;
                    }
                    nuevaSecuenciaConNumeros.append(caracter);
                }
            }
            if (contadorX > 0) {
                nuevaSecuenciaConNumeros.append(contadorX);
            }

            StringBuilder nuevaSecuenciaFinal = new StringBuilder();
            for (int i = 0; i < nuevaSecuenciaConNumeros.length(); i++) {
                char caracter = nuevaSecuenciaConNumeros.charAt(i);
                if (Character.isDigit(caracter)) {
                    int valorContador = Integer.parseInt(String.valueOf(caracter));
                    if (valorContador > 0 && i > 0 && i < nuevaSecuenciaConNumeros.length() - 1 &&
                            !Character.isDigit(nuevaSecuenciaConNumeros.charAt(i - 1)) &&
                            !Character.isDigit(nuevaSecuenciaConNumeros.charAt(i + 1))) {
                        nuevaSecuenciaFinal.append(caracter);
                    }
                } else {
                    nuevaSecuenciaFinal.append(caracter);
                }
            }

            secuenciasTransformadas.add(nuevaSecuenciaFinal.toString());
        }

        return secuenciasTransformadas;
    }
    
    public static List<String> compararTransformaciones(List<String> lista1, List<String> lista2) {
        Set<String> set1 = new HashSet<>(lista1);
        Set<String> set2 = new HashSet<>(lista2);
        List<String> listaPatrones = new ArrayList<>();

        for (String subsequence1 : set1) {
            if (set2.contains(subsequence1)) {
                // Secuencias idénticas
                String resultado = formatIdenticalSequence(subsequence1);
                listaPatrones.add(resultado);
            } else {
                for (String subsequence2 : set2) {
                    if (sequencesWithoutNumbersEqual(subsequence1, subsequence2)) {
                        // Secuencias similares (sin números)
                        String resultado = compareSequences(subsequence1, subsequence2);
                        listaPatrones.add(resultado);
                    }
                }
            }
        }

        return listaPatrones;
    }

    private static String formatIdenticalSequence(String sequence) {
        StringBuilder nuevaSubsecuencia = new StringBuilder();
        int len = sequence.length();
        for (int j = 0; j < len; j++) {
            char char1 = sequence.charAt(j);
            if (Character.isLetter(char1)) {
                if (j != 0) {
                    nuevaSubsecuencia.append("-").append(char1);
                } else {
                    nuevaSubsecuencia.append(char1);
                }
            } else if (Character.isDigit(char1)) {
                nuevaSubsecuencia.append("-X").append("(").append(char1).append(")");
            }
        }
        return nuevaSubsecuencia.toString();
    }

    private static boolean sequencesWithoutNumbersEqual(String seq1, String seq2) {
        String seq1WithoutNumbers = seq1.replaceAll("\\d", "");
        String seq2WithoutNumbers = seq2.replaceAll("\\d", "");
        return seq1WithoutNumbers.equals(seq2WithoutNumbers);
    }

    private static String compareSequences(String seq1, String seq2) {
        StringBuilder nuevaSubsecuencia = new StringBuilder();
        int len = Math.min(seq1.length(), seq2.length());
        int contadorX = 0;

        for (int j = 0; j < len; j++) {
            char char1 = seq1.charAt(j);
            char char2 = seq2.charAt(j);

            if (Character.isLetter(char1) && Character.isLetter(char2)) {
                if (char1 == char2) {
                    if (contadorX > 0) {
                        nuevaSubsecuencia.append("-X(").append(contadorX).append(")");
                        contadorX = 0;
                    }
                    if (j != 0) {
                        nuevaSubsecuencia.append("-").append(char1);
                    } else {
                        nuevaSubsecuencia.append(char1);
                    }
                } else {
                    contadorX++;
                }
            } else if (Character.isDigit(char1) && Character.isDigit(char2)) {
                if (char1 == char2) {
                    if (contadorX > 0) {
                        nuevaSubsecuencia.append("-X(").append(contadorX).append(")");
                        contadorX = 0;
                    }
                    nuevaSubsecuencia.append("-X(").append(char1).append(")");
                } else {
                    int num1 = Character.getNumericValue(char1);
                    int num2 = Character.getNumericValue(char2);
                    int menor = Math.min(num1, num2);
                    int mayor = Math.max(num1, num2);
                    nuevaSubsecuencia.append("-X(").append(menor).append(",").append(mayor).append(")");
                }
            } else {
                contadorX++;
            }
        }

        if (contadorX > 0) {
            nuevaSubsecuencia.append("-X(").append(contadorX).append(")");
        }

        return nuevaSubsecuencia.toString();
    }

    public static List<Integer> sumaStrings(List<String> listaStrings, int len) {
        List<Integer> resultados = new ArrayList<>();
        
        for (String s : listaStrings) {
            Pattern pattern = Pattern.compile("X\\((\\d+)(?:,(\\d+))?\\)");
            Matcher matcher = pattern.matcher(s);

            int suma = 0;

            while (matcher.find()) {
                int valor1 = Integer.parseInt(matcher.group(1));
                int valor2 = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : valor1;
                suma += Math.max(valor1, valor2);
            }

            for (char c : s.toCharArray()) {
                if (Character.isLetter(c) && c != 'X') {
                    suma += len;
                }
            }

            resultados.add(suma);
        }

        return resultados;
    }
    
    public static List<String> ordenarListaConNumeros(List<String> listaStrings, List<Integer> listaNumeros) {
        List<String> listaOrdenada = new ArrayList<>();
        
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < listaStrings.size(); i++) {
            indices.add(i);
        }

        Collections.sort(indices, Comparator.comparingInt(listaNumeros::get).reversed());

        for (int indice : indices) {
            listaOrdenada.add(listaStrings.get(indice));
        }

        return listaOrdenada;
    }

    public static void main(String[] args) {
        //List<String> secuenciasLista1 = Arrays.asList("ASKJF", "ASUUB", "ABCDE", "LMNOP", "FGFYY", "ZFCVBN", "ABCDE", "FGHIJ", "KLMNO", "PQRST", "UVWXY", "ABCDE", "FGHIJ", "KLMNO", "STPQR", "UVWXY", "ABCDE", "IFGHJ", "NOKLM", "PQSRT", "UVVWY", "ABCDE", "FGHIJ", "OKLMN", "PQRST", "UVWXY", "PORUYT", "ASKASD", "MNBOIU", "QWERAS", "ASDSEFG", "DFFKLMN", "OERZNCC", "VWXYZAB", "CDEFGHI", "ASDFJKF");
        //List<String> secuenciasLista2 = Arrays.asList("AJKIU", "ASIJK", "ABNME", "CCNIP", "VWXYY", "ZADBNO", "ABXYZ", "FXYIZ", "KXYIZ", "PXYIZ", "UXYIZ", "ABCXZ", "FGHXZ", "KLMXZ", "PQRXZ", "UVWXZ", "XYABC", "FGHXY", "KLMXY", "PQXRY", "UVCWY", "ABCDY", "YFGHI", "YKLMN", "PQRYT", "UVWXY", "POIUKJ", "POKLSD", "MHCEIU", "QWISAS", "ABCDEFG", "HIJKLON", "OPRSDAS", "OPVFZAB", "CQZAQZI", "JKFAHFG");
        
        /*
        
          // Valores para la lista 1
        List<String> secuenciasLista1 = Arrays.asList(
            "GXR",
            "SDDD",
            "SDKES",
            "GEGYRA",
            "KEGHISV",
            "SHDGDAGQ",
            "KEGHIDJFH",
            "GAVLIDHFJK",
            "VALIMFYWKRQ",
            "GAVLIMFYWKRQ",
            "VALIMFYWKRQNE",
            "RQNEGHILSTYRAG",
            "VALIMFYWKRQNEG",
            "RQNEGHILSTDYRAGQ"
        );

        // Valores para la lista 2
        List<String> secuenciasLista2 = Arrays.asList(
            "GSR",
            "ADCG",
            "QNKEF",
            "METYRA",
            "KEGHISV",
            "METYRAGQ",
            "KEGHISVAL",
            "GAVLIMFYWK",
            "VALIMFYWKRQ",
            "DHMUYVTIPQIPX",
            "YCIPMXDXQIMSWA",
            "SQFHBEDEFXAPFA",
            "EQXXYIPFGODPZFOU",
            "YSNBXIZCIDSLNUYET"
        );
        
        */
        
        //experimento 2.1 
        //List<String> secuenciasLista1 = Arrays.asList("CSDFGAYCKEKGHWAKJHDC");
        //List<String> secuenciasLista2 = Arrays.asList("CFABCNCGKEGHIAOPKNC");
        
        //experimento 3.1
        List<String> secuenciasLista1 = Arrays.asList("CAYCKEKGHWAKDC");
        List<String> secuenciasLista2 = Arrays.asList("CFNCGKEGHIAKNC");
        
        List<String> lista1 = new ArrayList<>();
        List<String> lista2 = new ArrayList<>();

        for (String secuencia : secuenciasLista1) {
            lista1.addAll(generarSecuenciasTransformadas(secuencia));
        }

        for (String secuencia : secuenciasLista2) {
            lista2.addAll(generarSecuenciasTransformadas(secuencia));
        }

        for (int i = 0; i < secuenciasLista1.size(); i++) {
            long startTime = System.nanoTime();

            List<String> lista1Sec = generarSecuenciasTransformadas(secuenciasLista1.get(i));
            List<String> lista2Sec = generarSecuenciasTransformadas(secuenciasLista2.get(i));

            List<String> listaPatrones = compararTransformaciones(lista1Sec, lista2Sec);

            int len = Math.max(secuenciasLista1.get(i).length(), secuenciasLista2.get(i).length());

            List<Integer> resultados = sumaStrings(listaPatrones, len);
            List<String> listaOrdenada = ordenarListaConNumeros(listaPatrones, resultados);

            long endTime = System.nanoTime();

            long duration = (endTime - startTime) / 1000000; // Convertir a milisegundos

            System.out.println("Patrón final para el par (" + secuenciasLista1.get(i) + ", " + secuenciasLista2.get(i) + "): " + listaOrdenada.get(0)+"  "+ duration + " ms");
            //System.out.println("Tiempo para encontrar el patrón: " + duration + " ms");
        }
    }
}
