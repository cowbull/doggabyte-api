package com.doggabyte.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.doggabyte.model.ErrorResponse;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setErrorMessage(ex.getMessage());
        errorResponse.setSuccess(false);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.OK.value());
        errorResponse.setErrorMessage("Invalid JWT signature");
        errorResponse.setSuccess(false);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.OK.value());
        errorResponse.setErrorMessage("Invalid JWT token");
        errorResponse.setSuccess(false);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.OK.value());
        errorResponse.setErrorMessage("WT token is expired");
        errorResponse.setSuccess(false);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.OK.value());
        errorResponse.setErrorMessage("JWT token is unsupported");
        errorResponse.setSuccess(false);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.OK.value());
        errorResponse.setErrorMessage("JWT claims string is empty");
        errorResponse.setSuccess(false);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidJWTException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJWTException(InvalidJWTException ex){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setErrorMessage("当前用户Token无效或已过期，请重新登录！");
        errorResponse.setSuccess(false);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoginException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.FORBIDDEN.value());
        errorResponse.setErrorMessage("账户或密码错误");
        errorResponse.setSuccess(false);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
