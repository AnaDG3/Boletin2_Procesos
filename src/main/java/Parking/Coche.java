package Parking;

import java.util.concurrent.TimeUnit;

class Coche implements Runnable {

    String matricula;
    Parking parking;

    int aleCirculando = (int)(Math.random() * 5);
    int aleParking = 1000 + (int)(Math.random() * 2001);
    int aleEspera = 10 + (int)(Math.random() * 11);

    public Coche(String matricula, Parking parking) {
        this.matricula = matricula;
        this.parking = parking;
    }

    @Override
    public void run() {

        try {

            // El coche comienza circulando hasta llegar al parking
            System.out.println(matricula + " está circulando por la ciudad");

            Thread.sleep(aleCirculando * 1000);

            System.out.println(matricula + " llega al parking e intenta entrar");

            // Espera para conseguir autorización
            boolean conseguido = parking.entrarCoche(matricula, aleEspera);

            if (conseguido) {

                System.out.println(matricula + " está aparcado");

                Thread.sleep(aleParking);

                // Sale del parking
                parking.salirCoche(matricula);

            } else {

                System.out.println(matricula +
                        " abandona el parking porque no ha conseguido plaza");

            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(matricula + " ha sido interrumpido");
        }
    }
}
