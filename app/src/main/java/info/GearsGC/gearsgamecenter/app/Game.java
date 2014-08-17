package info.GearsGC.gearsgamecenter.app;

/**
 * Created by luna on 2014-08-17.
 */
public class Game {
    String name;
    String info;

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

    public Game(String name, String info) {
        super();
        this.name = name;
        this.info = info;
    }

}
