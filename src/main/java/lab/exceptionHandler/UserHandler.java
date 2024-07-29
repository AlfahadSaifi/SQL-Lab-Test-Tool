package lab.exceptionHandler;


import lab.exceptions.user.UserAlreadyExist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class UserHandler {

    @ExceptionHandler(UserAlreadyExist.class)
    @ResponseBody
    public ResponseEntity<String> userAlreadyExist(UserAlreadyExist e){
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex) {
        return "pages/exception/internalServerError";
    }
}
