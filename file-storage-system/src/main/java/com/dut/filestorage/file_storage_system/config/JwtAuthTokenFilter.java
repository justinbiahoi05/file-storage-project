package com.dut.filestorage.file_storage_system.config;

import com.dut.filestorage.file_storage_system.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Override
     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // GẮN CAMERA SỐ 1: Kiểm tra xem Filter có chạy không
        System.out.println("====================================================");
        System.out.println(">>> JWT FILTER IS RUNNING FOR URI: " + request.getRequestURI());

        try {
            String jwt = parseJwt(request);

            // GẮN CAMERA SỐ 3: Kiểm tra token sau khi đã xử lý
            System.out.println(">>> PARSED JWT: " + jwt);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                System.out.println(">>> TOKEN IS VALID. PROCEEDING TO SET AUTHENTICATION...");
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println(">>> SUCCESSFULLY SET AUTHENTICATION FOR USER: " + username);
            } else {
                System.out.println(">>> TOKEN IS NULL OR INVALID.");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
            System.out.println("!!! EXCEPTION IN JWT FILTER: " + e.getMessage());
        }
        
        System.out.println("====================================================");
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        // GẮN CAMERA SỐ 2: Kiểm tra Header Authorization
        System.out.println(">>> AUTHORIZATION HEADER: " + headerAuth);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
