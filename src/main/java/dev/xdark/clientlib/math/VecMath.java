package dev.xdark.clientlib.math;

import dev.xdark.clientapi.math.MathHelper;
import dev.xdark.clientapi.math.Vec3d;

public final class VecMath {

  private VecMath() {
  }

  public static void direction(Vec3d vec, float yaw, float pitch) {
    float xRadians = (float) Math.toRadians(yaw);
    float yRadians = (float) Math.toRadians(pitch);
    double xz = MathHelper.cos(yRadians);
    vec.set(-xz * Math.sin(xRadians), -MathHelper.sin(yRadians), xz * MathHelper.cos(xRadians));
  }

  public static void normalize(Vec3d vec) {
    double x = vec.getX(), y = vec.getY(), z = vec.getZ();
    double d0 = Math.sqrt(x * x + y * y + z * z);
    if (d0 < 1.0E-4D) {
      vec.set(0.0, 0.0, 0.0);
    } else {
      vec.set(x / d0, y / d0, z / d0);
    }
  }
}
