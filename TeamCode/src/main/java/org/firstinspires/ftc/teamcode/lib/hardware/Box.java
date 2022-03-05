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
        RETRACTED
    }

    Servo swing1;
    Servo swing2;
    Rev2mDistanceSensor sensor;


    private State state = State.RETRACTED;
    private double swing1_In = 0;
    private double swing1_Out = 1;
    private double swing2_In = 0;
    private double swing2_Out = 1;
    boolean ready = true;
    public boolean full = false;
    ElapsedTime timer;

    public Box(HardwareMap hardwareMap) {
        swing1 = hardwareMap.get(Servo.class, "swing1");
        swing2 = hardwareMap.get(Servo.class, "swing2");
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
