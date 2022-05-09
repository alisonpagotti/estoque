package br.com.xbrain.estoque.modulos.config.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundExceptions(Exception ex, WebRequest request) {

        List<String> detalhes = new ArrayList<>();
        detalhes.add(ex.getLocalizedMessage());
        var error = new ErrorResponse("Não foi possível realizar essa operação!", detalhes);

        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        var detalhes = ex.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());

        var error = new ErrorResponse("Erro ao cadastrar!", detalhes);

        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, IllegalArgumentException.class, ArithmeticException.class})
    public final ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {

        List<String> detalhes = new ArrayList<>();
        detalhes.add(ex.getLocalizedMessage());
        var error = new ErrorResponse("Não foi possível realizar essa operação!", detalhes);

        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
