package com.junhyeong.chatchat;

import com.junhyeong.chatchat.interceptors.AuthenticationInterceptor;
import com.junhyeong.chatchat.utils.HttpUtil;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ChatchatApplication {
	@Value("${jwt.secret}")
	private String secret;

	public static void main(String[] args) {
		SpringApplication.run(ChatchatApplication.class, args);
	}

	@Bean
	public WebSecurityCustomizer ignoringCustomizer() {
		return (web) -> web.ignoring().antMatchers("/**");
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("*")
						.allowedOriginPatterns("*")
						.allowCredentials(true);
			}

			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(authenticationInterceptor());
			}
		};
	}

	@Bean
	public AuthenticationInterceptor authenticationInterceptor() {
		return new AuthenticationInterceptor(jwtUtil());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Argon2PasswordEncoder();
	}

	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil(secret);
	}

	@Bean
	public HttpUtil httpUtil() {
		return new HttpUtil();
	}
}
