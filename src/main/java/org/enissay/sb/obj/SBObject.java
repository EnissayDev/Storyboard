package org.enissay.sb.obj;

import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.LoopType;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Objects;

public class SBObject {

    private LinkedList<Command> commands;
    private Layer layer;
    private Origin origin;
    private String filePath;
    private double x, y;
    private int frameCount, frameDelay;
    private LoopType loopType;

    //Moving image
    public SBObject(Layer layer, Origin origin, String filePath, double x, double y, int frameCount, int frameDelay, LoopType loopType) {
        this.layer = layer;
        this.origin = origin;
        this.filePath = filePath;
        this.x = x;
        this.y = y;
        this.frameCount = frameCount;
        this.frameDelay = frameDelay;
        this.loopType = loopType;
        this.commands = new LinkedList<>();
    }

    public SBObject(Layer layer, Origin origin, String filePath, int frameCount, int frameDelay, LoopType loopType) {
        this.layer = layer;
        this.origin = origin;
        this.filePath = filePath;
        this.x = origin.getX();
        this.y = origin.getY();
        this.frameCount = frameCount;
        this.frameDelay = frameDelay;
        this.loopType = loopType;
        this.commands = new LinkedList<>();
    }

    //Basic image
    public SBObject(Layer layer, Origin origin, String filePath, double x, double y) {
        this.layer = layer;
        this.origin = origin;
        this.filePath = filePath;
        this.x = x;
        this.y = y;
        this.commands = new LinkedList<>();
    }

    public SBObject(Layer layer, Origin origin, String filePath) {
        this.layer = layer;
        this.origin = origin;
        this.filePath = filePath;
        this.x = origin.getX();
        this.y = origin.getY();
        this.commands = new LinkedList<>();
    }

    public String getFilePath() {
        return filePath;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public int getFrameDelay() {
        return frameDelay;
    }

    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }

    public LoopType getLoopType() {
        return loopType;
    }

    public void setLoopType(LoopType loopType) {
        this.loopType = loopType;
    }

    public LinkedList<Command> getCommands() {
        return commands;
    }

    public SBObject addCommand(final Command command) {
        if (!commands.contains(command)) commands.add(command);
        return this;
    }

    public boolean isAnimation() {
        return (!Objects.isNull(frameCount) && !Objects.isNull(frameDelay) && !Objects.isNull(loopType));
    }

    private String doubleToString(Double d) {
        if (d == null)
            return null;
        if (d.isNaN() || d.isInfinite())
            return d.toString();

        return new BigDecimal(d.toString()).stripTrailingZeros().toPlainString();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isAnimation())
            sb.append("Animation");
        else sb.append("Sprite");
        sb.append("," + layer.getName() + "," + origin.getName() + ",\"" + filePath + "\"," + doubleToString(x) + "," + doubleToString(y));
        if (isAnimation()) sb.append("," + frameCount + "," + frameDelay + "," + loopType.getName());
        getCommands().forEach(command -> {
            if (command.getParent() == null)
                sb.append("\n");
            sb.append(command.toString());
        });
        return sb.toString();
    }
}
