package br.com.xbrain.estoque.modulos.config.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
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

        List<String> detalhes = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            detalhes.add(error.getDefaultMessage());
        }
        var error = new ErrorResponse("Erro ao cadastrar!", detalhes);

        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ArithmeticException.class)
    public final ResponseEntity<Object> handleArithmeticExceptions(Exception ex, WebRequest request) {

        List<String> detalhes = new ArrayList<>();
        detalhes.add(ex.getLocalizedMessage());
        var error = new ErrorResponse("Não foi possível realizar essa operação!", detalhes);

        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> handleUnexpectedTypeExceptions(Exception ex, WebRequest request) {

        List<String> detalhes = new ArrayList<>();
        detalhes.add(ex.getLocalizedMessage());
        var error = new ErrorResponse("Não foi possível realizar essa operação!", detalhes);

        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgumentExceptions(Exception ex, WebRequest request) {

        List<String> detalhes = new ArrayList<>();
        detalhes.add(ex.getLocalizedMessage());
        var error = new ErrorResponse("Não foi possível realizar essa operação!", detalhes);

        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
