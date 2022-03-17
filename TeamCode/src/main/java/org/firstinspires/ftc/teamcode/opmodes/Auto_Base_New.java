package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.Globals;
import org.firstinspires.ftc.teamcode.lib.hardware.Box;
import org.firstinspires.ftc.teamcode.lib.hardware.Lock;
import org.firstinspires.ftc.teamcode.lib.hardware.Robot;
import org.firstinspires.ftc.teamcode.lib.hardware.Slides;
//import org.opencv.core.Mat;

//@Disabled
public class Auto_Base_New{

    // This enum defines our "state"
    // This is defines the possible steps our program will take
    enum State {
        START_TO_DUCK,
        WAIT_FOR_SPINNER,
        WHEELING,
        DUCK_TO_DEPOSIT,
        DEPOSIT,
        DEPOSIT_TO_TAKEIN,
        TAKING_IN,
        TAKE_IN,
        TAKEIN_TO_DEPOSIT,
        PARK,
        WAIT1,
        WALL,
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
    State state;

    // Declare our start pose
    Pose2d startPose;

    Pose2d takeinPose;

    Pose2d currentPos;

    // robot class
    Robot robot;

    private boolean firstTime = true;

    // Vision Class Variable ToDo
    //Vision vision;

    // Timer for timed actions
    ElapsedTime waitTimer;
    ElapsedTime waitTimer1;
    ElapsedTime waitTimer2;
    ElapsedTime startTimer;
    ElapsedTime stateTimer;

    //
    Globals.BarcodePos barcodePos;

    //Telemetry
    Telemetry telemetry;

    //Trajectories
    Trajectory startToDuck, duckToDepositHigh, duckToDepositLow, duckToDepositMiddle, depostToPark, takeinToDeposit, DepositToTakein, depositToWall;
    public Auto_Base_New(HardwareMap hardwareMap, Telemetry telemetry, StartPos startPos){
        //copy telemetry, startPos
        this.telemetry = telemetry;
        this.startPos = startPos;

        // Initialize the robot class
        robot = new Robot(hardwareMap);

        // Initialize the vision Class ToDo
        //vision = new Vision(hardwareMap);

        // a timer for timed actions
        waitTimer = new ElapsedTime();
        waitTimer1 = new ElapsedTime();
        waitTimer2 = new ElapsedTime();
        startTimer = new ElapsedTime();
        stateTimer = new ElapsedTime();


        // define first state
        state = State.WAIT1;

        /*
         **  Trajectories
         *
         */
        takeinPose = new Pose2d(0,0,0);

        //vision = new Vision(hardwareMap);

      /*  switch (startPos) {
            case BLUE_INNER:
                startPose = new Pose2d(-62.4, 88.5, Math.toRadians(0));
                startToDuck = robot.drive.trajectoryBuilder(startPose)
                        .lineToLinearHeading(new Pose2d(-62.4, 40, Math.toRadians(270)))// Y Koordinate isch no unkekannt!!!
                        .build();
                break;
            case BLUE_OUTER:

       */
        startPose = new Pose2d(-33, 64, Math.toRadians(180)); //-64, 33
        startToDuck = robot.drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(-58.5, 59, Math.toRadians(180)))//-62, 33
                .build();
              /*  break;
        }


               */
        duckToDepositHigh = robot.drive.trajectoryBuilder(startToDuck.end())
                .lineToLinearHeading(new Pose2d(-20.5, 64, Math.toRadians(180)))
                .build();

        duckToDepositMiddle= robot.drive.trajectoryBuilder(startToDuck.end())
                .lineToLinearHeading(new Pose2d(-21.5, 39, Math.toRadians(180))) //stimmt nonig
                .build();

        duckToDepositLow = robot.drive.trajectoryBuilder(startToDuck.end())
                .lineToLinearHeading(new Pose2d(-22, 39, Math.toRadians(180))) //stimmt nonig
                .build();


        // Save initial pose to PoseStorage
        Globals.currentPose = startPose;

        // Set initial pose
        robot.drive.setPoseEstimate(startPose);
    }
    
    private void transitionState(State state) {
        this.state = state;
        stateTimer.reset();
    }

    public void init() {
        waitTimer.reset();
        startTimer.reset();
        switch (barcodePos) {
            case TOP:
                robot.slides.setExtendedPos(Slides.State.MAX);
            case MIDDLE:
                robot.slides.setExtendedPos(Slides.State.MEDIUM);
            case BOTTOM:
                robot.slides.setExtendedPos(Slides.State.MIN);
        }
        // define first state
        state = State.WAIT1;
    }

