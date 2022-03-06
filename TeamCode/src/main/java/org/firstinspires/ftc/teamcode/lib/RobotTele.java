package org.firstinspires.ftc.teamcode.lib;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.lib.hardware.Lock;
import org.firstinspires.ftc.teamcode.lib.hardware.Robot;
import org.firstinspires.ftc.teamcode.lib.hardware.Slides;

public class RobotTele extends Robot {
    Controller controller1, controller2;
    int i = 0;

    public RobotTele(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        super(hardwareMap);

        // initialise controllers
        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);

    }

    @Override
    public void update() {
        // We update drive continuously in the background, regardless of state
        drive.update();

        // update controllers
        controller1.update();
        controller2.update();

        // update controls according to button states
        updateControls();
    }

    private void updateControls() {
        //controls
        if(controller2.getyButton() == Controller.ButtonState.ON_PRESS) {
            slides.setExtendedPos(Slides.State.MAX);
        }
        if(controller2.getxButton() == Controller.ButtonState.ON_PRESS) {
            slides.setExtendedPos(Slides.State.MEDIUM);
        }
        if(controller2.getaButton() == Controller.ButtonState.ON_PRESS) {
            slides.setExtendedPos(Slides.State.MIN);
        }

        if(controller2.getdPadUp() == Controller.ButtonState.ON_PRESS) {
            slides.offset += 10;
        }
        if(controller2.getdPadDown() == Controller.ButtonState.ON_PRESS) {
            slides.offset -= 10;
        }
        if(controller2.getRightBumper() == Controller.ButtonState.ON_PRESS) {
            lock.depositDirection = Lock.State.RIGHT;
            state = State.DEPOSITING;
            timer.reset();
        }
        if(controller2.getLeftBumper() == Controller.ButtonState.ON_PRESS) {
            lock.depositDirection = Lock.State.LEFT;
            state = State.DEPOSITING;
            timer.reset();
        }
        if(controller2.getLeftTrigger() == Controller.ButtonState.ON_PRESS) {
            state = State.EXTENDING;
        }
        if(controller2.getRightTrigger() == Controller.ButtonState.ON_PRESS) {
            state = State.RETRACTING;
        }
        if(controller2.getdPadLeft() == Controller.ButtonState.ON_PRESS) {
            intake.toggleState();
        }
        if(controller2.getdPadRight() == Controller.ButtonState.ON_PRESS) {
            //spinner.toggleState();
        }
        if(controller2.getRightJoystickButton() == Controller.ButtonState.ON_PRESS &&
                controller2.getLeftJoystickButton() == Controller.ButtonState.ON_PRESS) {
            automation = !automation;
        }
        if(controller2.getbButton() == Controller.ButtonState.PRESSED) {
            slides.motor.setPower(0);
        }
    }
}
