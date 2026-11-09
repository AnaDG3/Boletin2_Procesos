package pasoporpuente;

import java.util.Random;

class Persona implements Runnable {

    private final Puente puente;

    private final String idPersona;
    private final int peso;
    private final int tMinPaso, tMaxPaso;

    // 1 = derecha, -1 = izquierda
    private final int sentido;

    public int getPeso() {
        return peso;
    }

    public int getSentido() {
        return sentido;
    }

    Persona(Puente puente, int peso, int tMinPaso,
            int tMaxPaso, String idP, int sentido) {

        this.puente = puente;
        this.peso = peso;
        this.tMinPaso = tMinPaso;
        this.tMaxPaso = tMaxPaso;
        this.idPersona = idP;
        this.sentido = sentido;
    }

    @Override
    public void run() {

        String direccion = (sentido == 1) ? "derecha" : "izquierda";

        System.out.printf("- %s de %d kg quiere cruzar hacia %s, en puente %d kg, %d persona%s.\n",
                this.idPersona, this.peso, direccion,
                puente.getPeso(), puente.getNumPersonas(),
                puente.getNumPersonas() != 1 ? "s" : "");

        // Espera para conseguir autorización
        boolean autorizado = false;
        while (!autorizado) {
            synchronized (this.puente) {
                autorizado = this.puente.autorizacionPaso(this);
                if (!autorizado) {
                    try {
                        System.out.printf("# %s debe esperar.\n", this.idPersona);
                        this.puente.wait();
                    } catch (InterruptedException ex) {
                        System.out.printf("Interrupción mientras %s espera para cruzar.\n", this.idPersona);
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }

        System.out.printf("> %s con peso %d puede cruzar hacia %s, puente soporta peso %d, con %d personas.\n",
                this.idPersona, this.getPeso(), direccion,
                this.puente.getPeso(), puente.getNumPersonas());

        // Pasa al puente, y tarda un tiempo en cruzar
        Random r = new Random();
        int tiempoPaso = this.tMinPaso + r.nextInt(this.tMaxPaso - this.tMinPaso + 1);
        try {
            System.out.printf("%s va a tardar tiempo %d en cruzar.\n", this.idPersona, tiempoPaso);
            Thread.sleep(tiempoPaso);
        } catch (InterruptedException ex) {
            System.out.printf("Interrupción mientras %s pasa.\n", this.idPersona);
            Thread.currentThread().interrupt();
        } finally {

            // Sale del puente
            synchronized (this.puente) {
                this.puente.terminaPaso(this);
                System.out.printf("< %s sale del puente, puente soporta peso %d, %d persona%s.\n",
                        this.idPersona, this.puente.getPeso(),
                        this.puente.getNumPersonas(),
                        this.puente.getNumPersonas() != 1 ? "s" : "");
                puente.notifyAll();
            }
        }
    }
}
