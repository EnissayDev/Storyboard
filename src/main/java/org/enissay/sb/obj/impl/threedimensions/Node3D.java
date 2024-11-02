package org.enissay.sb.obj.impl.threedimensions;

import org.enissay.sb.keyframe.InterpolatingFunctions;
import org.enissay.sb.keyframe.KeyframedValue;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;

public interface Node3D {
    public KeyframedValue<Float> PositionX = new KeyframedValue<Float>(InterpolatingFunctions.floatInterpolator);
    public KeyframedValue<Float> PositionY = new KeyframedValue<Float>(InterpolatingFunctions.floatInterpolator);
    public KeyframedValue<Float> PositionZ = new KeyframedValue<Float>(InterpolatingFunctions.floatInterpolator);
    public KeyframedValue<Float> ScaleX = new KeyframedValue<Float>(InterpolatingFunctions.floatInterpolator, 1f);
    public KeyframedValue<Float> ScaleY = new KeyframedValue<Float>(InterpolatingFunctions.floatInterpolator, 1f);
    public KeyframedValue<Float> ScaleZ = new KeyframedValue<Float>(InterpolatingFunctions.floatInterpolator, 1f);
    public KeyframedValue<Quaternionf> Rotation = new KeyframedValue<Quaternionf>(InterpolatingFunctions.quaternionSlerp);

    public default Matrix4f worldTransformAt(double time) {
        // Retrieve the values at the given time
        float scaleX = ScaleX.valueAt(time);
        float scaleY = ScaleY.valueAt(time);
        float scaleZ = ScaleZ.valueAt(time);

        Quaternionf rotation = Rotation.valueAt(time);

        float posX = PositionX.valueAt(time);
        float posY = PositionY.valueAt(time);
        float posZ = PositionZ.valueAt(time);

        // Create matrices for scale, rotation, and translation
        Matrix4f scaleMatrix = new Matrix4f().scaling(scaleX, scaleY, scaleZ);
        Matrix4f rotationMatrix = null;
        if (rotation != null) rotationMatrix = new Matrix4f().rotation(rotation);
        else rotationMatrix = new Matrix4f();
        Matrix4f translationMatrix = new Matrix4f().translation(posX, posY, posZ);

        // Compute the world transform matrix
        return scaleMatrix
                .mul(rotationMatrix)
                .mul(translationMatrix);
    }
}
