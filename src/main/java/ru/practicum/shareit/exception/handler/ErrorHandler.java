package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.AccessNotAllowed;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.exception.NotValidRequestException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("Input data  is not valid. Error:{}. Stack trace :", e.getFieldError().getDefaultMessage());
        e.printStackTrace();
        //Добавила логгирование с выводом стейктрейса. Правильно я поняла задание?
        //Но существенно лучше в поиске причин неправильной работы не стало. Или я что-то не понимаю?
        return new ErrorResponse(e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException e) {
        log.error("Input data  is not valid. Error:{}. Stack trace :", e.getLocalizedMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValidRequest(final NotValidRequestException e) {
        log.error("Input data  is not valid. Error:{}. Stack trace :", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoEntityException(final NoEntityException e) {
        log.error("Entity not found. Error:{}. Stack trace :", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistException(final AlreadyExistException e) {
        log.error("The data already exist. Error:{}. Stack trace :", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAccessNotAllowedException(final AccessNotAllowed e) {
        log.error("Access not allowed. Error:{}. Stack trace : ", e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.getMessage());
    }

}