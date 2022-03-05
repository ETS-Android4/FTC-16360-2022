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
        MEDIUM
    }

    public DcMotorEx motor;

    final int motor_max = 770;
    final int motor_medium = 770;
    final int motor_min = 770;
    final int motor_retracted = 0;
    public int offset = 0;
    public double speed = 0;
    public State extendedPos = State.MAX;


    public Slides(HardwareMap hardwareMap) {

        //sensor = hardwareMap.get(Rev2mDistanceSensor.class, "sensor");
        motor = hardwareMap.get(DcMotorEx.class, "slides");
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void extend() {
        setpos(extendedPos);
    }

    public void retract() {
        setpos(State.ZERO);
    }

    public void setExtendedPos(State pos) {
        extendedPos = pos;
    }

    public void setpos(State state) {
        switch (state) {
            case MAX:
                motor.setTargetPosition(motor_max + offset);
                break;
            case ZERO:
                motor.setTargetPosition(motor_retracted + offset);
                break;
            case MEDIUM:
                motor.setTargetPosition(motor_medium + offset);
                break;
            case MIN:
                motor.setTargetPosition(motor_min + offset);
                break;
        }
        motor.setPower(speed);
    }

    public void update() {

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
