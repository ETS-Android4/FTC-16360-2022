package org.firstinspires.ftc.teamcode.lib.hardware;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.lib.Controller;
import org.firstinspires.ftc.teamcode.lib.Globals;

public class Robot {

    public enum State{
        IDLE,
        DRIVING,
        INTAKING,
        EXTENDING,
        RETRACTING,
        DEPOSITING,
        EXTENDED,
        VIBING,
        TRANSITION
    }

    public State state;
    public State transition;
    public ElapsedTime timer;
    public ElapsedTime stateTimer;

    protected HardwareMap hardwareMap;

    public SampleMecanumDrive drive;
    public Slides slides;
    public Spinner spinner;
    public Box box;
    public Lock lock;
    public Intake intake;
    public boolean automation = true; //Set to false to disable automatic state transitions
    public boolean autoextend = true;
    public String temp = "";

    protected Pose2d poseEstimate = new Pose2d();

    boolean firstTime = true;
    boolean once = true;
    int statePart = 0;

    public Robot(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        //pass HardwareMap to hardware classes, initialize
        spinner = new Spinner(hardwareMap);
        slides = new Slides(hardwareMap);
        lock = new Lock(hardwareMap);
        intake = new Intake(hardwareMap);
        box = new Box(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
        timer = new ElapsedTime();
        stateTimer = new ElapsedTime();
        state = State.IDLE;
        transition = State.IDLE;

        // set robot pose
        drive.setPoseEstimate(Globals.currentPose);

        // Velocity control per wheel is not necessary outside of motion profiled auto
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //turn on bulk reading
        for (LynxModule module : this.hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    public void transitionState(State state) {
        transition = state;
    }

    public void extend() {transitionState(State.EXTENDING);}

    public void retract() {transitionState(State.RETRACTING);}

    public void deposit() {transitionState(State.DEPOSITING);}

    public void update() {

        switch (state) {
            case EXTENDING:
                if (statePart == 3 && box.ready) {
                    slides.extend();
                    transitionState(State.DRIVING);
                }
                if (statePart == 2 && slides.motor.getCurrentPosition() > slides.motor_safe-90) {
                    box.extend();
                    if(slides.extendedPos != Slides.State.MIN) {
                        slides.extend();
                        transitionState(State.DRIVING);
                    }
                    statePart++;
                }
                if (statePart == 1 && box.ready) {
                    slides.safeEx();
                    statePart++;
                }
                if (statePart == 0) {
                    box.transport();
                    intake.disable();
                    lock.state = Lock.State.NEUTRAL;
                    statePart++;
                }
                break;

            case RETRACTING:
                if (statePart > 1 && slides.motor.getCurrentPosition() <= 35) {
                    if (automation) {
                        transitionState(State.INTAKING);
                        timer.reset();
                    } else {
                        transitionState(State.DRIVING);
                    }
                }
                if (statePart > 1 && box.ready && stateTimer.milliseconds() > 320) {
                    slides.retract();
                }
                if (statePart == 1 && stateTimer.milliseconds() > 200) {
                    box.transport();
                    statePart++;
                }
                if (statePart == 0) {
                    lock.state = Lock.State.NEUTRAL;
                    slides.safeRe();
                    statePart++;
                }
                break;

            case DEPOSITING:
                lock.state = lock.depositDirection;
                if(stateTimer.milliseconds() > 300 && automation) {
                    transitionState(State.RETRACTING);
                }
                break;

            case INTAKING:
                if (timer.milliseconds() > 150) {
                    box.retract();
                }
                lock.state = Lock.State.INTAKING;
                intake.enable();
                if(box.full && automation && autoextend) {
                    transitionState(State.EXTENDING);
                }
                break;

            case DRIVING:
                intake.disable();
                break;
        }

        //transition between states
        if (transition != State.TRANSITION) {
            statePart = 0;
            state = transition;
            stateTimer.reset();
            transition = State.TRANSITION;
            firstTime = true;
        }

        // We update hardware classes continuously in the background, regardless of state
        drive.update();
        spinner.update();
        box.update();
        lock.update();
        intake.update();
        slides.update();
    }
}

