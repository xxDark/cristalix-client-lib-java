package dev.xdark.clientlib.particle;

import dev.xdark.clientapi.particle.Particle;
import dev.xdark.clientapi.util.ParticleType;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public final class ParticlePartBuilder<C extends ParticleContext> {

  private static final int[] NO_DATA = {};
  private static final ContextDoubleFunction ZERO = constant(0.0D);
  private static final Consumer<Particle> NO_OP = __ -> {};

  private ContextDoubleFunction xOffset = ZERO;
  private ContextDoubleFunction yOffset = ZERO;
  private ContextDoubleFunction zOffset = ZERO;
  private ContextDoubleFunction xSpeed = ZERO;
  private ContextDoubleFunction ySpeed = ZERO;
  private ContextDoubleFunction zSpeed = ZERO;
  private Predicate<C> tickPredicate;
  private ParticleType type;
  private int[] data = NO_DATA;
  private Consumer<Particle> setup = NO_OP;

  private ParticlePartBuilder() {
  }

  public static <C extends ParticleContext> ParticlePartBuilder<C> builder() {
    return new ParticlePartBuilder<>();
  }

  public ParticlePartBuilder<C> reset() {
    this.xOffset = ZERO;
    this.yOffset = ZERO;
    this.zOffset = ZERO;
    this.xSpeed = ZERO;
    this.ySpeed = ZERO;
    this.zSpeed = ZERO;
    this.data = NO_DATA;
    this.setup = NO_OP;
    return this;
  }

  public ParticlePartBuilder<C> xOffset(ContextDoubleFunction offset) {
    this.xOffset = offset;
    return this;
  }

  public ParticlePartBuilder<C> yOffset(ContextDoubleFunction offset) {
    this.yOffset = offset;
    return this;
  }

  public ParticlePartBuilder<C> zOffset(ContextDoubleFunction offset) {
    this.zOffset = offset;
    return this;
  }

  public ParticlePartBuilder<C> xOffset(DoubleSupplier offset) {
    this.xOffset = constant(offset);
    return this;
  }

  public ParticlePartBuilder<C> yOffset(DoubleSupplier offset) {
    this.yOffset = constant(offset);
    return this;
  }

  public ParticlePartBuilder<C> zOffset(DoubleSupplier offset) {
    this.zOffset = constant(offset);
    return this;
  }

  public ParticlePartBuilder<C> xOffset(double offset) {
    this.xOffset = constant(offset);
    return this;
  }

  public ParticlePartBuilder<C> yOffset(double offset) {
    this.yOffset = constant(offset);
    return this;
  }

  public ParticlePartBuilder<C> zOffset(double offset) {
    this.zOffset = constant(offset);
    return this;
  }

  public ParticlePartBuilder<C> offset(ContextDoubleFunction offset) {
    this.xOffset = offset;
    this.yOffset = offset;
    this.zOffset = offset;
    return this;
  }

  public ParticlePartBuilder<C> offset(DoubleSupplier offset) {
    return this.offset(constant(offset));
  }

  public ParticlePartBuilder<C> offset(double offset) {
    return this.offset(constant(offset));
  }

  public ParticlePartBuilder<C> xSpeed(ContextDoubleFunction speed) {
    this.xSpeed = speed;
    return this;
  }

  public ParticlePartBuilder<C> ySpeed(ContextDoubleFunction speed) {
    this.ySpeed = speed;
    return this;
  }

  public ParticlePartBuilder<C> zSpeed(ContextDoubleFunction speed) {
    this.zSpeed = speed;
    return this;
  }

  public ParticlePartBuilder<C> xSpeed(DoubleSupplier speed) {
    this.xSpeed = constant(speed);
    return this;
  }

  public ParticlePartBuilder<C> ySpeed(DoubleSupplier speed) {
    this.ySpeed = constant(speed);
    return this;
  }

  public ParticlePartBuilder<C> zSpeed(DoubleSupplier speed) {
    this.zSpeed = constant(speed);
    return this;
  }

  public ParticlePartBuilder<C> xSpeed(double speed) {
    this.xSpeed = constant(speed);
    return this;
  }

  public ParticlePartBuilder<C> ySpeed(double speed) {
    this.ySpeed = constant(speed);
    return this;
  }

  public ParticlePartBuilder<C> zSpeed(double speed) {
    this.zSpeed = constant(speed);
    return this;
  }

  public ParticlePartBuilder<C> speed(ContextDoubleFunction speed) {
    this.xSpeed = speed;
    this.ySpeed = speed;
    this.zSpeed = speed;
    return this;
  }

  public ParticlePartBuilder<C> speed(DoubleSupplier speed) {
    return this.speed(constant(speed));
  }

  public ParticlePartBuilder<C> speed(double speed) {
    return this.speed(constant(speed));
  }

  public ParticlePartBuilder<C> tickPredicate(Predicate<C> tickPredicate) {
    this.tickPredicate = tickPredicate;
    return this;
  }

  public ParticlePartBuilder<C> tickPredicate(IntPredicate tickPredicate) {
    this.tickPredicate = ctx -> tickPredicate.test(ctx.currentTick);
    return this;
  }

  public ParticlePartBuilder<C> each(int ticks) {
    this.tickPredicate = ParticlePart.each(ticks);
    return this;
  }

  public ParticlePartBuilder<C> type(ParticleType type) {
    this.type = type;
    return this;
  }

  public ParticlePartBuilder<C> data(int... data) {
    this.data = data;
    return this;
  }

  public ParticlePartBuilder<C> setup(Consumer<Particle> setup) {
    this.setup = setup;
    return this;
  }

  public ParticlePart<C> build() {
    return new ParticlePart<>(this.xOffset, this.yOffset, this.zOffset, this.xSpeed, this.ySpeed,
        this.zSpeed, this.tickPredicate, this.type, this.data, this.setup);
  }

  private static ContextDoubleFunction constant(double value) {
    return __ -> value;
  }

  private static ContextDoubleFunction constant(DoubleSupplier supplier) {
    return __ -> supplier.getAsDouble();
  }
}