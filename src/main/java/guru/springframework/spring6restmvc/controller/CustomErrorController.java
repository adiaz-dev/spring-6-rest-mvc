package guru.springframework.spring6restmvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * It allows you to handle exceptions across the whole application, not just to an individual controller.
 * You can think of it as an interceptor of exceptions thrown by methods annotated with
 * @RequestMapping or one of the shortcuts.
 * */
@ControllerAdvice
public class CustomErrorController {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity handleBindErrors(MethodArgumentNotValidException exception) {

    List<Map<String, String>> errorList = exception.getFieldErrors().stream()
        .map(fieldError -> {
          Map<String, String> errorMap = new HashMap<>();
          errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
          return errorMap;
        }).collect(Collectors.toList());

    return ResponseEntity.badRequest().body(errorList);
  }
}
