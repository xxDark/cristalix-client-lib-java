package dev.xdark.clientlib.particle;

import dev.xdark.clientapi.particle.Particle;
import dev.xdark.clientapi.util.ParticleType;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;

public final class ParticlePart<C extends ParticleContext> {

  final ContextDoubleFunction xOffset;
  final ContextDoubleFunction yOffset;
  final ContextDoubleFunction zOffset;
  final ContextDoubleFunction xSpeed;
  final ContextDoubleFunction ySpeed;
  final ContextDoubleFunction zSpeed;
  final Predicate<C> predicate;
  final ParticleType type;
  final int[] data;
  final Consumer<Particle> setup;

  ParticlePart(ContextDoubleFunction xOffset, ContextDoubleFunction yOffset,
      ContextDoubleFunction zOffset, ContextDoubleFunction xSpeed,
      ContextDoubleFunction ySpeed,
      ContextDoubleFunction zSpeed, Predicate<C> tickPredicate,
      ParticleType type,
      int[] data,
      Consumer<Particle> setup) {
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.zOffset = zOffset;
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
    this.zSpeed = zSpeed;
    this.predicate = tickPredicate;
    this.type = type;
    this.data = data;
    this.setup = setup;
  }

  public static DoubleSupplier constant(double value) {
    return () -> value;
  }

  public static <C extends ParticleContext> Predicate<C> each(int ticks) {
    return ctx -> ctx.currentTick % ticks == 0;
  }
}
