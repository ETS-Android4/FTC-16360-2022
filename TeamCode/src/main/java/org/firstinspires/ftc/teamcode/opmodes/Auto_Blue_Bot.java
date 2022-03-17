package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.lib.Globals;

//@Disabled
@Autonomous(group = "opmodes")
public class Auto_Blue_Bot extends LinearOpMode {

    Auto_Base_New base;

    @Override
    public void runOpMode() throws InterruptedException {

        // initialize auto
        base = new Auto_Base_New(hardwareMap, telemetry, Auto_Base_New.StartPos.BLUE_OUTER);

        while(!isStarted() && !isStopRequested()) {
            //telemetry.addData("pp", base.vision.getBarcodePosition());
            telemetry.update();
            telemetry.addData("pos: ", String.valueOf(base.barcodePos));
            base.barcodePos = Globals.BarcodePos.BOTTOM;
            base.robot.drive.TRANSLATIONAL_PID = new PIDCoefficients(12,0,1);
        }

        waitForStart();

        if (isStopRequested()) return;

        base.init();

        while(opModeIsActive() && !isStopRequested()) {
            // clear cache for bulk reading
            for (LynxModule module : this.hardwareMap.getAll(LynxModule.class)) {
                module.clearBulkCache();
            }

            base.update();
            base.robot.update();
        }
    }
    //Globals.currentPose = base.robot.drive.getPoseEstimate();
}