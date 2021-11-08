package com.doggabyte.controller;

import com.doggabyte.exception.InvalidJWTException;
import com.doggabyte.advice.TokenControllerAdvice;
import com.doggabyte.exception.TokenRefreshException;
import com.doggabyte.model.ERole;
import com.doggabyte.model.RefreshToken;
import com.doggabyte.model.Role;
import com.doggabyte.payload.request.LogOutRequest;
import com.doggabyte.payload.request.SignupRequest;
import com.doggabyte.payload.request.TokenRefreshRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
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

    @PostMapping("/signin")
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(HttpStatus.CONFLICT.value(), "Error: Username is already taken!", false));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse
                            (HttpStatus.CONFLICT.value(), "Error: Email is already in use!", false));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.guest)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.admin)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "user" -> {
                        Role adminRole = roleRepository.findByName(ERole.user)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.guest)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        user.setAccess("admin");
        user.setAddress("beijing");
        user.setCountry("China");
        user.setPhone("010-88888888");
        user.setTitle("manager");
        user.setAvatar("https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getName());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

}
