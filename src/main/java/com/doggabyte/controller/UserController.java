package com.doggabyte.controller;

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
import javax.validation.Valid;
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
            logger.error("ValidateLogin is failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(new LoginStatus(null,loginUser.getType(),null,"error" ));
        }
    }

    @GetMapping("/login/currentUser")
    public ResponseForFront<User> getCurrentUser(HttpServletRequest request) {
        String jwt = authTokenFilter.parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            User user = userRepository.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

            ResponseForFront<User> result = new ResponseForFront<User>();
            result.setSuccess(true);
            result.setData(user);
            return result;
        } else {
            ResponseForFront<User> result = new ResponseForFront<User>();
            result.setSuccess(false);
            result.setData(null);
            return result;
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
