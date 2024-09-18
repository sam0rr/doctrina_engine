package doctrina;

import java.util.concurrent.TimeUnit;

public class GameTime {

    private static final int FPS_TARGET = 60;
    private long syncTime;
    private static int currentFPS;
    private static int fpsCount;
    private static long fpsTimeDelta;
    private static long gameStartTime;

    protected GameTime(){
        updateSynctime();
        gameStartTime = System.currentTimeMillis();
        fpsTimeDelta = 0;
        currentFPS = 0;
    }

    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }

    public static int getCurrentFPS(){
        return (currentFPS > 0) ? currentFPS : fpsCount;
    }

    public static long getElapsedTime(){
        return System.currentTimeMillis() - gameStartTime;
    }

    public static String getElapsedFormattedTime(){
        long time = getElapsedTime();
        long hours = TimeUnit.MILLISECONDS.toHours(time);
        time -= TimeUnit.MILLISECONDS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void syncronise(){
        update();
        try {
            Thread.sleep(getSleepTime());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        updateSynctime();
    }

    private void update(){
        fpsCount++;
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(getElapsedTime());
        if(fpsTimeDelta != currentSecond){
            currentFPS = fpsCount;
            fpsCount = 0;
        }
        fpsTimeDelta = currentSecond;
    }

    private long getSleepTime(){
        long targetTime = 1000l / FPS_TARGET;
        long sleep = targetTime - (System.currentTimeMillis() - syncTime);
        if (sleep < 0) {
            sleep = 4;
        }
        return sleep;
    }

    private void updateSynctime() {
        syncTime = System.currentTimeMillis();
    }

}