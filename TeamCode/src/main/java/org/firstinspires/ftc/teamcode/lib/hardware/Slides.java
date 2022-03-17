package org.firstinspires.ftc.teamcode.lib.hardware;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.Globals;

import java.util.Timer;

public class Slides {

    public enum State {
        MAX,
        ZERO,
        MIN,
        SAFETY,
        MEDIUM
    }

    public DcMotorEx motor;

    final int motor_max = 956;
    final int motor_medium = 390;
    final int motor_min = 145;
    final int motor_safe = 310;
    final int motor_retracted = 0;
    final double speed_in = 0.5;
    final double speed_out = 1;
    private double speed = 0.75;
    private double p = 0;
    public int offset = 0;
    public State extendedPos = State.MAX;
    public State state = State.ZERO;


    public Slides(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, "slides");
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void extend() {
        p = 10;
        state = extendedPos;
        speed = speed_out;
    }

    public void retract() {
        p = 10;
        state = State.ZERO;
        speed = speed_in;
    }

    public void safeEx() {
        p = 10;
        state = State.SAFETY;
        speed = speed_out;
    }

    public void safeRe() {
        p = 10;
        state = State.SAFETY;
        speed = speed_in;
    }


    public void setExtendedPos(State pos) {
        extendedPos = pos;
    }

    public void update() {
        motor.setPower(speed);
        motor.setPositionPIDFCoefficients(p);
        switch (state) {
            case MAX:
                motor.setTargetPosition(motor_max + offset);
                break;
            case ZERO:
                motor.setTargetPosition(motor_retracted);
                break;
            case MEDIUM:
                motor.setTargetPosition(motor_medium + offset);
                break;
            case SAFETY:
                motor.setTargetPosition(motor_safe + offset);
                break;
            case MIN:
                motor.setTargetPosition(motor_min + offset);
                break;
        }
    }
}
/*
        switch(currentState) {
            case INTAKING:
                if (sensor.getDistance() < 10) {
                    lock.setPosition(lock_neutral);
                    currentState = State.EXTENDING_0;
                    timer.reset();
                }
                if (motor.getCurrentPosition() == motor.getTargetPosition()) {
                    lock.setPosition(lock_intaking);
                }
                break;

            case EXTENDING_0:
                if (timer.milliseconds() > 10) {
                    motor.setTargetPosition(motor_extended);
                    currentState = State.EXTENDING_1;
                }
                break;

            case EXTENDING_1:
                if (timer.milliseconds() > 20) {
                    swing1.setPosition(swing1_Out);
                    swing2.setPosition(swing2_Out);
                    currentState = State.EXTENDED;
                }
                break;

            case EXTENDED:
                break;

            case DEPOSITLEFT:
                if (currentState == State.EXTENDED || currentState == State.DEPOSITRIGHT) {
                    lock.setPosition(lock_left);
                }
                break;

            case DEPOSITRIGHT:
                if (currentState == State.EXTENDED || currentState == State.DEPOSITLEFT) {
                    lock.setPosition(lock_right);
                }
                break;

            case RETRACTING:
                lock.setPosition(lock_neutral);
                swing1.setPosition(swing1_In);
                swing2.setPosition(swing2_In);
                motor.setTargetPosition(motor_retracted);
                timer.reset();
                currentState = State.INTAKING;
                break;
        }*/
