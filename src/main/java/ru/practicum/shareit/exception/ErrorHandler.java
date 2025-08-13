package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

        @ExceptionHandler
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorResponse handleIllegalArgument(final IllegalArgumentException ex) {
                return new ErrorResponse("Неверный аргумент", ex.getMessage());
        }

        @ExceptionHandler
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ErrorResponse handleNotFoundException(final NotFoundException exception) {
                return new ErrorResponse("Объект не найден", exception.getMessage());
        }

        @ExceptionHandler
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ErrorResponse handleUnexpectedError(final RuntimeException exception) {
                return new ErrorResponse("Произошла непредвиденная ошибка", exception.getMessage());
        }
}
