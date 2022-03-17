package org.firstinspires.ftc.teamcode.lib;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.lib.hardware.Box;
import org.firstinspires.ftc.teamcode.lib.hardware.Lock;
import org.firstinspires.ftc.teamcode.lib.hardware.Robot;
import org.firstinspires.ftc.teamcode.lib.hardware.Slides;

public class RobotTele extends Robot {
    Controller controller1, controller2;
    Gamepad gamepad1, gamepad2;
    int i = 0;

    public RobotTele(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        super(hardwareMap);

        // initialise controllers
        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);

        //store gamepads
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        state = State.DRIVING;
    }

    @Override
    public void update() {

        //get controller input to drive
        switch (state) {
            case INTAKING:
            case EXTENDING:
                drive.setWeightedDrivePower(new Pose2d(
                        gamepad1.left_stick_x,
                        -gamepad1.left_stick_y - 0.4 * (Math.abs(gamepad1.left_stick_x)),
                        -gamepad1.right_stick_x*0.5 - 0.1 * Math.abs(gamepad1.left_stick_x)));
                break;
            default:
                drive.setWeightedDrivePower(new Pose2d(
                        gamepad1.left_stick_x,
                        -gamepad1.left_stick_y,
                        -gamepad1.right_stick_x*0.5));
                break;
        }

        // We update drive continuously in the background, regardless of state
        drive.update();

        // update controllers
        controller1.update();
        controller2.update();

        // update controls according to button states
        updateControls();

        //update Robot
        super.update();
    }

    private void updateControls() {
        if(controller1.getaButton() == Controller.ButtonState.ON_PRESS) {
            spinner.toggleState();
        }

        if (controller1.getxButton() == Controller.ButtonState.ON_PRESS) {
            transitionState(State.INTAKING);
        }

        if (controller1.getbButton() == Controller.ButtonState.ON_PRESS) {
            intake.kill();
            transitionState(State.VIBING);
        }

        //controls
        if(controller2.getyButton() == Controller.ButtonState.ON_PRESS) {
            box.setExtendedPos(Box.State.MAX);
            slides.setExtendedPos(Slides.State.MAX);
        }
        if(controller2.getxButton() == Controller.ButtonState.ON_PRESS) {
            box.setExtendedPos(Box.State.MEDIUM);
            slides.setExtendedPos(Slides.State.MEDIUM);
        }
        if(controller2.getaButton() == Controller.ButtonState.ON_PRESS) {
            //intake.boost = true;
            box.setExtendedPos(Box.State.MEDIUM);
            slides.setExtendedPos(Slides.State.MIN);
        }
        if(controller2.getaButton() == Controller.ButtonState.ON_RELEASE) {
            intake.boost = false;
        }
        if(controller2.getbButton() == Controller.ButtonState.ON_PRESS) {
            intake.manualReverse = true;
            intake.boost = true;
        }
        if(controller2.getbButton() == Controller.ButtonState.ON_RELEASE) {
            intake.manualReverse = false;
            intake.boost = false;
        }


        if(controller2.getdPadUp() == Controller.ButtonState.ON_PRESS) {
            slides.offset += 15;
        }
        if(controller2.getdPadDown() == Controller.ButtonState.ON_PRESS) {
            slides.offset -= 15;
        }
        if(controller2.getRightBumper() == Controller.ButtonState.ON_PRESS) {
            lock.depositDirection = Lock.State.RIGHT;
            transitionState(State.DEPOSITING);
            timer.reset();
        }
        if(controller2.getLeftBumper() == Controller.ButtonState.ON_PRESS) {
            lock.depositDirection = Lock.State.LEFT;
            transitionState(State.DEPOSITING);
            timer.reset();
        }
        if(controller2.getLeftTrigger() == Controller.ButtonState.ON_PRESS) {
            transitionState(State.EXTENDING);
        }
        if(controller2.getRightTrigger() == Controller.ButtonState.ON_PRESS) {
            transitionState(State.RETRACTING);
        }
        if(controller2.getdPadLeft() == Controller.ButtonState.ON_PRESS) {
            intake.toggleState();
            state = State.IDLE;
        }
        if(controller2.getRightJoystickButton() == Controller.ButtonState.ON_PRESS &&
                controller2.getLeftJoystickButton() == Controller.ButtonState.ON_PRESS) {
            automation = !automation;
        }
        if(controller2.getdPadRight() == Controller.ButtonState.ON_PRESS) {
            autoextend = !autoextend;
        }
    }
}
