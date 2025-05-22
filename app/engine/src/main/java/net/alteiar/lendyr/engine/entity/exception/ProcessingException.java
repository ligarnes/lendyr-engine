package net.alteiar.lendyr.engine.entity.exception;

public class ProcessingException extends ActionException {

  public ProcessingException(String message) {
    super(message);
  }

  public ProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
