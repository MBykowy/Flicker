
package com.FlickerDomain.flicker.security;
import com.FlickerDomain.flicker.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostMapping("/register")
public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
    if (!captchaService.verifyCaptcha(request.getCaptchaResponse())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Captcha verification failed");
    }
    userService.register(request);
    return ResponseEntity.ok("User registered successfully");
}
