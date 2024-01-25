package com.example.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home() {
        log.info(SecurityContextHolder.getContext().getAuthentication().getName());
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    // 로그인 한 후에 내가 누군지 확인 하기 위한
    @GetMapping("/my-profile")
    public String myProfile(
            Authentication authentication,
            Model model
    ) {
        model.addAttribute("username",authentication.getName());
        log.info(authentication.getName());
        log.info(((User) authentication.getPrincipal()).getPassword());
        return "my-profile";
    }

    // 회원가입 화면
    @GetMapping("/register")
    public String signUpForm() {
        return "register-form";
    }

    @PostMapping("/register")
    public String signupRequest(
            @RequestParam("username")
            String username,
            @RequestParam("password")
            String password,
            @RequestParam("password-check")
            String passwordCheck
    ) {
        // TODO password == passwordCheck
        if (password.equals(passwordCheck))
            // TODO 주어진 정보를 바탕으로 새로운 사용자 생성
            manager.createUser(User.withUsername(username)
                    .password(passwordEncoder.encode(password))
                    .build());
        // 회원가입 성공 후 로그인 페이지로
        return "redirect:/users/login";
    }
}
