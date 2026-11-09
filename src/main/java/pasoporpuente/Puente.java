package pasoporpuente;

/**
 *
 * programa que controle el paso de personas por un puente, siempre en la misma
 * dirección, para que se cumplan las siguientes restricciones. No pueden pasar
 * más de tres personas a la vez, y no puede haber más de 200 kg de peso en
 * ningún momento. En un sistema real, se podría obtener la información de las
 * personas que llegan y de su peso mediante sensores. Para realizar la
 * simulación, se va a modelar las personas como hilos. El tiempo entre la
 * llegada de dos personas es aleatorio entre 1 y 30 segundos,
 * para pasar entre 10 y 50 segundos, y las personas tienen un peso aleatorio
 * entre 40 y 120 kg.
 *
 */
import java.util.Random;

class Puente {  // Estado, objeto compartido entre los hilos

    private static final int PESO_MAXIMO = 200;
    private static final int MAX_PERSONAS = 3;
    private int peso = 0;
    private int numPersonas = 0;

    synchronized public int getPeso() {
        return peso;
    }

    synchronized public int getNumPersonas() {
        return numPersonas;
    }


    synchronized public boolean autorizacionPaso(Persona persona) {

        boolean result;

        if (this.peso + persona.getPeso() <= Puente.PESO_MAXIMO && this.numPersonas < Puente.MAX_PERSONAS) {
            this.numPersonas++;
            this.peso += persona.getPeso();
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    synchronized public void terminaPaso(Persona persona) {
        this.peso -= persona.getPeso();
        this.numPersonas--;
    }
}

