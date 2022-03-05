package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.Globals;
import org.firstinspires.ftc.teamcode.lib.Vision;
import org.firstinspires.ftc.teamcode.lib.hardware.Arm;
import org.firstinspires.ftc.teamcode.lib.hardware.Robot;
import org.opencv.core.Mat;

//@Disabled
public class Auto_Base_New{

    // This enum defines our "state"
    // This is defines the possible steps our program will take
    enum State {
        START_TO_DUCK,
        WHEELING,
        DUCK_TO_DEPOSIT,
        DEPOSIT,
        INTAKE_ON,
        DEPOSIT_TO_TAKEIN,
        TAKE_IN,
        TAKEIN_TO_DEPOSIT,
        PARK
    }

    enum StartPos {
        RED_INNER,
        RED_OUTER,
        BLUE_INNER,
        BLUE_OUTER
    }

    // Out Starting Position
    public StartPos startPos = StartPos.BLUE_INNER;

    // Our current State
    State currentState;

    // Declare our start pose
    Pose2d startPose;

    Pose2d takeinPose;

    // robot class
    Robot robot;

    // Vision Class Variable ToDo
    Vision vision;

    // Timer for timed actions
    ElapsedTime waitTimer;

    //
    Globals.BarcodePos barcodePos;

    //Telemetry
    Telemetry telemetry;

    //Trajectories
    Trajectory startToDuck, duckToDeposit, depostToPark, takeinToDeposit;
    public Auto_Base_New(HardwareMap hardwareMap, Telemetry telemetry, StartPos startPos){
        //copy telemetry, startPos
        this.telemetry = telemetry;
        this.startPos = startPos;

        // Initialize the robot class
        robot = new Robot(hardwareMap);

        //set arm to idle and close hand
        robot.arm.armState = Arm.StateArm.FRONT;
        robot.arm.handState = Arm.StateHand.CLOSED;
        robot.update();
        robot.arm.forceReset();

        // Initialize the vision Class ToDo
        vision = new Vision(hardwareMap);

        // a timer for timed actions
        waitTimer = new ElapsedTime();


        // define first state
        currentState = State.START_TO_DUCK;

        /*
         **  Trajectories
         *
         */

        switch (startPos) {
            case BLUE_INNER:
                startPose = new Pose2d(-62.4, 88.5, Math.toRadians(0));
                startToDuck = robot.drive.trajectoryBuilder(startPose)
                        .lineToLinearHeading(new Pose2d(-62.4, 40, Math.toRadians(270)))// Y Koordinate isch no unkekannt!!!
                        .build();
                break;
            case BLUE_OUTER:
                startPose = new Pose2d(-62.4, 40.5, Math.toRadians(0));
                startToDuck = robot.drive.trajectoryBuilder(startPose)
                        .lineToLinearHeading(new Pose2d(-62.4, 40, Math.toRadians(270)))// Y Koordinate isch no unbekannt!!!
                        .build();
                break;
        }

        duckToDeposit = robot.drive.trajectoryBuilder(startToDuck.end())// Y Koordinate isch unbekannt!!!
                .lineToLinearHeading(new Pose2d(-62.4, 20, Math.toRadians(270)))
                .build();

       depostToPark = robot.drive.trajectoryBuilder(takeinToDeposit.end())
               .lineTo(new Vector2d(-62.4, 20))//Y stimmt nonig!!!!!
               .build();

        takeinToDeposit = robot.drive.trajectoryBuilder(takeinPose) ///eifach endposition vom hindere Fahre, ich weiss nonig wie das gaht
                .lineTo(new Vector2d(-62.4, 20))//Y stimmt nöd!!!!
                .build();

        // Save initial pose to PoseStorage
        Globals.currentPose = startPose;

        // Set initial pose
        robot.drive.setPoseEstimate(startPose);
    }

    public void init() {
        waitTimer.reset();
    }

    public void update() {

        switch (currentState) {
            case START_TO_DUCK:
                // get Barcode Position ToDO
                barcodePos = vision.getBarcodePosition();
                robot.drive.followTrajectoryAsync(startToDuck);
                currentState = State.WHEELING;
                break;

            case WHEELING:
                //rad astelle für s entli
                currentState = State.DUCK_TO_DEPOSIT;
                break;
            case DUCK_TO_DEPOSIT:
                robot.drive.followTrajectoryAsync(duckToDeposit);
                currentState = State.DEPOSIT;
                break;
            case DEPOSIT:
                // arm usefahre und denn ablade uf de richtige höchi
                if (waitTimer.seconds() < 25) {
                    currentState = State.DEPOSIT_TO_TAKEIN;
                }
                else{
                    currentState = State.PARK;
                }
                break;
            case DEPOSIT_TO_TAKEIN:
                // intake aschalte und fürefahre
                robot.drive.setMotorPowers(10, 10, 10, 10); // stimmt no gar nonig
                currentState = State.TAKE_IN;
                break;

            case TAKE_IN:
                //intake bis öppis drin isch
                robot.drive.setMotorPowers(0,0,0,0);
                takeinPose = robot.drive.getPoseEstimate();
                currentState = State.TAKEIN_TO_DEPOSIT;
                break;
            case TAKEIN_TO_DEPOSIT:
                robot.drive.followTrajectoryAsync(takeinToDeposit);
                currentState=State.DEPOSIT;
                break;
            case PARK:
                robot.drive.followTrajectoryAsync(depostToPark);
                break;
        }

        // We update robot continuously in the background, regardless of state
        robot.update();

        // Read pose
        Pose2d poseEstimate = robot.drive.getPoseEstimate();

        // Continually write pose to PoseStorage
        Globals.currentPose = poseEstimate;

        // Print pose to telemetry
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
        telemetry.addData("position", barcodePos);
        telemetry.addData("armPos", robot.arm.armState);
        telemetry.addData("state", currentState);
        telemetry.update();
    }
}