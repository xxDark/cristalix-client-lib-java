package dev.xdark.clientlib.particle;

@FunctionalInterface
public interface ContextDoubleFunction {

  double apply(ParticleContext ctx);
}
