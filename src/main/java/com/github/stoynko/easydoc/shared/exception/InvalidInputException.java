package com.github.stoynko.easydoc.shared.exception;

import static com.github.stoynko.easydoc.shared.exception.ErrorMessages.INPUT_INVALID;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException() {
    super(INPUT_INVALID.getErrorMessage());
  }
}
