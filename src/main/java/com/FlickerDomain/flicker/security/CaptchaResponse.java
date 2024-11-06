@PostMapping("/register")
public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
    if (!captchaService.verifyCaptcha(request.getCaptchaResponse())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Captcha verification failed");
    }
    userService.register(request);
    return ResponseEntity.ok("User registered successfully");
}
