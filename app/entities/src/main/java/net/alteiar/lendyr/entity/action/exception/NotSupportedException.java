package net.alteiar.lendyr.entity.action.exception;

public class NotSupportedException extends ActionException {

  public NotSupportedException(String message) {
    super(message);
  }

  public NotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }
}
