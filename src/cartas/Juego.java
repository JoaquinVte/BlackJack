package cartas;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Juego {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char[] palo = { '♣', '♠', '♥', '♦' };
		// char[] palo = { "\u2661".charAt(0), "\u2662".charAt(0),
		// "\u2664".charAt(0), "\u2667".charAt(0) };
		char[] valor = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'J', 'Q', 'K' };
		double puntosJugador = 0, puntosMaquina = 0;

		Deque<Carta> baraja = new LinkedList<Carta>();

		for (char p : palo) {
			for (char v : valor) {
				baraja.add(new Carta(v, p));
			}
		}

		Collections.shuffle((List<?>) baraja);

		puntosJugador = jugador(baraja);

		System.out.println("\n\nAhora me toca a mi.... preparate a perder");
		puntosMaquina = maquina(puntosJugador, baraja);

		if (puntosJugador == 0) {
			System.out.println("Has perdido!");
		} else if (puntosMaquina == 0) {
			System.out.println("Has ganado !!!");
		} else if (puntosJugador > puntosMaquina) {
			System.out.println("Has ganado !!!");
		} else {
			System.out.println("Has perdido!");
		}
		
		Sonido(new File("./src/sonidos/game-over.wav"));
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static double maquina(double puntosJugador, Deque<Carta> baraja) {
		double puntosMaquina = 0;
		List<Carta> mano = new LinkedList<Carta>();

		while (puntosMaquina < puntosJugador) {
			System.out.println("Quiero otra carta \u261D");
			mano.add(baraja.pop());
			puntosMaquina = mano.stream().collect(Collectors.summingDouble(Carta::getValorJuego));
			for (Carta c : mano)
				System.out.print(c + "  ");

			System.out.println("Puntos: " + puntosMaquina);

			if (puntosMaquina > 21) {
				System.out.println("Me he pasado \u2639");
				Sonido(new File("./src/sonidos/fallo.wav"));
			} else {
				Sonido(new File("./src/sonidos/acierto.wav"));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		if (puntosMaquina <= 21){
			System.out.println("Me planto \u263A");
			
		}

		return (puntosMaquina > 21) ? 0 : puntosMaquina;
	}

	public static double jugador(Deque<Carta> baraja) {
		List<Carta> mano = new ArrayList<Carta>();
		double puntos = 0;
		char opcion;
		Scanner entrada = new Scanner(System.in);
		System.out.println("Quieres carta? [s/n]");
		opcion = entrada.nextLine().charAt(0);

		while (puntos < 21 && opcion != 'n') {

			mano.add(baraja.pop());
			puntos = mano.stream().collect(Collectors.summingDouble(Carta::getValorJuego));
			for (Carta c : mano)
				System.out.print(c + "  ");

			System.out.println("Puntos: " + puntos);

			if (puntos > 21) {
				System.out.println("Te has pasado \u2639");
				Sonido(new File("./src/sonidos/fallo.wav"));
			} else {
				Sonido(new File("./src/sonidos/acierto.wav"));
				System.out.println("Quieres otra carta? [s/n]");
				opcion = entrada.nextLine().charAt(0);
			}
		}
		if(opcion=='n')System.out.println("La siguiente carta era ..." +baraja.peek());
		return (puntos > 21) ? 0 : puntos;
	}

	public static void Sonido(File f) {
		Clip sonido;
		try {
			sonido = AudioSystem.getClip(null);

			AudioInputStream ais;
			ais = AudioSystem.getAudioInputStream(f);

			sonido.open(ais);
			sonido.start();
			

		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class Carta implements Comparable {
	private char valor;
	private char palo;

	public Carta(char valor, char palo) {
		super();
		this.valor = valor;
		this.palo = palo;
	}

	/**
	 * @return the valor
	 */
	public float getValorJuego() {
		switch (valor) {
		case ('J'):
		case ('Q'):
		case ('K'):
			return 0.5f;
		default:
			return Float.valueOf(String.valueOf(valor));
		}

	}

	/**
	 * @return the valor
	 */
	public char getValor() {
		return valor;
	}

	/**
	 * @return the palo
	 */
	public char getPalo() {
		return palo;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(char valor) {
		this.valor = valor;
	}

	/**
	 * @param palo
	 *            the palo to set
	 */
	public void setPalo(char palo) {
		this.palo = palo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return valor + " " + palo;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		int p = ((Carta) o).getPalo() - palo;
		if (p != 0)
			return p;
		else
			return ((Carta) o).getValor() - valor;
	}

}