    public void update() {
        switch (state) {
            case WAIT1:
                transitionState(State.START_TO_DUCK);
                robot.intake.deploy();
                robot.drive.followTrajectoryAsync(startToDuck);
                break;

            case START_TO_DUCK:
                if (!robot.drive.isBusy()) {
                    transitionState(State.WHEELING);
                    robot.spinner.setSpinning();
                    waitTimer1.reset();
                }
                break;
            case WHEELING:
                if (waitTimer1.seconds()>2.5) {
                    robot.spinner.setIdle();
                    transitionState(State.DUCK_TO_DEPOSIT);
                    robot.extend();
                    switch (barcodePos) {
                        case TOP: {
                            robot.drive.followTrajectoryAsync(duckToDepositHigh);
                            robot.slides.setExtendedPos(Slides.State.MAX);
                            break;
                        }
                        case MIDDLE: {
                            robot.drive.followTrajectoryAsync(duckToDepositMiddle);
                            robot.slides.setExtendedPos(Slides.State.MEDIUM);
                            robot.box.setExtendedPos(Box.State.MEDIUM);
                            break;
                        }
                        case BOTTOM: {
                            robot.drive.followTrajectoryAsync(duckToDepositLow);
                            robot.slides.setExtendedPos(Slides.State.MIN);
                            robot.box.setExtendedPos(Box.State.MEDIUM);
                            break;
                        }
                    }
                }
                break;

            case DUCK_TO_DEPOSIT:
                if (!robot.drive.isBusy()) {
                    if (firstTime) {
                        stateTimer.reset();
                        firstTime = false;
                        robot.lock.depositDirection = Lock.State.LEFT;
                    }
                    if (stateTimer.milliseconds() > 1300) {
                        transitionState(State.DEPOSIT);
                    }
                }
                break;
            case DEPOSIT:
                if (robot.state == Robot.State.DRIVING) {
                    robot.deposit();
                }
                if(robot.slides.extendedPos == Slides.State.MAX && stateTimer.milliseconds() > 200){
                    DepositToTakein = robot.drive.trajectoryBuilder(robot.drive.getPoseEstimate())
                            .lineToLinearHeading(new Pose2d(35, 66, Math.toRadians(178)))
                            .build();
                    if (startTimer.seconds() < 24 && stateTimer.seconds() > 0.6 &&  robot.state == Robot.State.INTAKING) {
                        robot.drive.followTrajectoryAsync(DepositToTakein);
                        transitionState(State.DEPOSIT_TO_TAKEIN);
                    }
                    else if (startTimer.seconds() > 25 && robot.state == Robot.State.INTAKING) {
                        depostToPark = robot.drive.trajectoryBuilder(robot.drive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(35, 66, Math.toRadians(178)))
                                .build();
                        robot.drive.followTrajectoryAsync(depostToPark);
                        transitionState(State.PARK);
                    }
                } else if (stateTimer.milliseconds() > 200) {
                    depositToWall = robot.drive.trajectoryBuilder(robot.drive.getPoseEstimate())
                            .lineToLinearHeading(new Pose2d(-23, 65, Math.toRadians(180)))
                            .build();
                    robot.drive.followTrajectoryAsync(depositToWall);
                    transitionState(State.WALL);
                }
                break;
            case WALL:
                robot.slides.setExtendedPos(Slides.State.MAX);
                robot.box.setExtendedPos(Box.State.MAX);
                if (!robot.drive.isBusy()){
                    DepositToTakein = robot.drive.trajectoryBuilder(robot.drive.getPoseEstimate())
                            .lineToLinearHeading(new Pose2d(35, 66, Math.toRadians(178)))
                            .build();
                    barcodePos = Globals.BarcodePos.TOP;
                    if (startTimer.seconds() < 23 && stateTimer.seconds() > 0.6 &&  robot.state == Robot.State.INTAKING) {
                        robot.drive.followTrajectoryAsync(DepositToTakein);
                        transitionState(State.DEPOSIT_TO_TAKEIN);
                    }
                    else if (waitTimer.seconds() > 23 && robot.state == Robot.State.INTAKING) {
                        depostToPark = robot.drive.trajectoryBuilder(robot.drive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(35, 65, Math.toRadians(178)))
                                .build();
                        robot.drive.followTrajectoryAsync(depostToPark); //ToDo
                        transitionState(State.PARK);
                    }
                }
                break;

            case DEPOSIT_TO_TAKEIN:
                if(!robot.drive.isBusy()){
                    transitionState(State.TAKING_IN);
                    robot.intake.enable();
                    robot.drive.setMotorPowers(-0.2, -0.2, -0.17, -0.17);
                    robot.lock.depositDirection = Lock.State.RIGHT;
                }
                break;

            case TAKING_IN:
                if (robot.box.full){
                    robot.intake.disable();
                    transitionState(State.TAKEIN_TO_DEPOSIT);
                    takeinToDeposit = robot.drive.trajectoryBuilder(robot.drive.getPoseEstimate())
                            .lineTo(new Vector2d(-4, 64))
                            .build();
                    robot.drive.followTrajectoryAsync(takeinToDeposit);
                } else if (waitTimer.seconds() > 26) {
                    robot.intake.disable();
                    robot.drive.setMotorPowers(0,0,0,0);
                }
                break;

            case TAKEIN_TO_DEPOSIT:
                if (!robot.drive.isBusy()) {
                    transitionState(State.DEPOSIT);
                    waitTimer2.reset();
                    //robot.extend();
                }
                break;

            case PARK:
                robot.autoextend = false;
                robot.retract();
                break;
        }

        // We update robot continuou
        // sly in the background, regardless of state
        //robot.update();

        // Read pose
        Pose2d poseEstimate = robot.drive.getPoseEstimate();

        // Print pose to telemetry
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
        telemetry.addData("position", barcodePos);
        telemetry.addData("state", state);
        telemetry.addData("robtoState: ", robot.state);
        telemetry.update();
    }
}