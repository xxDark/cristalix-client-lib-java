package dev.xdark.clientlib.particle;

public class ParticleContext {

  public double x, y, z;
  public float rotationYaw, rotationPitch;
  public float prevRotationYaw, prevRotationPitch;
  public int currentTick;

  protected ParticleContext() {
  }

  protected void setup() { }
}
