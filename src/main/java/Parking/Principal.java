package Parking;

import java.util.concurrent.Semaphore;

class Principal {
    public static void main(String[] args) {
        Semaphore plaza= new Semaphore(5);
        for(int x=0;x<10;x++)
        {
            Coche hiloCoche= new Coche("coche numero: "+(x+1),plaza);
            Thread th= new Thread(hiloCoche);
            th.start();
        }
    }
}