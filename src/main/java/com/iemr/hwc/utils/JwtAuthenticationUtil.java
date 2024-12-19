package com.iemr.hwc.utils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.iemr.hwc.data.login.Users;
import com.iemr.hwc.repo.login.UserLoginRepo;
import com.iemr.hwc.utils.exception.IEMRException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthenticationUtil {

	@Autowired
	private CookieUtil cookieUtil;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	public JwtAuthenticationUtil(CookieUtil cookieUtil, JwtUtil jwtUtil) {
		this.cookieUtil = cookieUtil;
		this.jwtUtil = jwtUtil;
	}

	public ResponseEntity<String> validateJwtToken(HttpServletRequest request) {
		Optional<String> jwtTokenOpt = cookieUtil.getCookieValue(request, "Jwttoken");

		if (jwtTokenOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Error 401: Unauthorized - JWT Token is not set!");
		}

		String jwtToken = jwtTokenOpt.get();

		// Validate the token
		Claims claims = jwtUtil.validateToken(jwtToken);
		if (claims == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error 401: Unauthorized - Invalid JWT Token!");
		}

		// Extract username from token
		String usernameFromToken = claims.getSubject();
		if (usernameFromToken == null || usernameFromToken.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Error 401: Unauthorized - Username is missing!");
		}

		// Return the username if valid
		return ResponseEntity.ok(usernameFromToken);
	}

	public boolean validateUserIdAndJwtToken(String jwtToken) throws IEMRException {
		try {
			// Validate JWT token and extract claims
			Claims claims = jwtUtil.validateToken(jwtToken);

			if (claims == null) {
				throw new IEMRException("Invalid JWT token.");
			}

			String userId = claims.get("userId", String.class);

			// Check if user data is present in Redis
			Users user = getUserFromCache(userId);
			if (user == null) {
				throw new IEMRException("Invalid User ID.");
			}

			return true; // Valid userId and JWT token
		} catch (Exception e) {
			logger.error("Validation failed: " + e.getMessage(), e);
			throw new IEMRException("Validation error: " + e.getMessage(), e);
		}
	}

	private Users getUserFromCache(String userId) {
		String redisKey = "user_" + userId; // The Redis key format
		Users user = (Users) redisTemplate.opsForValue().get(redisKey);

		if (user == null) {
			logger.warn("User not found in Redis.");
		} else {
			logger.info("User fetched successfully from Redis.");
		}

		return user; // Returns null if not found
	}
}
