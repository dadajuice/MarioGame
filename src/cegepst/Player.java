package cegepst;

import cegepst.engine.Buffer;
import cegepst.engine.controls.MovementController;
import cegepst.engine.entity.ControllableEntity;

import java.awt.*;

public class Player extends ControllableEntity {

    private GamePad gamePad;

    public Player(GamePad gamePad) {
        super(gamePad);
        this.gamePad = gamePad;
        super.setDimension(30, 30);
        super.setSpeed(2);
    }

    @Override
    public void update() {
        super.update();
        if (gamePad.isJumpPressed()) {
            super.startJump();
        }
    }

    @Override
    public void draw(Buffer buffer) {
        buffer.drawRectangle(x, y, width, height, Color.GREEN);
        drawHitBox(buffer);
    }
}
