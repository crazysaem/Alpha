package com.crazysaem.alpha.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by crazysaem on 08.06.2014.
 */
public class CameraController extends CameraInputController
{
  private final Vector3 tmpV = new Vector3();

  private Vector3 camPosition = new Vector3();
  private Vector3 camPositionXZProjected = new Vector3();

  public CameraController(Camera camera)
  {
    super(camera);
  }

  @Override
  protected boolean process(float deltaX, float deltaY, int button)
  {
    if (button == rotateButton) {
      tmpV.set(camera.direction).crs(camera.up).y = 0f;

      deltaY *= rotateAngle;
      if (deltaY > 0)
      {
        camPosition.x = camera.position.x;
        camPosition.y = camera.position.y;
        camPosition.z = camera.position.z;
        camPosition.nor();
        camPositionXZProjected.x = camera.position.x;
        camPositionXZProjected.z = camera.position.z;
        camPositionXZProjected.nor();
        float currentAngle = (float) Math.acos(camPosition.dot(camPositionXZProjected)) * 57.3f;
        if (currentAngle - deltaY > 5.0f)
          camera.rotateAround(target, tmpV.nor(), deltaY);
      }
      else
      {
        camera.rotateAround(target, tmpV.nor(), deltaY);
      }
      camera.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
    } else if (button == translateButton) {
      camera.translate(tmpV.set(camera.direction).crs(camera.up).nor().scl(-deltaX * translateUnits));
      camera.translate(tmpV.set(camera.up).scl(-deltaY * translateUnits));
      if (translateTarget) target.add(tmpV).add(tmpV);
    } else if (button == forwardButton) {
      camera.translate(tmpV.set(camera.direction).scl(deltaY * translateUnits));
      if (forwardTarget) target.add(tmpV);
    }
    if (autoUpdate) camera.update();
    return true;
  }
}
