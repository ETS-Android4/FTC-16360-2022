package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.lib.Controller;
import org.firstinspires.ftc.teamcode.lib.Globals;
import org.firstinspires.ftc.teamcode.lib.RobotTele;
//import org.firstinspires.ftc.teamcode.lib.Vision;
import org.firstinspires.ftc.teamcode.lib.hardware.Lock;
import org.firstinspires.ftc.teamcode.lib.hardware.Robot;


@TeleOp(group = "advanced", name = "test2")

public class Test2 extends LinearOpMode {

    private DcMotor slides;

    private DcMotor intake;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

        Servo s1 = hardwareMap.get(Servo.class, "intakeRear");
        Rev2mDistanceSensor sensor = hardwareMap.get(Rev2mDistanceSensor.class, "sensor");
        Controller controller1 = new Controller(gamepad1);
        waitForStart();
        double i = 0.5;
        double j = 0.5;
        double avg = 200;

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            // clear cache for bulk reading
            for (LynxModule module : this.hardwareMap.getAll(LynxModule.class)) {
                module.clearBulkCache();
            }

            s1.setPosition(i);

            if (controller1.getaButton() == Controller.ButtonState.ON_PRESS) {
                i += 0.04;
            }
            if (controller1.getbButton() == Controller.ButtonState.ON_PRESS) {
                i -= 0.04;
            }

            if (controller1.getdPadUp() == Controller.ButtonState.ON_PRESS) {
                j += 0.04;
            }
            if (controller1.getdPadDown() == Controller.ButtonState.ON_PRESS) {
                j -= 0.04;
            }


            avg += sensor.getDistance(DistanceUnit.CM);
            avg -= avg/16;

            telemetry.addData("avg: ", avg);
            telemetry.addData("swing1: ", s1.getPosition());
            telemetry.addData("sensor: ", sensor.getDistance(DistanceUnit.CM));

            controller1.update();
            telemetry.update();
        }
    }
}