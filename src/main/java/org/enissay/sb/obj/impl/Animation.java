package org.enissay.sb.obj.impl;

import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Commands;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.cmds.LoopType;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.SBObject;

import java.awt.*;

public class Animation extends SBObject implements ISBObject{

    public Animation(Layer layer, Origin origin, String filePath, double x, double y, int frameCount, int frameDelay, LoopType loopType) {
        super(layer, origin, filePath, x, y, frameCount, frameDelay, loopType);
    }

    public Animation(Layer layer, Origin origin, String filePath, int frameCount, int frameDelay, LoopType loopType) {
        super(layer, origin, filePath, frameCount, frameDelay, loopType);
    }

    /*private SBObject sbObject;

    public Animation(Layer layer, Origin origin, String filePath, double x, double y, int frameCount, int frameDelay, LoopType loopType) {
        this.sbObject = new SBObject(layer, origin, filePath, x, y, frameCount, frameDelay, loopType);
    }

    public Animation(Layer layer, Origin origin, String filePath, int frameCount, int frameDelay, LoopType loopType) {
        this.sbObject = new SBObject(layer, origin, filePath, frameCount, frameDelay, loopType);
    }

    public SBObject getSBObject() {
        return sbObject;
    }*/

    @Override
    public Command Fade(Easing easing, long startTime, long endTime, double startOpacity, double endOpacity) {
        Command cmd = new Command(Commands.FADE, easing, startTime, endTime, new String[]{String.valueOf(startOpacity), String.valueOf(endOpacity)});
        this.addCommand(cmd);
        return cmd;
    }

    @Override
    public Command Fade(long startTime, long endTime, double startOpacity, double endOpacity) {
        return Fade(Easing.LINEAR, startTime, endTime, startOpacity, endOpacity);
    }

    @Override
    public Command Move(Easing easing, long startTime, long endTime, double startX, double startY, double endX, double endY) {
        Command cmd = new Command(Commands.MOVE, easing, startTime, endTime, new String[]{String.valueOf(startX),
                String.valueOf(startY), String.valueOf(endX), String.valueOf(endY)});
        this.addCommand(cmd);
        return cmd;
    }

    @Override
    public Command Move(long startTime, long endTime, double startX, double startY, double endX, double endY) {
        return Move(Easing.LINEAR, startTime, endTime, startX, startY, endX, endY);
    }

    @Override
    public Command MoveX(Easing easing, long startTime, long endTime, double startX, double endX) {
        return Move(easing, startTime, endTime, startX, startX, endX, endX);
    }

    @Override
    public Command MoveX(long startTime, long endTime, double startX, double endX) {
        return MoveX(Easing.LINEAR, startTime, endTime, startX, endX);
    }

    @Override
    public Command MoveY(Easing easing, long startTime, long endTime, double startY, double endY) {
        return Move(easing, startTime, endTime, startY, startY, endY, endY);
    }

    @Override
    public Command MoveY(long startTime, long endTime, double startY, double endY) {
        return MoveY(Easing.LINEAR, startTime, endTime, startY, endY);
    }

    @Override
    public Command Scale(Easing easing, long startTime, long endTime, double startScale, double endScale) {
        Command cmd = new Command(Commands.SCALE, easing, startTime, endTime, new String[]{String.valueOf(startScale),
                String.valueOf(endScale)});
        this.addCommand(cmd);
        return cmd;
    }

    @Override
    public Command Scale(long startTime, long endTime, double startScale, double endScale) {
        return Scale(Easing.LINEAR, startTime, endTime, startScale, endScale);
    }

    @Override
    public Command VectorScale(Easing easing, long startTime, long endTime, double startX, double startY, double endX, double endY) {
        Command cmd = new Command(Commands.VECTOR_SCALE, easing, startTime, endTime, new String[]{String.valueOf(startX),
                String.valueOf(startY), String.valueOf(endX), String.valueOf(endY)});
        this.addCommand(cmd);
        return cmd;
    }

    @Override
    public Command VectorScale(long startTime, long endTime, double startX, double startY, double endX, double endY) {
        return VectorScale(Easing.LINEAR, startTime, endTime, startX, startY, endX, endY);
    }

    @Override
    public Command Rotate(Easing easing, long startTime, long endTime, double startRotate, double endRotate) {
        Command cmd = new Command(Commands.ROTATE, easing, startTime, endTime, new String[]{String.valueOf(startRotate),
                String.valueOf(endRotate)});
        this.addCommand(cmd);
        return cmd;
    }

    @Override
    public Command Rotate(long startTime, long endTime, double startRotate, double endRotate) {
        return Rotate(Easing.LINEAR, startTime, endTime, startRotate, endRotate);
    }

    @Override
    public Command Color(Easing easing, long startTime, long endTime, int startR, int startG, int startB, int endR, int endG, int endB) {
        Command cmd = new Command(Commands.COLOR, easing, startTime, endTime, new String[]{String.valueOf(startR),
                String.valueOf(startG), String.valueOf(startB), String.valueOf(endR), String.valueOf(endG), String.valueOf(endB)});
        this.addCommand(cmd);
        return cmd;
    }

    @Override
    public Command Color(long startTime, long endTime, int startR, int startG, int startB, int endR, int endG, int endB) {
        return Color(Easing.LINEAR, startTime, endTime, startR, startG, startB, endR, endG, endB);
    }

    @Override
    public Command Color(Easing easing, long startTime, long endTime, Color startRGB, Color endRGB) {
        return Color(easing, startTime, endTime, startRGB.getRed(), startRGB.getGreen(), startRGB.getBlue(), endRGB.getRed(), endRGB.getGreen(), endRGB.getBlue());
    }

    @Override
    public Command Color(long startTime, long endTime, Color startRGB, Color endRGB) {
        return Color(Easing.LINEAR, startTime, endTime, startRGB, endRGB);
    }

    @Override
    public Command Parameter(Easing easing, long startTime, long endTime, char opt) {
        Command cmd = new Command(Commands.PARAMETER, easing, startTime, endTime, new String[]{String.valueOf(opt)});
        this.addCommand(cmd);
        return cmd;
    }

    @Override
    public Command Parameter(long startTime, long endTime, char opt) {
        return Parameter(Easing.LINEAR, startTime, endTime, opt);
    }

    @Override
    public Command createLoop(final long startTime, final int loopCount) {
        final String[] lc = new String[]{String.valueOf(loopCount)};
        Command cmd = new Command(Commands.LOOP, null, startTime, 0, lc);
        this.addCommand(cmd);
        return cmd;
    }
}
