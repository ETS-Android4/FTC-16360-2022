package org.firstinspires.ftc.teamcode.lib.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.Globals;

public class Lock {

    public enum State {
        LEFT,
        RIGHT,
        NEUTRAL,
        INTAKING
    }

    public Servo servo;


    public State state = State.NEUTRAL;
    private double lock_neutral = 0.5;
    private double lock_left = 1;
    private double lock_right = 0;
    private double lock_intaking;
    public State depositDirection = State.LEFT;

    public Lock(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "lock");

        //Get Intake Side from Globals
        /*
        if (Globals.IntakeSide == Globals.Side.LEFT) {
            lock_intaking = lock_right;
        } else {
            lock_intaking = lock_right;
        }*/
        lock_intaking = 0.6;
    }

    public void update(){
        switch (state) {
            case LEFT:
                servo.setPosition(lock_left);
                break;
            case RIGHT:
                servo.setPosition(lock_right);
                break;
            case NEUTRAL:
                servo.setPosition(lock_neutral);
                break;
            case INTAKING:
                servo.setPosition(lock_intaking);
                break;
        }
    }
}

