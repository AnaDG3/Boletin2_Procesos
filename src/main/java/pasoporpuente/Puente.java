package pasoporpuente;

/*
 * programa que controle el paso de personas por un puente,
 * siempre en la misma dirección, para que se cumplan las
 * siguientes restricciones.
 *
 * No pueden pasar más de cuatro personas a la vez,
 * ni más de tres en cada sentido.
 */

class Puente {  // Estado, objeto compartido entre los hilos

    private static final int PESO_MAXIMO = 200;
    private static final int MAX_PERSONAS = 4;
    private static final int MAX_PERSONAS_SENTIDO = 3;

    private int peso = 0;
    private int numPersonas = 0;

    // 0 = vacío, 1 = derecha, -1 = izquierda
    private int sentidoActual = 0;

    private int personasDerecha = 0;
    private int personasIzquierda = 0;

    synchronized public int getPeso() {
        return peso;
    }

    synchronized public int getNumPersonas() {
        return numPersonas;
    }

    synchronized public boolean autorizacionPaso(Persona persona) {

        boolean result;

        if ((this.sentidoActual == 0 ||
                this.sentidoActual == persona.getSentido())
                && this.peso + persona.getPeso() <= Puente.PESO_MAXIMO
                && this.numPersonas < Puente.MAX_PERSONAS
                && (persona.getSentido() == 1
                ? this.personasDerecha < Puente.MAX_PERSONAS_SENTIDO
                : this.personasIzquierda < Puente.MAX_PERSONAS_SENTIDO)) {

            this.sentidoActual = persona.getSentido();

            this.numPersonas++;
            this.peso += persona.getPeso();

            if (persona.getSentido() == 1) {
                this.personasDerecha++;
            } else {
                this.personasIzquierda++;
            }

            result = true;

        } else {
            result = false;
        }

        return result;
    }

    synchronized public void terminaPaso(Persona persona) {

        this.peso -= persona.getPeso();
        this.numPersonas--;

        if (persona.getSentido() == 1) {
            this.personasDerecha--;
        } else {
            this.personasIzquierda--;
        }

        // Si el puente queda vacío, puede cambiar de sentido
        if (this.numPersonas == 0) {
            this.sentidoActual = 0;
        }
    }
}