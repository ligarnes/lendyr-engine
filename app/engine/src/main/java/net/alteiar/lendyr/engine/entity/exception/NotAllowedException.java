package net.alteiar.lendyr.engine.entity.exception;

public class NotAllowedException extends ActionException {

  public NotAllowedException(String message) {
    super(message);
  }

  public NotAllowedException(String message, Throwable cause) {
    super(message, cause);
  }
}
