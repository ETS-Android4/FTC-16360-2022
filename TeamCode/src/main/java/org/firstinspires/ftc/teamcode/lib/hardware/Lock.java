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

    Servo lock;


    public State state = State.NEUTRAL;
    private double lock_neutral = 0.5;
    private double lock_left = 0;
    private double lock_right = 1;
    private double lock_intaking;
    public State depositDirection = State.LEFT;

    public Lock(HardwareMap hardwareMap) {
        lock   = hardwareMap.get(Servo.class, "lock");

        //Get Intake Side from Globals
        if (Globals.IntakeSide == Globals.Side.LEFT) {
            lock_intaking = lock_right;
        } else {
            lock_intaking = lock_right;
        }
    }

    public void update(){
        switch (state) {
            case LEFT:
                lock.setPosition(lock_left);
                break;
            case RIGHT:
                lock.setPosition(lock_right);
                break;
            case NEUTRAL:
                lock.setPosition(lock_neutral);
                break;
            case INTAKING:
                lock.setPosition(lock_intaking);
                break;
        }
    }
}

