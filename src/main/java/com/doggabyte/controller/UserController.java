package com.doggabyte.controller;

import com.doggabyte.exception.InvalidJWTException;
import com.doggabyte.exception.RestExceptionHandler;
import com.doggabyte.exception.UserNotFoundException;
import com.doggabyte.model.ErrorResponse;
import com.doggabyte.model.User;
import com.doggabyte.payload.request.UserLoginDetails;
import com.doggabyte.payload.response.LoginStatus;
import com.doggabyte.payload.response.ResponseForFront;
import com.doggabyte.security.jwt.AuthTokenFilter;
import com.doggabyte.security.jwt.JwtUtils;
import com.doggabyte.security.services.UserDetailsImpl;
import com.doggabyte.repository.RoleRepository;
import com.doggabyte.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthTokenFilter authTokenFilter;

    @Autowired
    RestExceptionHandler restExceptionHandler;

    @PostMapping("/login/account")
    public ResponseEntity<?> validateLogin(@Valid @RequestBody UserLoginDetails loginUser){

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new LoginStatus(jwt, loginUser.getType(), roles, "ok"));
        }catch (Exception e) {
            return restExceptionHandler.handleInvalidLoginException(e);
      }
    }

    @GetMapping("/login/currentUser")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            String jwt = authTokenFilter.parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt, response)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                User user = userRepository.findByName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

                ResponseForFront<User> result = new ResponseForFront<User>();
                result.setSuccess(true);
                result.setData(user);
                return ResponseEntity.ok(result);
            }else{
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse
                                (HttpStatus.OK.value(), "当前用户已过期，请重新登录！", false));
            }
        } catch (InvalidJWTException e) {
            return restExceptionHandler.handleInvalidJWTException(e);
        }
    }

    @PostMapping("/login/outLogin")
    public ResponseForFront<?> outLogin(HttpServletRequest request){
        ResponseForFront<?> result = new ResponseForFront<>();
        result.setSuccess(true);
        result.setData(null);
        return result;
    }
}
