package org.enissay.sb.obj.impl.threedimensions;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CameraState {
    public final Matrix4f viewProjection;
    public final double aspectRatio;
    public final double focusDistance;
    public final double resolutionScale;
    public final double nearClip;
    public final double nearFade;
    public final double farFade;
    public final double farClip;

    public CameraState(Matrix4f viewProjection, double aspectRatio, double focusDistance, double resolutionScale,
                       double nearClip, double nearFade, double farFade, double farClip) {
        this.viewProjection = viewProjection;
        this.aspectRatio = aspectRatio;
        this.focusDistance = focusDistance;
        this.resolutionScale = resolutionScale;
        this.nearClip = nearClip;
        this.nearFade = nearFade;
        this.farFade = farFade;
        this.farClip = farClip;
    }

    public Vector4f toScreen(Matrix4f transform, Vector3f point) {
        // Assuming OsuHitObject.StoryboardSize.Y is available; replace it with appropriate values
        float storyboardSizeY = 1.0f; // Placeholder
        float storyboardSizeX = 1.0f; // Placeholder

        Vector2f scale = new Vector2f((float) (storyboardSizeY * aspectRatio), storyboardSizeY);
        float offset = (scale.x - storyboardSizeX) * 0.5f;

        Vector4f transformedPoint = new Vector4f(point, 1.0f).mul(transform);
        Vector2f ndc = new Vector2f(transformedPoint.x, transformedPoint.y).div(Math.abs(transformedPoint.w));

        Vector2f screenPosition = ndc.add(new Vector2f(1.0f)).mul(0.5f).mul(scale);
        float depth = transformedPoint.z / transformedPoint.w;

        return new Vector4f(screenPosition.x - offset, screenPosition.y, depth, transformedPoint.w);
    }

    public double linearizeZ(double z) {
        return (2 * nearClip) / (farClip + nearClip - z * (farClip - nearClip));
    }

    public float opacityAt(float distance) {
        if (distance < nearFade) {
            return (float) Math.max(0, Math.min((distance - nearClip) / (nearFade - nearClip), 1));
        } else if (distance > farFade) {
            return (float) Math.max(0, Math.min((farClip - distance) / (farClip - farFade), 1));
        }
        return 1.0f;
    }

    public Matrix4f getViewProjection() {
        return viewProjection;
    }

    public double getFocusDistance() {
        return focusDistance;
    }

    public double getResolutionScale() {
        return resolutionScale;
    }
}