public class Test implements Runnable {

    @Override
    public void run() {
        for(int i = 0; i<9999; i++){
            System.out.println(i);
        }
    }
}
