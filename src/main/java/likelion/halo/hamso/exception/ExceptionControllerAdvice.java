package likelion.halo.hamso.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleCntException(MemberNotFoundException ex) {
        return new ErrorResponse(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(), ex.getMessage());
    }

    @ExceptionHandler(MemberDuplicateException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleCntException(MemberDuplicateException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }


    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Parameter cannot be null");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
