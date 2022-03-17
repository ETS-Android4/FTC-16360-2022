package org.firstinspires.ftc.teamcode.lib.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    enum State {
        ON,
        OFF,
        REVERSE,
        REVERSE_BOOST,
        BOOST
    }

    DcMotorEx motorFront;
    DcMotorEx motorRear;

    DcMotorEx singleMotor;
    public Servo singleServo;

    Servo rear;
    Servo front;
    final double speedF = 0.9;
    final double speedR = 0.4;
    private double speed = 0;
    public boolean boost = false;
    public boolean manualReverse = false;
    private DcMotorSimple.Direction direction = DcMotorSimple.Direction.FORWARD;
    State state;

    public Intake(HardwareMap hardwareMap) {
        motorFront = hardwareMap.get(DcMotorEx.class, "intakeFront");
        motorRear = hardwareMap.get(DcMotorEx.class, "intakeRear");
        rear = hardwareMap.get(Servo.class, "intakeRear");
        front = hardwareMap.get(Servo.class, "intakeFront");

        singleServo = rear;
        singleMotor = motorRear;
        state = State.OFF;

    }

    public void enable() {
        state = State.ON;
    }

    public void deploy() {singleServo.setPosition(0.67);}
    public void retract() {singleServo.setPosition(0.86);}

    public void disable() {
        state = State.REVERSE;
    }

    public void kill() { state = State.OFF; }


    public void toggleState() {
        if (state == State.ON) {
            state = State.REVERSE;
        } else {
            state = State.ON;
        }
    }

    public void update() {
        switch (state) {
            case ON:
                speed = speedF;
                direction = DcMotorSimple.Direction.FORWARD;
                break;
            case REVERSE:
                speed = speedR;
                direction = DcMotorSimple.Direction.REVERSE;
                break;
            case OFF:
                singleMotor.setPower(0);
                 break;
        }
        if (boost) {
            speed = 1;
        }
        if (manualReverse) {
            direction = DcMotorSimple.Direction.REVERSE;
        }
        motorRear.setPower(speed);
        motorRear.setDirection(direction);
    }
}
