package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.Globals;
import org.firstinspires.ftc.teamcode.lib.RobotTele;
import org.firstinspires.ftc.teamcode.lib.hardware.Robot;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(group = "advanced")
public class Tele extends LinearOpMode {
    private RobotTele robot;

    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {

        // initialize robot
        robot = new RobotTele(hardwareMap, gamepad1, gamepad2);

        waitForStart();
        robot.state = Robot.State.INTAKING;

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            // clear cache for bulk reading
            for (LynxModule module : this.hardwareMap.getAll(LynxModule.class)) {
                module.clearBulkCache();
            }

            // update robot and all it's elements
            robot.update();

            //Telemetry
            telemetry.addData("power: ", robot.slides.motor.getPower());
            telemetry.addData("c: ", robot.box.c);
            telemetry.addData("timer: ", robot.timer.milliseconds());
            telemetry.addData("state: ", robot.state);
            telemetry.addData("slides state: ", robot.slides.state);
            telemetry.addData("box ready: ", robot.box.ready);
            telemetry.addData("box timer: ", robot.box.timer.milliseconds());
            telemetry.addData("slides position: ", robot.slides.motor.getCurrentPosition());
            telemetry.update();
        }
    }
}
