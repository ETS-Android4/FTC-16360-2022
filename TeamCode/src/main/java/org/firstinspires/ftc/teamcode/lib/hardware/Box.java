package org.firstinspires.ftc.teamcode.lib.hardware;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.lib.Globals;

public class Box {

    enum State {
        EXTENDED,
        RETRACTED,
        TRANSPORTING
    }

    public Servo swing1;
    public Servo swing2;
    public Rev2mDistanceSensor sensor;


    private State state = State.RETRACTED;
    private double swing1_In = 0.1;
    private double swing1_Out = 1;
    private double swing1_Trs = 0;
    private double swing2_In = 0.1;
    private double swing2_Out = 1;
    private double swing2_Trs = 0;
    boolean ready = true;
    public boolean full = false;
    ElapsedTime timer;

    public Box(HardwareMap hardwareMap) {
        swing1 = hardwareMap.get(Servo.class, "swing1");
        swing2 = hardwareMap.get(Servo.class, "swing2");
        sensor = hardwareMap.get(Rev2mDistanceSensor.class, "sensor");
    }

    public void extend() {
        state = State.EXTENDED;
        ready = false;
        timer.reset();
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
            case EXTENDED:
                swing1.setPosition(swing1_Out);
                swing2.setPosition(swing2_Out);
                break;
            case RETRACTED:
                swing1.setPosition(swing1_In);
                swing2.setPosition(swing2_In);
                break;
            case TRANSPORTING:
                swing1.setPosition(swing1_Trs);
                swing2.setPosition(swing2_Trs);
                break;
        }
        if (timer.milliseconds() > 10) {
            ready = true;
        }
        if (sensor.getDistance(DistanceUnit.CM) < 2) {
            full = true;
        } else {
            full = false;
        }
    }
}
