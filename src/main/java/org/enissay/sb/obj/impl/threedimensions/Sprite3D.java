package org.enissay.sb.obj.impl.threedimensions;

import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Commands;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.keyframe.InterpolatingFunctions;
import org.enissay.sb.keyframe.KeyframedValue;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.SBObject;
import org.enissay.sb.obj.impl.ISBObject;
import org.enissay.sb.obj.impl.Sprite;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Sprite3D extends SBObject implements Node3D {

    public boolean additive;
    public RotationMode rotationMode = RotationMode.UNIT_Y;
    public boolean useDistanceFade = true;
    public final KeyframedValue<Vector2f> spriteScale = new KeyframedValue<>(InterpolatingFunctions.vector2Interpolator, new Vector2f(1f, 1f));
    public final KeyframedValue<Double> spriteRotation = new KeyframedValue<>(InterpolatingFunctions.doubleAngleInterpolator, 0.0);

    public Sprite3D(String name, Layer layer, Origin origin, String filePath, double x, double y) {
        super(name, layer, origin, filePath, x, y);
    }

    public Sprite3D(String name, Layer layer, Origin origin, String filePath) {
        super(name, layer, origin, filePath);
    }

    public void generateStates(double time, CameraState cameraState) {
        Matrix4f wvp = worldTransformAt(time).mul(cameraState.getViewProjection());
        Vector4f screenPosition = cameraState.toScreen(wvp, new Vector3f(0, 0, 0));

        double angle = 0.0;
        switch (rotationMode) {
            case UNIT_X:
                Vector4f unitXPosition = cameraState.toScreen(wvp, new Vector3f(1, 0, 0));
                Vector2f deltaX = new Vector2f(unitXPosition.x, unitXPosition.y).sub(new Vector2f(screenPosition.x, screenPosition.y));
                angle += Math.atan2(deltaX.y, deltaX.x);
                break;
            case UNIT_Y:
                Vector4f unitYPosition = cameraState.toScreen(wvp, new Vector3f(0, 1, 0));
                Vector2f deltaY = new Vector2f(unitYPosition.x, unitYPosition.y).sub(new Vector2f(screenPosition.x, screenPosition.y));
                angle += Math.atan2(deltaY.y, deltaY.x) - Math.PI * 0.5;
                break;
            case FIXED:
                // Handle fixed rotation mode if needed
                break;
        }

        double spriteRotationAtTime = spriteRotation.valueAt(time);

        double v = InterpolatingFunctions.doubleAngleInterpolator.apply(
                spriteRotationAtTime,
                angle,
                1.0
        );

        double rotation = v + spriteRotation.valueAt(time);

        Vector2f scale = spriteScale.valueAt(time)
                .mul(new Vector2f(worldTransformAt(time).getScale(new Vector3f(ScaleX.valueAt(time), ScaleY.valueAt(time), ScaleZ.valueAt(time))).xy(spriteScale.valueAt(time))))
                .mul((float) (cameraState.getFocusDistance() / screenPosition.w))
                .mul((float) cameraState.getResolutionScale());

        float opacity = 1;//screenPosition.w < 0 ? 0 : object3DState.getOpacity();
        if (useDistanceFade) opacity *= cameraState.opacityAt(screenPosition.w);

        Command cmdOpacity = new Command(Commands.FADE, Easing.LINEAR, (long) time, (long) time, new String[]{String.valueOf(opacity), String.valueOf(opacity)});
        Command cmdMove = new Command(Commands.MOVE, Easing.LINEAR, (long) time, (long) time, new String[]{String.valueOf(screenPosition.x),
                String.valueOf(screenPosition.y), String.valueOf(screenPosition.x), String.valueOf(screenPosition.y)});
        Command cmdVScale = new Command(Commands.VECTOR_SCALE, Easing.LINEAR, (long) time, (long) time, new String[]{String.valueOf(scale.x),
                String.valueOf(scale.y), String.valueOf(scale.x), String.valueOf(scale.y)});
        Command cmdRotate = new Command(Commands.ROTATE, Easing.LINEAR, (long) time, (long) time, new String[]{String.valueOf(rotation),
                String.valueOf(rotation)});
        setEndX(screenPosition.x);
        setEndY(screenPosition.y);

        /*setStartTime(startTime);
        setEndTime(endTime);*/
        this.addCommand(cmdOpacity);
        this.addCommand(cmdMove);
        this.addCommand(cmdVScale);
        this.addCommand(cmdRotate);
        //sprite.Color(time, )
    }

    public enum RotationMode {
        FIXED,
        UNIT_X,
        UNIT_Y
    }
}