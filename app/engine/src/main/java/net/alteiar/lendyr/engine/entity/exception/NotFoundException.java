package net.alteiar.lendyr.engine.entity.exception;

public class NotFoundException extends ActionException {

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
