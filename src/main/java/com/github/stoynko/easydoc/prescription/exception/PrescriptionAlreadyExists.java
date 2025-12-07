package com.github.stoynko.easydoc.prescription.exception;


import static com.github.stoynko.easydoc.prescription.exception.PrescriptionErrorMessages.PRESCRIPTION_EXISTS;

public class PrescriptionAlreadyExists extends RuntimeException {

  public PrescriptionAlreadyExists() {
    super(PRESCRIPTION_EXISTS.getDisplayName());
  }
}
