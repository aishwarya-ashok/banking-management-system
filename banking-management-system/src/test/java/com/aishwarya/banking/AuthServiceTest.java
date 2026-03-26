package com.aishwarya.banking;

import com.aishwarya.banking.dto.AuthResponse;
import com.aishwarya.banking.dto.LoginRequest;
import com.aishwarya.banking.dto.RegisterRequest;
import com.aishwarya.banking.entity.Role;
import com.aishwarya.banking.entity.User;
import com.aishwarya.banking.repository.UserRepository;
import com.aishwarya.banking.service.AuthService;
import com.aishwarya.banking.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertEquals("token", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = User.builder().email("test@example.com").name("Test").role(Role.USER).password("encoded").build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, user.getPassword()));
        when(jwtUtil.generateToken(userDetails)).thenReturn("token");

        AuthResponse response = authService.login(request);

        assertEquals("token", response.getToken());
    }
}
