package org.firstinspires.ftc.teamcode.lib.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    enum State {
        ON,
        OFF
    }

    DcMotorEx motorFront;
    DcMotorEx motorRear;

    DcMotorEx singleMotor;

    Servo rear;
    Servo front;
    double speed = 0.8;
    State state;

    public Intake(HardwareMap hardwareMap) {
        motorFront = hardwareMap.get(DcMotorEx.class, "intakeFront");
        motorRear = hardwareMap.get(DcMotorEx.class, "intakeRear");
        rear = hardwareMap.get(Servo.class, "intakeRear");
        front = hardwareMap.get(Servo.class, "intakeFront");

        singleMotor = motorRear;
        state = State.OFF;

    }

    public void enable() {
        state = State.ON;
    }

    public void disable() {
        state = State.OFF;
    }

    public void toggleState() {
        if (state == State.ON) {
            state = State.OFF;
        } else {
            state = State.ON;
        }
    }

    public void update() {
        switch (state) {
            case ON:
                singleMotor.setPower(speed);
                singleMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                break;
            case OFF:
                singleMotor.setPower(speed/6);
                singleMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                 break;
        }
    }
}
