package cegepst;

import cegepst.engine.Buffer;
import cegepst.engine.Game;
import cegepst.engine.entity.StaticEntity;

import java.util.ArrayList;

public class TankGame extends Game {

    private Player player;
    private GamePad gamePad;
    private ArrayList<Brick> bricks;

    public TankGame() {
        gamePad = new GamePad();
        player = new Player(gamePad);
        player.teleport(245, 10);
        bricks = new ArrayList<>();
        bricks.add(new Brick(250, 250));
        bricks.add(new Brick(500, 250));
    }

    @Override
    public void initialize() {

    }

    @Override
    public void conclude() {

    }

    @Override
    public void update() {
        player.update();
        if (gamePad.isQuitPressed()) {
            super.stop();
        }
    }

    @Override
    public void draw(Buffer buffer) {
        for (StaticEntity entity : bricks) {
            entity.draw(buffer);
        }
        player.draw(buffer);
    }
}
