package com.doggabyte.controller;

import com.doggabyte.exception.InvalidJWTException;
import com.doggabyte.advice.TokenControllerAdvice;
import com.doggabyte.model.RefreshToken;
import com.doggabyte.payload.request.LogOutRequest;
import com.doggabyte.payload.response.*;
import com.doggabyte.model.User;
import com.doggabyte.payload.request.UserLoginDetails;
import com.doggabyte.security.jwt.AuthTokenFilter;
import com.doggabyte.security.jwt.JwtUtils;
import com.doggabyte.security.services.RefreshTokenService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

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
    TokenControllerAdvice tokenControllerAdvice;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/account")
    public ResponseEntity<?> validateLogin(@Valid @RequestBody UserLoginDetails loginUser){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new LoginStatus(jwt, loginUser.getType(), roles, "ok", refreshToken.getToken(),
                userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));

    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            String jwt = authTokenFilter.parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt, response)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                User user = userRepository.findByName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

                LoginResponse<User> result = new LoginResponse<User>();
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
            return ResponseEntity.ok().body(tokenControllerAdvice.handleInvalidJWTException(e));
        }
    }

    @PostMapping("/outLogin")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        LoginResponse<?> result = new LoginResponse<>();
        result.setSuccess(true);
        result.setData(null);
        return ResponseEntity.ok(result);
    }
}
