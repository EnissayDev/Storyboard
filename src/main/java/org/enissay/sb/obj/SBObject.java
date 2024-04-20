package org.enissay.sb.obj;

import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.cmds.LoopType;
import org.enissay.sb.utils.OsuUtils;

import javax.vecmath.Vector2d;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class SBObject implements Cloneable{

    private LinkedList<Command> commands;
    private String name;
    private Layer layer;
    private Origin origin;
    private String filePath;
    private double x, y, endX, endY;
    private int frameCount, frameDelay;
    private LoopType loopType;

    //Moving image
    public SBObject(String name, Layer layer, Origin origin, String filePath, double x, double y, int frameCount, int frameDelay, LoopType loopType) {
        this.name = name;
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

    public SBObject(String name, Layer layer, Origin origin, String filePath, int frameCount, int frameDelay, LoopType loopType) {
        this.name = name;
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
    public SBObject(String name, Layer layer, Origin origin, String filePath, double x, double y) {
        this.name = name;
        this.layer = layer;
        this.origin = origin;
        this.filePath = filePath;
        this.x = x;
        this.y = y;
        this.commands = new LinkedList<>();
    }

    public SBObject(String name, Layer layer, Origin origin, String filePath) {
        this.name = name;
        this.layer = layer;
        this.origin = origin;
        this.filePath = filePath;
        this.x = origin.getX();
        this.y = origin.getY();
        this.commands = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public long getStartTime() {
        return Objects.isNull(startTime) ? 0 : startTime;
    }

    public long getEndTime() {
        return Objects.isNull(endTime) ? 0 : endTime;
    }*/

    public double getEndX() {
        return Objects.isNull(endX) ? 0 : endX;
    }

    public double getEndY() {
        return Objects.isNull(endY) ? 0 : endY;
    }

    /*public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }*/

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setEndY(double endY) {
        this.endY = endY;
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

    public double getWidth(Storyboard sb) {
        return OsuUtils.getImageDim(sb.getPath() + "\\" + getFilePath()) == null ? 0 : OsuUtils.getImageDim(sb.getPath() + "\\" + getFilePath()).getWidth();
    }

    public double getHeight(Storyboard sb) {
        return OsuUtils.getImageDim(sb.getPath() + "\\" + getFilePath()) == null ? 0 : OsuUtils.getImageDim(sb.getPath() + "\\" + getFilePath()).getHeight();
    }

    private String doubleToString(Double d) {
        if (d == null)
            return null;
        if (d.isNaN() || d.isInfinite())
            return d.toString();

        return new BigDecimal(d.toString()).stripTrailingZeros().toPlainString();
    }

    public Vector2d getPositionAt(final long time, final Easing easing) {
        return null;//getPosition2At(time, easing, getStartTime(), getEndTime(), new Vector2d(getEndX(), getEndY()));
    }

    public Vector2d getPositionAt(final Command command, final long time, final Easing easing, long startTime, long endTime, final Vector2d destination) {
        if (command != null && command.getParent() != null) {
            AtomicLong end = new AtomicLong(0);
            command.getParent().getSubCommands().forEach(subCommand -> {
                end.addAndGet(subCommand.getEndTime());
            });
            end.set(end.get()*Integer.valueOf(command.getParent().getParams()[0])-command.getParent().getStartTime());
            startTime += command.getParent().getStartTime();
            endTime = end.get();
        }
        final long duration = endTime - startTime;

        final double delta = (time - startTime);
        final double result = delta/duration;

        final double p = duration > 0 ? Easing.ease(easing, result) : 0;
        double x = (delta > 0 && delta <= duration) ? (getX() + (destination.getX() - getX()) * p) : getX();
        double y = (delta > 0 && delta <= duration) ? (getY() + (destination.getY() - getY()) * p) : getY();
        return new Vector2d(x,y);
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
