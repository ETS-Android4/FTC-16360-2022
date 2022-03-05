package org.firstinspires.ftc.teamcode.lib;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

public class Globals {

    public enum Side {
        LEFT,
        RIGHT
    }

    public enum BarcodePos {
        BOTTOM,
        MIDDLE,
        TOP
    }

    public static Side IntakeSide = Side.LEFT;
    public static Pose2d currentPose = new Pose2d();
}
