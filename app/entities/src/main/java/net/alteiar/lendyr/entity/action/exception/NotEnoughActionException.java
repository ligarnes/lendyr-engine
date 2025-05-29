package net.alteiar.lendyr.entity.action.exception;

public class NotEnoughActionException extends ActionException {

  public NotEnoughActionException(String message) {
    super(message);
  }

  public NotEnoughActionException(String message, Throwable cause) {
    super(message, cause);
  }
}
