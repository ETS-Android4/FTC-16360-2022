package org.firstinspires.ftc.teamcode.lib.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;
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
        rear = hardwareMap.get(Servo.class, "rear");
        front = hardwareMap.get(Servo.class, "front");

        singleMotor = motorRear;
        state = State.OFF;

    }

    public void enable() {
        state = State.ON;
    }

    public void disable() {
        state = State.OFF;
    }

    public void update() {
        switch (state) {
            case ON:
                singleMotor.setPower(speed);
                break;
            case OFF:
                singleMotor.setPower(0);
                 break;
        }
    }
}
