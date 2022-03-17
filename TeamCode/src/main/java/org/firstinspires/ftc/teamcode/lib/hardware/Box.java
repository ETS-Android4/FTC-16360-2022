package org.firstinspires.ftc.teamcode.lib.hardware;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.lib.Globals;

public class Box {

    public enum State {
        MAX,
        RETRACTED,
        TRANSPORTING,
        MEDIUM
    }

    public Servo swing1;
    public Servo swing2;
    public Rev2mDistanceSensor sensor;


    private State state = State.RETRACTED;

    private double swing1_In = 0.03;
    private double swing1_Out = 0.56;
    private double swing1_Med = 0.38;
    private double swing1_Trs = 0;

    private double swing2_In = 0.97;
    private double swing2_Out = 0.44;
    private double swing2_Med = 0.62;
    private double swing2_Trs = 1;

    public boolean ready = true;
    public double c = 0;
    double sensorThreshold = 8;
    public boolean full = false;
    public ElapsedTime timer;
    private State extendedPos = State.MAX;

    public Box(HardwareMap hardwareMap) {
        swing1 = hardwareMap.get(Servo.class, "swing1");
        swing2 = hardwareMap.get(Servo.class, "swing2");
        sensor = hardwareMap.get(Rev2mDistanceSensor.class, "sensor");
        timer = new ElapsedTime();
    }

    public void extend() {
        state = extendedPos;
        ready = false;
        timer.reset();
    }

    public void setExtendedPos(State state){
        extendedPos = state;
    }

    public void retract() {
        state = State.RETRACTED;
        ready = false;
        timer.reset();
    }

    public void transport() {
        state = State.TRANSPORTING;
        ready = false;
        timer.reset();
    }

    public void update(){
        switch (state) {
            case MAX:
                swing1.setPosition(swing1_Out);
                swing2.setPosition(swing2_Out);
                c = 0;
                break;
            case RETRACTED:
                swing1.setPosition(swing1_In);
                swing2.setPosition(swing2_In);
                if (sensor.getDistance(DistanceUnit.CM) < sensorThreshold) {
                    c++;
                } else {
                    c = 0;
                }
                break;
            case TRANSPORTING:
                swing1.setPosition(swing1_Trs);
                swing2.setPosition(swing2_Trs);
                break;
            case MEDIUM:
                swing1.setPosition(swing1_Med);
                swing2.setPosition(swing2_Med);
                c = 0;
                break;
        }
        if (timer.milliseconds() > 450) {
            ready = true;
        }
        if (c > 15) {
            full = true;
        } else {
            full = false;
        }
    }
}
