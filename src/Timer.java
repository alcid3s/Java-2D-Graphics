public class Timer {
    private long millis;
    private final int delay;


    public Timer(int delay) {
        this.delay = delay;
        this.millis = System.currentTimeMillis() + this.delay;
    }

    public long getDelay() {
        return millis;
    }

    public void resetTimer() {
        this.millis = System.currentTimeMillis() + this.delay;
    }
}
