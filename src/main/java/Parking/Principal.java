package Parking;

class Principal {

    public static void main(String[] args) {

        Parking parking = new Parking();

        // Crear los 40 coches
        for (int x = 0; x < 40; x++) {

            String matricula = "COCHE-" + (x + 1);

            Coche hiloCoche = new Coche(matricula, parking);

            Thread th = new Thread(hiloCoche);

            th.start();
        }

        // Crear las 10 furgonetas
        for (int x = 0; x < 10; x++) {

            String matricula = "FURGO-" + (x + 1);

            Furgoneta hiloFurgoneta =
                    new Furgoneta(matricula, parking);

            Thread th = new Thread(hiloFurgoneta);

            th.start();
        }
    }
}