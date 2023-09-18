package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(100, 5, -30);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;

    private Player player;

    public Camera(Player player){
        this.player = player;
    }

    public void move(){
        controllPitch();
        controllYaw();

        position.x = player.getPosition().x;
        position.z = player.getPosition().z;
        position.y = player.getPosition().y+5;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private float calculatedHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }
    private float calculatedVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }


    private void controllPitch(){
        pitch -= Mouse.getDY() * 0.05f;
    }

    private void controllYaw(){
        yaw = 180 - player.getRotY();
    }
}
