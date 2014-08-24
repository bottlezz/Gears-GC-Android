package info.GearsGC.gearsgamecenter.app;

/**
 * Created by luna on 2014-08-17.
 */
public class Game {
    String name;
    String info;
    boolean started;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public boolean isStarted(){return started;}
    public void setGameStart(){started = true;}
    public void setGameStop(){started = false;}

    public Game(String name, String info) {
        super();
        this.name = name;
        this.info = info;
        started = false;
    }

}
