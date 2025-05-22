package net.alteiar.lendyr.server.grpc.v1.mapper;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class RandomProvider {
  public static final RandomProvider INSTANCE = new RandomProvider();

  private final PodamFactory factory = new PodamFactoryImpl();

  public <E> E nextObject(Class<E> clazz) {
    return factory.manufacturePojo(clazz);
  }
}
