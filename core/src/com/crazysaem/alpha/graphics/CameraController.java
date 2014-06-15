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

  private Vector3 position = new Vector3();
  private Vector3 direction = new Vector3();
  private Vector3 up = new Vector3();

  public CameraController(Camera camera)
  {
    super(camera);
  }

  @Override
  protected boolean process(float deltaX, float deltaY, int button)
  {
    if (button == rotateButton) {
      tmpV.set(camera.direction).crs(camera.up).y = 0f;
      //TODO: Find a better way to check for places were the cam is not supposed to go
      storeCameraValues();
      camera.rotateAround(target, tmpV.nor(), deltaY * rotateAngle);
      if (camera.position.y <= 0.65f)
        restoreCameraValues();
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

  private void storeCameraValues()
  {
    position.x = camera.position.x; position.y = camera.position.y; position.z = camera.position.z;
    direction.x = camera.direction.x; direction.y = camera.direction.y; direction.z = camera.direction.z;
    up.x = camera.up.x; up.y = camera.up.y; up.z = camera.up.z;
  }

  private void restoreCameraValues()
  {
    camera.position.x = position.x; camera.position.y = position.y; camera.position.z = position.z;
    camera.direction.x = direction.x; camera.direction.y = direction.y; camera.direction.z = direction.z;
    camera.up.x = up.x; camera.up.y = up.y; camera.up.z = up.z;
  }
}
