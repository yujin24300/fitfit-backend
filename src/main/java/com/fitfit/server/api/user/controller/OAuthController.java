package com.fitfit.server.api.user.controller;


import com.fitfit.server.api.user.service.OAuthService;
import com.fitfit.server.global.exception.ApiResponse;
import com.fitfit.server.global.exception.CustomException;
import com.fitfit.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<?>> validateIdToken(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        try {
            // 인증된 사용자의 정보를 받아오고 JWT를 발급
            Map<String, Object> userDetails = oAuthService.authenticateUser(idToken);
            return ResponseEntity.ok(ApiResponse.success(userDetails));
        } catch (IllegalArgumentException e) {
            ApiResponse<?> response = ApiResponse.fail(new CustomException(ErrorCode.UNAUTHORIZED, "Invalid token"));
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getHttpStatus()).body(response);
        } catch (Exception e) {
            ApiResponse<?> response = ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Error validating token"));
            return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(response);
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> getAccessToken(@RequestBody String accessToken) {
        try {
            Map<String, Object> tokenResponse = oAuthService.getAccessTokenFromAuthCode(accessToken);
            return ResponseEntity.ok(tokenResponse);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Access token error: " + e.getMessage());
        }
    }
}