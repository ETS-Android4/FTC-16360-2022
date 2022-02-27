package org.firstinspires.ftc.teamcode.lib.hardware;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.lib.Globals;

public class Robot {

    public enum State{
        IDLE,
        DRIVING,
        INTAKING,
        EXTENDING,
        RETRACTING,
        EXTENDED,
        VIBING
    }

    public State state;
    ElapsedTime timer;

    protected HardwareMap hardwareMap;

    public SampleMecanumDrive drive;
    public Slides slides;
    public Spinner spinner;
    public Box box;
    public Lock lock;
    public Intake intake;

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

    public void update() {

        switch (state) {
            case EXTENDING:
                intake.disable();
                slides.motor.setPower(1);
                slides.setpos(Slides.State.MAX);
                lock.state = Lock.State.NEUTRAL;
                if (slides.motor.getCurrentPosition() > 10) {
                    box.extend();
                }
                break;

            case RETRACTING:
                lock.state = Lock.State.NEUTRAL;
                slides.motor.setPower(0.8);
                box.retract();
                if (box.ready) {
                    slides.setpos(Slides.State.ZERO);
                }
                break;

            case INTAKING:
                lock.state = Lock.State.INTAKING;
                intake.enable();
                break;

            case DRIVING:
                intake.disable();
                break;
        }

        //controls



        // We update hardware classes continuously in the background, regardless of state
        drive.update();
        spinner.update();
        box.update();
        lock.update();
        intake.update();
    }

}
