package com.github.stoynko.easydoc.exceptions;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException(ErrorMessages errorMessage) {
    super(errorMessage.getErrorMessage());
  }
}
