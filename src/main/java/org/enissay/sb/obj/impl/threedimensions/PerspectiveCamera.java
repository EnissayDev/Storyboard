package org.enissay.sb.obj.impl.threedimensions;

import org.enissay.sb.keyframe.InterpolatingFunctions;
import org.enissay.sb.keyframe.KeyframedValue;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PerspectiveCamera extends Camera {
    private final KeyframedValue<Float> positionX = new KeyframedValue<>(InterpolatingFunctions.floatInterpolator);
    private final KeyframedValue<Float> positionY = new KeyframedValue<>(InterpolatingFunctions.floatInterpolator);
    private final KeyframedValue<Float> positionZ = new KeyframedValue<>(InterpolatingFunctions.floatAngleInterpolator);
    private final KeyframedValue<Vector3f> targetPosition = new KeyframedValue<>(InterpolatingFunctions.vector3Interpolator);
    private final KeyframedValue<Vector3f> up = new KeyframedValue<>(InterpolatingFunctions.vector3Interpolator, new Vector3f(0, 1, 0));

    private final KeyframedValue<Float> nearClip = new KeyframedValue<>(InterpolatingFunctions.floatAngleInterpolator);
    private final KeyframedValue<Float> nearFade = new KeyframedValue<>(InterpolatingFunctions.floatInterpolator);
    private final KeyframedValue<Float> farFade = new KeyframedValue<>(InterpolatingFunctions.floatInterpolator);
    private final KeyframedValue<Float> farClip = new KeyframedValue<>(InterpolatingFunctions.floatInterpolator);

    private final KeyframedValue<Float> horizontalFov = new KeyframedValue<>(InterpolatingFunctions.floatInterpolator);
    private final KeyframedValue<Float> verticalFov = new KeyframedValue<>(InterpolatingFunctions.floatInterpolator);

    @Override
    public CameraState stateAt(double time) {
        float aspectRatio = (float) this.aspectRatio;

        Vector3f cameraPosition = new Vector3f(positionX.valueAt(time), positionY.valueAt(time), positionZ.valueAt(time));
        Vector3f targetPosition = this.targetPosition.valueAt(time);
        Vector3f up = this.up.valueAt(time).normalize();

        float fovY;
        if (horizontalFov.getCount() > 0) {
            float fovX = (float) Math.toRadians(horizontalFov.valueAt(time));
            fovY = 2 * (float) Math.atan(Math.tan(fovX * 0.5) / aspectRatio);
        } else {
            fovY = verticalFov.getCount() > 0
                    ? (float) Math.toRadians(verticalFov.valueAt(time))
                    : 2 * (float) Math.atan(storyboardSize.y * 0.5 / Math.max(0.0001f, cameraPosition.distance(targetPosition)));
        }

        float focusDistance = (float) (resolution.y * 0.5 / Math.tan(fovY * 0.5));
        float nearClip = this.nearClip.getCount() > 0 ? this.nearClip.valueAt(time) : Math.min(focusDistance * 0.5f, 1);
        float farClip = this.farClip.getCount() > 0 ? this.farClip.valueAt(time) : focusDistance * 1.5f;

        float nearFade = this.nearFade.getCount() > 0 ? this.nearFade.valueAt(time) : nearClip;
        float farFade = this.farFade.getCount() > 0 ? this.farFade.valueAt(time) : farClip;

        Matrix4f view = new Matrix4f().lookAt(cameraPosition, targetPosition, up);
        Matrix4f projection = new Matrix4f().perspective(fovY, aspectRatio, nearClip, farClip);

        return new CameraState(view.mul(projection), aspectRatio, focusDistance, resolutionScale, nearClip, nearFade, farFade, farClip);
    }

    public KeyframedValue<Float> getPositionX() {
        return positionX;
    }

    public KeyframedValue<Float> getPositionY() {
        return positionY;
    }

    public KeyframedValue<Float> getPositionZ() {
        return positionZ;
    }

    public KeyframedValue<Vector3f> getTargetPosition() {
        return targetPosition;
    }
}