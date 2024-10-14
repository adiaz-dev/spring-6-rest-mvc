package guru.springframework.spring6restmvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * This advice applies for all the controllers define in the same package as this class is, so to all
 * controllers inside the same package
 * */
//@ControllerAdvice
public class ExceptionController {

  //exception handlers
  //@ExceptionHandler(NotFoundException.class)
  public ResponseEntity notFound() {
    System.out.println("Not found exception to be sent by BeerController");
    return new ResponseEntity(HttpStatus.NOT_FOUND);
  }

}
