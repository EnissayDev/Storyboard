package org.enissay.sb.obj.impl;

import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Commands;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.obj.SBObject;

import java.awt.*;

public interface ISBObject {

    Command Fade(Easing easing, long startTime, long endTime, double startOpacity, double endOpacity);
    Command Fade(long startTime, long endTime, double startOpacity, double endOpacity);
    Command Move(Easing easing, long startTime, long endTime, double startX, double startY, double endX, double endY);
    Command Move(long startTime, long endTime, double startX, double startY, double endX, double endY);
    Command Move(Easing easing, long startTime, long endTime, double x, double y);
    Command Move(long startTime, long endTime, double x, double y);
    Command MoveX(Easing easing, long startTime, long endTime, double startX, double endX);
    Command MoveX(long startTime, long endTime, double startX, double endX);
    Command MoveY(Easing easing, long startTime, long endTime, double startY, double endY);
    Command MoveY(long startTime, long endTime, double startY, double endY);
    Command Scale(Easing easing, long startTime, long endTime, double startScale, double endScale);
    Command Scale(long startTime, long endTime, double startScale, double endScale);
    Command VectorScale(Easing easing, long startTime, long endTime, double startX, double startY, double endX, double endY);
    Command VectorScale(long startTime, long endTime, double startX, double startY, double endX, double endY);
    Command Rotate(Easing easing, long startTime, long endTime, double startRotate, double endRotate);
    Command Rotate(long startTime, long endTime, double startRotate, double endRotate);
    Command Color(Easing easing, long startTime, long endTime, int startR, int startG, int startB, int endR, int endG, int endB);
    Command Color(long startTime, long endTime, int startR, int startG, int startB, int endR, int endG, int endB);
    Command Color(Easing easing, long startTime, long endTime, Color startRGB, Color endRGB);
    Command Color(long startTime, long endTime, Color startRGB, Color endRGB);
    Command Parameter(Easing easing, long startTime, long endTime, char opt);
    Command Parameter(long startTime, long endTime, char opt);
    Command createLoop(long startTime, int loopCount);
}
