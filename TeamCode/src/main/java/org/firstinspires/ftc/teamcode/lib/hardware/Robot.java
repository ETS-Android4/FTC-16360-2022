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
        VIBING
    }

    public State state;
    public ElapsedTime timer;

    protected HardwareMap hardwareMap;

    public SampleMecanumDrive drive;
    public Slides slides;
    public Spinner spinner;
    public Box box;
    public Lock lock;
    public Intake intake;
    public boolean automation = true; //Set to false to disable automatic state transitions

    protected Pose2d poseEstimate = new Pose2d();

    public Robot(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        //pass HardwareMap to hardware classes, initialize
        spinner = new Spinner(hardwareMap);
        slides = new Slides(hardwareMap);
        box = new Box(hardwareMap);
        lock = new Lock(hardwareMap);
        intake = new Intake(hardwareMap);

        // set robot pose
        drive.setPoseEstimate(Globals.currentPose);

        // Velocity control per wheel is not necessary outside of motion profiled auto
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //turn on bulk reading
        for (LynxModule module : this.hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    public void extendArm() {state = State.EXTENDED;}

    public void retractArm() {state = State.RETRACTING;}

    public void deposit() {state = State.DEPOSITING;}

    public void update() {

        switch (state) {
            case EXTENDING:
                intake.disable();
                slides.speed = 1;
                slides.extend();
                lock.state = Lock.State.NEUTRAL;
                if (slides.motor.getCurrentPosition() > 10) {
                    box.extend();
                } else {
                    box.transport();
                }
                if (slides.motor.getCurrentPosition() >= slides.motor.getTargetPosition()) {
                    state = State.DRIVING;
                }
                break;

            case RETRACTING:
                lock.state = Lock.State.NEUTRAL;
                slides.speed = 0.8;
                box.transport();
                if (box.ready) {
                    slides.retract();
                }
                if (slides.motor.getCurrentPosition() <= 10) {
                    if (automation) {
                        state = State.INTAKING;
                    } else {
                        state = State.DRIVING;
                    }
                    box.retract();
                }
                break;

            case DEPOSITING:
                lock.state = lock.depositDirection;
                if(timer.milliseconds() > 10 && !box.full && automation) {
                    state = State.RETRACTING;
                }
                break;

            case INTAKING:
                lock.state = Lock.State.INTAKING;
                intake.enable();
                if(box.full && automation) {
                    state = State.EXTENDING;
                }
                break;

            case DRIVING:
                intake.disable();
                break;
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
