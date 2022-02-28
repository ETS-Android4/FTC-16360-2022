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
