package org.enissay.sb.obj.impl.threedimensions;

import org.joml.Vector2d;

public abstract class Camera {
    public Vector2d storyboardSize = new Vector2d(640, 480);
    public Vector2d resolution = new Vector2d(1366, 768);
    public double resolutionScale = storyboardSize.y / resolution.y;
    public double aspectRatio = resolution.x / resolution.y;

    public float distanceForHorizontalFov(double fov)
    {
        return (float)(1366 * 0.5 / Math.tan(Math.toRadians(fov) * 0.5));
    }
    public float distanceForVerticalFov(double fov)
    {
        return (float)(1366 * 0.5 / Math.tan(Math.toRadians(fov) * 0.5));
    }

    public abstract CameraState stateAt(double time);
}