package org.eda2.practica01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class NbaStats {
	
	public static ArrayList<Player> jugadores = new ArrayList<Player>();

	public ArrayList<Player> getJugadores() {
		return jugadores;
	}
	
	
	private static String directorio = ("E:\\Archivos de programa (x86)\\Elcipse\\WORKSPACES\\UnNombreAdecuado\\EDA2\\src\\org\\eda2\\practica01\\NbaStats.csv");
	
	public static void main(String[] args) {
		NbaStats nba = new NbaStats();
		long Tinicial = 0, Tfinal = 0, Ttotal = 0;
		int topNjugadores = 10; 
		cargarArchivo(directorio);
		
		for (int i = 0; i < 10; i++) {
			Tinicial = System.nanoTime();
			NbaStats.mergeSort(nba.getJugadores());
			Tfinal = System.nanoTime();
			Ttotal += Tfinal - Tinicial;
		}
			
		System.out.println("**** Tiempo medio 10 ejecuciones: " +(Ttotal / 10) /  Math.pow(10, 6)+ " ms ****");
		System.out.println("Los "+topNjugadores+" mejores jugadores son: ");
		System.out.println("-------------------------------------------------");
		
		for (int i = nba.getJugadores().size() - 1; i >= nba.getJugadores().size() - topNjugadores; i--) {
			System.out.println(nba.getJugadores().get(i).toString() + "\n");
		}	
			
	}
	
	
	//Esquema DyV
	public static ArrayList<Player> mergeSort(ArrayList<Player> array) {
		ArrayList<Player> izquierda = new ArrayList<Player>();
	    ArrayList<Player> derecha = new ArrayList<Player>();
	    int centro;
	    //caso base
	    if (array.size() == 1) {
	    	return array;
	    } else {
	    	centro = array.size() / 2; 
	        for (int i = 0; i < centro; i++) {
	        	izquierda.add(array.get(i));
	        }
	        for (int i = centro; i < array.size(); i++) {
	        	derecha.add(array.get(i));
	        }
	        izquierda = mergeSort(izquierda);
	        derecha = mergeSort(derecha);

	        merge(izquierda, derecha, array);
	    }
	    return array;
	}
	
	private static void merge(ArrayList<Player> izquierda, ArrayList<Player> derecha, ArrayList<Player> array) {
		int indiceIzquierda = 0;
	    int indiceDerecha = 0;
	    int indiceOrigen = 0;
	
	    while (indiceIzquierda < izquierda.size() && indiceDerecha < derecha.size()) {
	    	if ((izquierda.get(indiceIzquierda).compareTo(derecha.get(indiceDerecha))) < 0) {
	    		array.set(indiceOrigen, izquierda.get(indiceIzquierda));
	            indiceIzquierda++;
	        } else {
	        	array.set(indiceOrigen, derecha.get(indiceDerecha));
	                indiceDerecha++;
	        }
	            indiceOrigen++;
	    }
	
	    ArrayList<Player> resto;
	    int indiceResto;
	    if (indiceIzquierda >= izquierda.size()) {
	    	resto = derecha;
	        indiceResto = indiceDerecha;
	    } else {
	    	resto = izquierda;
	        indiceResto = indiceIzquierda;
	    }
	    for (int i = indiceResto; i < resto.size(); i++) {
	    	array.set(indiceOrigen, resto.get(i));
	        indiceOrigen++;
	    }
    }
	
	//Metodo para cargar NbaStats.csv
	
	public static void cargarArchivo(String archivo) {
		try {
			Scanner scan = new Scanner(new File(archivo));
			String line;
			String[] items;	
			
			// [2]PlayerName, [4]Pos , [6]Tm , [7]FG% , [8]PTS	
			
			while (scan.hasNextLine()) {
				line = scan.nextLine().trim();
				if (line.isEmpty()|| line.startsWith("#") || line.contains("%"))
					continue;
				items = line.split(";");
				
				if(jugadores.size() == 0) {		
					jugadores.add(new Player(items[2], items[6], items[4], 
							(int)Double.parseDouble(items[7].isEmpty() ? "0.0" : items[7].replace(",", "."))*Integer.parseInt(items[8].isEmpty() ? "0" : items[8] )/100));
					continue;
				}
				
				if(jugadores.get(jugadores.size()-1).getPlayerName().equals(items[2])){

					Player ultimoJug = jugadores.get(jugadores.size()-1);
					
					if(!ultimoJug.getTeams().contains(items[6])) ultimoJug.getTeams().add(items[6]);
					if(!ultimoJug.getPositions().contains(items[4])) ultimoJug.getPositions().add(items[4]);
					int score = (int) (Double.parseDouble(items[7].isEmpty() ? "0" : items[7].replace(",", "."))) * (Integer.parseInt(items[8].isEmpty() ? "0" : items[8])) / 100;
					
					ultimoJug.setScore((ultimoJug.getScore()+score)/2);
					
				} else{
					jugadores.add(new Player(items[2], items[6], items[4], 
							(int)Double.parseDouble(items[7].isEmpty() ? "0.0" : items[7].replace(",", "."))*Integer.parseInt(items[8].isEmpty() ? "0" : items[8] )/100));
				}			
			}
			scan.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

}
