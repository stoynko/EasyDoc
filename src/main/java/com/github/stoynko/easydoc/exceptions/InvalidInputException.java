package com.github.stoynko.easydoc.exceptions;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.INPUT_INVALID;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException() {
    super(INPUT_INVALID.getErrorMessage());
  }
}
