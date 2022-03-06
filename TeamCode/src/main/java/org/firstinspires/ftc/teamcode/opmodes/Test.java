package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.lib.Controller;
import org.firstinspires.ftc.teamcode.lib.Globals;
import org.firstinspires.ftc.teamcode.lib.RobotTele;
import org.firstinspires.ftc.teamcode.lib.Vision;
import org.firstinspires.ftc.teamcode.lib.hardware.Robot;


@TeleOp(group = "advanced", name = "test")
public class Test extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    public Robot robot;
    public Controller controller1;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new Robot(hardwareMap);
        boolean b = false;
        int i = 0;
        controller1 = new Controller(gamepad1);

        waitForStart();
        robot.slides.motor.setPower(0.8);

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            // clear cache for bulk reading
            for (LynxModule module : this.hardwareMap.getAll(LynxModule.class)) {
                module.clearBulkCache();
            }

            if (controller1.getdPadUp() == Controller.ButtonState.ON_PRESS) {
                robot.slides.motor.setTargetPosition(robot.slides.motor.getTargetPosition() + 10);
            }
            if (controller1.getdPadDown() == Controller.ButtonState.ON_PRESS) {
                robot.slides.motor.setTargetPosition(robot.slides.motor.getTargetPosition() - 10);
            }
            if (controller1.getdPadRight() == Controller.ButtonState.ON_PRESS) {
                robot.lock.servo.setPosition(robot.lock.servo.getPosition() + 0.05);
            }
            if (controller1.getdPadLeft() == Controller.ButtonState.ON_PRESS) {
                robot.lock.servo.setPosition(robot.lock.servo.getPosition() - 0.05);
            }
            if (controller1.getaButton() == Controller.ButtonState.ON_PRESS) {
                robot.box.swing1.setPosition(robot.box.swing1.getPosition() + 0.05);
                robot.box.swing2.setPosition(robot.box.swing2.getPosition() + 0.05);
            }
            if (controller1.getbButton() == Controller.ButtonState.ON_PRESS) {
                robot.box.swing1.setPosition(robot.box.swing1.getPosition() - 0.05);
                robot.box.swing2.setPosition(robot.box.swing2.getPosition() - 0.05);
            }

            if (controller1.getxButton() == Controller.ButtonState.ON_PRESS) {
                robot.box.swing1.setPosition(1);
                robot.box.swing2.setPosition(1);
            }
            if (controller1.getyButton() == Controller.ButtonState.ON_PRESS) {
                robot.box.swing1.setPosition(0);
                robot.box.swing2.setPosition(0);
            }
            if (controller1.getRightBumper() == Controller.ButtonState.ON_PRESS) {
                robot.slides.motor.setTargetPosition(0);
            }
            if (gamepad1.a) {
                robot.slides.motor.setTargetPosition(300);
            }
            i++;


            //telemetry.addLine(vision.getBarcodePosition().name());
            telemetry.addData("sensor Distance: ", robot.box.sensor.getDistance(DistanceUnit.CM));
            telemetry.addData("slides Position: ", robot.slides.motor.getCurrentPosition());
            telemetry.addData("slides Target: ", robot.slides.motor.getTargetPosition());
            telemetry.addData("i: ",i);
            telemetry.addData("joystick: ", controller1.getLeftJoystickXValue());
            telemetry.update();
            controller1.update();
        }
    }
}

/*
package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.Controller;
import org.firstinspires.ftc.teamcode.lib.Globals;
import org.firstinspires.ftc.teamcode.lib.RobotTele;
import org.firstinspires.ftc.teamcode.lib.Vision;
import org.firstinspires.ftc.teamcode.lib.hardware.Arm;
import org.firstinspires.ftc.teamcode.lib.hardware.Robot;


@TeleOp(group = "advanced", name = "test")

public class Test extends LinearOpMode {

    private DcMotor slides;

    private DcMotor intake;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

        // initialize robot
        slides = hardwareMap.get(DcMotor.class, "slides");
        slides.setTargetPosition(0);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            // clear cache for bulk reading
            for (LynxModule module : this.hardwareMap.getAll(LynxModule.class)) {
                module.clearBulkCache();
            }

            if (gamepad1.a) {
                slides.setPower(1);
                slides.setTargetPosition(-770);
            }
            if (gamepad1.b) {
                slides.setPower(0.8);
                slides.setTargetPosition(0);
            }
            if (gamepad1.x) {
                slides.setPower(1);
                slides.setTargetPosition(-220);
            }

            if (gamepad1.left_bumper) {
                intake.setPower(1);
            }
            if (gamepad1.right_bumper) {
                intake.setPower(-1);
            }
            if (gamepad1.left_stick_button) {
                intake.setPower(0);
            }


            //telemetry.addLine(vision.getBarcodePosition().name());
            telemetry.addData("position", slides.getCurrentPosition());
            telemetry.update();
        }
    }
}

 */