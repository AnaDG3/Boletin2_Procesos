package Parking;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class Parking {

    private static final int NUM_PLAZAS = 20;

    private String[] plazas = new String[NUM_PLAZAS];

    // Semáforo para las plazas de coches
    private Semaphore semCoches = new Semaphore(10);

    // Semáforo para las plazas de furgonetas
    private Semaphore semFurgonetas = new Semaphore(5);

    // Semáforo para controlar la única entrada
    private Semaphore entrada = new Semaphore(1);

    public boolean entrarCoche(String matricula, int tiempoEspera)
            throws InterruptedException {

        if (semCoches.tryAcquire(tiempoEspera, TimeUnit.SECONDS)) {

            boolean asignado = false;

            try {
                entrada.acquire();

                try {
                    synchronized (this) {

                        for (int i = 0; i < 10; i++) {

                            if (plazas[i] == null) {

                                plazas[i] = matricula;
                                asignado = true;

                                System.out.println(matricula +
                                        " entra en la plaza " + (i + 1));

                                mostrarParking();

                                break;
                            }
                        }
                    }

                } finally {
                    entrada.release();
                }

            } finally {
                if (!asignado) {
                    semCoches.release();
                }
            }

            return asignado;
        }

        return false;
    }

    public boolean entrarFurgoneta(String matricula, int tiempoEspera)
            throws InterruptedException {

        if (semFurgonetas.tryAcquire(tiempoEspera, TimeUnit.SECONDS)) {

            boolean asignado = false;

            try {
                entrada.acquire();

                try {
                    synchronized (this) {

                        // Buscar dos plazas consecutivas libres
                        for (int i = 10; i < 20; i += 2) {

                            if (plazas[i] == null &&
                                    plazas[i + 1] == null) {

                                plazas[i] = matricula;
                                plazas[i + 1] = matricula;

                                asignado = true;

                                System.out.println(matricula +
                                        " entra en las plazas " +
                                        (i + 1) + " y " + (i + 2));

                                mostrarParking();

                                break;
                            }
                        }
                    }

                } finally {
                    entrada.release();
                }

            } finally {
                if (!asignado) {
                    semFurgonetas.release();
                }
            }

            return asignado;
        }

        return false;
    }

    public synchronized void salirCoche(String matricula) {

        for (int i = 0; i < 10; i++) {

            if (matricula.equals(plazas[i])) {

                plazas[i] = null;

                System.out.println(matricula +
                        " sale del parking");

                semCoches.release();

                mostrarParking();

                break;
            }
        }
    }

    public synchronized void salirFurgoneta(String matricula) {

        for (int i = 10; i < 20; i += 2) {

            if (matricula.equals(plazas[i])) {

                plazas[i] = null;
                plazas[i + 1] = null;

                System.out.println(matricula +
                        " sale del parking");

                semFurgonetas.release();

                mostrarParking();

                break;
            }
        }
    }

    public synchronized void mostrarParking() {

        System.out.println("ESTADO DEL PARKING");

        for (int i = 0; i < NUM_PLAZAS; i++) {

            if (plazas[i] == null) {
                System.out.println("Plaza " + (i + 1) + ": LIBRE");
            } else {
                System.out.println("Plaza " + (i + 1) + ": " + plazas[i]);
            }
        }
    }
}
