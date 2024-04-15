package org.enissay.sb.cmds;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Command {

    private Commands type;
    private Command parent;
    private Easing easing;
    private long startTime, endTime;
    private String[] params;
    private LinkedList<Command> subCommands;//Only for trigger and loop commands

    public Command(Commands type, Easing easing, long startTime, long endTime, String[] params) {
        this.type = type;
        this.easing = easing;
        this.startTime = startTime;
        this.endTime = endTime;
        this.params = params;
        this.subCommands = new LinkedList<>();
    }

    public Command getParent() {
        return parent;
    }

    public void setParent(Command parent) {
        this.parent = parent;
    }

    public Commands getType() {
        return type;
    }

    public Easing getEasing() {
        return easing;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String[] getParams() {
        return params;
    }

    public void addSubCommand(final Command command) {
        if (!subCommands.contains(command) && command != this) {
            if (command.getType() != Commands.LOOP && command.getType() != Commands.TRIGGER) {
                subCommands.add(command);
                command.setParent(this);
            }
        }
    }

    public LinkedList<Command> getSubCommands() {
        return subCommands;
    }

    /*public String getStructure(final Commands type, final Easing easing, final long startTime, final long endTime, final String[] params, LinkedList<Command> subCommands) {
        StringBuilder sb = new StringBuilder();
        sb.append(getStructure(false, type, easing, startTime, endTime, params));
        if (subCommands != null && subCommands.size() > 0) {
            subCommands.forEach(subCommand -> {
                sb.append(getStructure(true, subCommand.getType(), subCommand.getEasing(), subCommand.getStartTime(), subCommand.getEndTime(), subCommand.getParams()));
            });
        }

        return sb.toString();
    }*/

    public String getLoopStructure(final long startTime, final int loopCount) {
        StringBuilder sb = new StringBuilder();
        //2 spaces if sub command
        sb.append(" " + type.getSymbol() + "," + startTime + "," + loopCount + (subCommands.size() > 0 ? "\n" : ""));
        AtomicInteger i = new AtomicInteger();
        subCommands.forEach(subCommand -> {
            i.getAndIncrement();
            sb.append(getStructure(true, subCommand.getType(), subCommand.getEasing(), subCommand.getStartTime(), subCommand.getEndTime(), subCommand.getParams()) +
                    (i.get() < subCommands.size() ? "\n" : ""));
        });
        return sb.toString();
    }

    /*public String getTriggerStructure(final Trifinal long startTime, final int loopCount) {
        StringBuilder sb = new StringBuilder();
        //2 spaces if sub command
        sb.append(" " + type.getSymbol() + "," + startTime + "," + loopCount);
        subCommands.forEach(subCommand -> {
            sb.append(getStructure(true, subCommand.getType(), subCommand.getEasing(), subCommand.getStartTime(), subCommand.getEndTime(), subCommand.getParams()));
        });
        return sb.toString();
    }*/

    private String doubleToString(Double d) {
        if (d == null)
            return null;
        if (d.isNaN() || d.isInfinite())
            return d.toString();

        return new BigDecimal(d.toString()).stripTrailingZeros().toPlainString();
    }

    /**
     *
     * LOOP ->
     * _L,(starttime),(loopcount)
     * __(event),(easing),(relative_starttime),(relative_endtime),(params...)
     */
    public String getStructure(final boolean subCommand, final Commands type, final Easing easing, final long startTime, final long endTime, final String[] params) {
        StringBuilder sb = new StringBuilder();
        //2 spaces if sub command
        sb.append((subCommand ? "  " : " ") + type.getSymbol() + "," + easing.getIndex() + "," + startTime + "," + (endTime == startTime ? "" : endTime));
        sb.append(",");
        switch (type) {
            case VECTOR_SCALE:
                double startVecX = Double.parseDouble(params[0]);
                double startVecY = Double.parseDouble(params[1]);
                double endVecX = Double.parseDouble(params[2]);
                double endVecY = Double.parseDouble(params[3]);
                if (startVecX == endVecX && startVecY == endVecY) sb.append(doubleToString(startVecX) + "," + doubleToString(startVecY));
                else {
                    List<String> paramStrings = new ArrayList<>();
                    for (String param : params) {
                        paramStrings.add(doubleToString(Double.parseDouble(param)));
                    }
                    sb.append(String.join(",", paramStrings));
                }
                break;
            case ROTATE:
            case SCALE:
            case FADE:
                double start = Double.parseDouble(params[0]);
                double end = Double.parseDouble(params[1]);
                if (start == end) sb.append(doubleToString(start));
                else {
                    List<String> paramStrings = new ArrayList<>();
                    for (String param : params) {
                        paramStrings.add(doubleToString(Double.parseDouble(param)));
                    }
                    sb.append(String.join(",", paramStrings));
                }
                break;
            case COLOR:
                int startR = Integer.valueOf(params[0]);
                int startG = Integer.valueOf(params[1]);
                int startB = Integer.valueOf(params[2]);
                int endR = Integer.valueOf(params[3]);
                int endG = Integer.valueOf(params[4]);
                int endB = Integer.valueOf(params[5]);
                if (startR == endR && startG == endG && startB == endB) sb.append(endR + "," + endG + "," + endB);
                else {
                    List<String> paramStrings = new ArrayList<>();
                    for (String param : params) {
                        paramStrings.add(doubleToString(Double.parseDouble(param)));
                    }
                    sb.append(String.join(",", paramStrings));
                }
                break;
            case MOVE:
                double startX = Double.valueOf(params[0]);
                double startY = Double.valueOf(params[1]);
                double endX = Double.valueOf(params[2]);
                double endY = Double.valueOf(params[3]);

                if (startX == endX) {
                    //OFFSET 3 IF SUBCOMMAND
                    sb.insert(2 + (subCommand ? 1 : 0), "Y");
                    sb.append(doubleToString(startY) + "," + doubleToString(endY));
                }
                else if (startY == endY) {
                    //OFFSET 3 IF SUBCOMMAND
                    sb.insert(2 + (subCommand ? 1 : 0), "X");
                    sb.append(doubleToString(startX) + "," + doubleToString(endX));
                } else {
                    List<String> paramStrings = new ArrayList<>();
                    for (String param : params) {
                        paramStrings.add(doubleToString(Double.parseDouble(param)));
                    }
                    sb.append(String.join(",", paramStrings));
                }
                break;
            default:
                List<String> paramStrings = new ArrayList<>();
                for (String param : params) {
                    try {
                        paramStrings.add(doubleToString(Double.parseDouble(param)));
                    }catch (Exception e) {
                        paramStrings.add(param);
                    }
                }
                sb.append(String.join(",", paramStrings));
                break;
        }
        return sb.toString();
    }

    /*public String getStructure(boolean subCommand) {
        StringBuilder sb = new StringBuilder();
        //2 spaces if sub command
        sb.append((subCommand ? "  " : " ") + type.getSymbol() + "," + easing.getIndex() + "," + startTime + "," + (endTime == startTime ? "" : endTime));
        sb.append(",");
        switch (type) {
            case FADE:
                double startOpacity = Double.valueOf(params[0]);
                double endOpacity = Double.valueOf(params[1]);
                if (startOpacity == endOpacity) sb.append(startOpacity);
                else sb.append(String.join(",", Arrays.asList(params)));
                break;
            case MOVE:
                double startX = Double.valueOf(params[0]);
                double startY = Double.valueOf(params[1]);
                double endX = Double.valueOf(params[2]);
                double endY = Double.valueOf(params[3]);

                if (startX == endX) {
                    //OFFSET 3 IF SUBCOMMAND
                    sb.insert(2 + (subCommand ? 1 : 0), "Y");
                    sb.append(startY + "," + endY);
                }
                else if (startY == endY) {
                    //OFFSET 3 IF SUBCOMMAND
                    sb.insert(2 + (subCommand ? 1 : 0), "X");
                    sb.append(startX + "," + endX);
                }else
                    sb.append(startX + "," + startY + "," + endX + "," + endY);
                break;
            default:
                sb.append(String.join(",", Arrays.asList(params)));
                break;
        }
        return sb.toString();
    }*/

    /**
     *
     * FADE ->
     *   params: (start_opacity),(end_opacity)
     *
     * MOVE ->
     *   params: (start_x),(start_y),(end_x),(end_y)
     *
     * SCALE ->
     *   params: (start_scale),(end_scale)
     *
     * VECTOR_SCALE ->
     *   params: (start_scale_x),(start_scale_y),(end_scale_x),(end_scale_y)
     *
     * ROTATE ->
     *   params: (start_rotate),(end_rotate)
     *
     * COLOR ->
     *   params: (start_r),(start_g),(start_b),(end_r),(end_g),(end_b)
     *
     * PARAMETER ->
     *   params:
     *      "H" - flip the image horizontally (NOT the same as rotating the object 180 degrees, i.e., pi radians). [Horizontal Flip]
     *      "V" - flip the image vertically. [Vertical Flip]
     *      "A" - use additive-colour blending instead of alpha-blending
     *
     * LOOP ->
     * _L,(starttime),(loopcount)
     * __(event),(easing),(relative_starttime),(relative_endtime),(params...)
     *
     * @return
     */
    //TO-DO: sub commands
    @Override
    public String toString() {
        String result;
        switch (type) {
            case TRIGGER:
                result = "";
                break;
            case LOOP:
                result = getLoopStructure(getStartTime(), Integer.parseInt(getParams()[0]));
                break;
            default:
                if (this.getParent() == null)
                    result = getStructure(false, getType(), getEasing(), getStartTime(), getEndTime(), getParams());
                else result = "";
                break;
        }
        return result;
    }
}
