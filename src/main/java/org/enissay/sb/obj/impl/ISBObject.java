package org.enissay.sb.obj.impl;

import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Commands;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.obj.SBObject;

import java.awt.*;

public interface ISBObject {

    Command Fade(Easing easing, long startTime, long endTime, double startOpacity, double endOpacity);
    Command Fade(long startTime, long endTime, double startOpacity, double endOpacity);
    Command Fade(Easing easing, long time, double startOpacity, double endOpacity);
    Command Fade(long time, double startOpacity, double endOpacity);
    Command Fade(Easing easing, long startTime, long endTime, double opacity);
    Command Fade(long startTime, long endTime, double opacity);
    Command Fade(Easing easing, long time, double opacity);
    Command Fade(long time, double opacity);
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
    Command Scale(Easing easing, long time, double startScale, double endScale);
    Command Scale(long time, double startScale, double endScale);
    Command Scale(Easing easing, long startTime, long endTime, double scale);
    Command Scale(long startTime, long endTime, double scale);
    Command Scale(Easing easing, long time, double scale);
    Command Scale(long time, double scale);
    Command VectorScale(Easing easing, long startTime, long endTime, double startX, double startY, double endX, double endY);
    Command VectorScale(long startTime, long endTime, double startX, double startY, double endX, double endY);
    Command VectorScale(Easing easing, long time, double startX, double startY, double endX, double endY);
    Command VectorScale(long time, double startX, double startY, double endX, double endY);
    Command VectorScale(Easing easing, long startTime, long endTime, double x, double y);
    Command VectorScale(long startTime, long endTime, double x, double y);
    Command VectorScale(Easing easing, long time, double x, double y);
    Command VectorScale(long time, double x, double y);
    Command Rotate(Easing easing, long startTime, long endTime, double startRotate, double endRotate);
    Command Rotate(long startTime, long endTime, double startRotate, double endRotate);
    Command Rotate(Easing easing, long startTime, long endTime, double rotation);
    Command Rotate(long startTime, long endTime, double rotation);
    Command Rotate(Easing easing, long time, double startRotate, double endRotate);
    Command Rotate(long time, double startRotate, double endRotate);
    Command Rotate(Easing easing, long time, double rotation);
    Command Rotate(long time, double rotation);
    Command Color(Easing easing, long startTime, long endTime, int startR, int startG, int startB, int endR, int endG, int endB);
    Command Color(long startTime, long endTime, int startR, int startG, int startB, int endR, int endG, int endB);
    Command Color(Easing easing, long startTime, long endTime, Color startRGB, Color endRGB);
    Command Color(long startTime, long endTime, Color startRGB, Color endRGB);
    Command Color(Easing easing, long time, Color startRGB, Color endRGB);
    Command Color(long time, Color startRGB, Color endRGB);
    Command Color(Easing easing, long startTime, long endTime, Color color);
    Command Color(long startTime, long endTime, Color color);
    Command Color(Easing easing, long time, Color color);
    Command Color(long time, Color color);
    Command Parameter(Easing easing, long startTime, long endTime, char opt);
    Command Parameter(long startTime, long endTime, char opt);
    Command Parameter(Easing easing, long time, char opt);
    Command Parameter(long time, char opt);
    Command createLoop(long startTime, int loopCount);
}
