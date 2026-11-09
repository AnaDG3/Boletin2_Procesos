package Parking;

class Furgoneta implements Runnable {

    String matricula;
    Parking parking;

    int aleCirculando = (int)(Math.random() * 5);
    int aleParking = 1000 + (int)(Math.random() * 2001);
    int aleEspera = 10 + (int)(Math.random() * 11);

    public Furgoneta(String matricula, Parking parking) {
        this.matricula = matricula;
        this.parking = parking;
    }

    @Override
    public void run() {

        try {

            // La furgoneta comienza circulando hasta llegar al parking
            System.out.println(matricula + " está circulando por la ciudad");

            Thread.sleep(aleCirculando * 1000);

            System.out.println(matricula + " llega al parking e intenta entrar");

            // Espera para conseguir dos plazas
            boolean conseguido = parking.entrarFurgoneta(matricula, aleEspera);

            if (conseguido) {

                System.out.println(matricula + " está aparcada");

                Thread.sleep(aleParking);

                // Sale del parking
                parking.salirFurgoneta(matricula);

            } else {

                System.out.println(matricula +
                        " abandona el parking porque no ha conseguido dos plazas");

            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(matricula + " ha sido interrumpida");
        }
    }
}