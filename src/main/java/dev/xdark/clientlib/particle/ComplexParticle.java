package dev.xdark.clientlib.particle;

import dev.xdark.clientapi.particle.Particle;
import dev.xdark.clientapi.render.RenderGlobal;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;

public final class ComplexParticle<C extends ParticleContext> {

  private final List<ParticlePart<C>> parts = new ReferenceArrayList<>();
  private final C ctx;

  public ComplexParticle(C ctx) {
    this.ctx = ctx;
  }

  public ComplexParticle() {
    this.ctx = (C) new ParticleContext();
  }

  public ComplexParticle<C> register(ParticlePart<C> part) {
    this.parts.add(part);
    return this;
  }

  public ComplexParticle<C> register(ParticlePartBuilder<C> builder) {
    this.parts.add(builder.build());
    return this;
  }

  public void display(RenderGlobal renderGlobal, double x, double y, double z, float yaw,
      float pitch, float prevYaw, float prevPitch, int tick) {
    List<ParticlePart<C>> parts = this.parts;
    ParticleContext ctx = this.ctx;
    ctx.x = x;
    ctx.y = y;
    ctx.z = z;
    ctx.rotationYaw = yaw;
    ctx.rotationPitch = pitch;
    ctx.prevRotationYaw = prevYaw;
    ctx.prevRotationPitch = prevPitch;
    ctx.currentTick = tick;
    ctx.setup();
    for (int i = 0, j = parts.size(); i < j; i++) {
      ParticlePart part = parts.get(i);
      if (!part.predicate.test(ctx)) {
        continue;
      }
      Particle particle = renderGlobal.spawnParticle(part.type, true, x + part.xOffset.apply(ctx),
          y + part.yOffset.apply(ctx), z + part.zOffset.apply(ctx),
          part.xSpeed.apply(ctx),
          part.ySpeed.apply(ctx), part.zSpeed.apply(ctx), part.data);
      if (particle != null) {
        part.setup.accept(particle);
      }
    }
  }
}
