package Entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

import java.security.Key;

public class Player extends Entity{
    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;

    private static final float TERRAIN_HEIGHT = 0;

    private float currentSpeed = 0;
    private float currentSideSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(Terrain terrain){
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));

        float sideDistance = currentSideSpeed * DisplayManager.getFrameTimeSeconds();
        float dx2 = (float) (sideDistance * Math.sin(Math.toRadians(super.getRotY()+90)));
        float dz2 = (float) (sideDistance * Math.cos(Math.toRadians(super.getRotY()+90)));

        super.increasePosition(dx + dx2, 0, dz + dz2);
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);

        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        if(super.getPosition().y < terrainHeight){
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
    }

    private void jump(){
        if(!isInAir) {
            upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = RUN_SPEED;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = -RUN_SPEED;
        } else{
            this.currentSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentSideSpeed = RUN_SPEED;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentSideSpeed = -RUN_SPEED;
        } else{
            this.currentSideSpeed = 0;
        }

        increaseRotation(0, -Mouse.getDX() * 0.05f, 0);


        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            this.currentSpeed *= 2.8;
        }
    }
}
