package likelion.halo.hamso.configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.halo.hamso.service.AuthService;
import likelion.halo.hamso.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authentication : {}", authentication);

        // token 안보내면 block!
        if(authentication == null || !authentication.startsWith("Bearer ")) {
            log.error("authentication is wrong!");
            filterChain.doFilter(request, response);
            return;
        }

        // Token 꺼내기
        String token = authentication.split(" ")[1];

        // Token Expired 되었는지 여부
        if(JwtUtil.isExpired(token, secretKey)) {
            log.error("Token is Expired!");
            filterChain.doFilter(request, response);
            return;
        }

        // loginId Token 에서 꺼내기
        String loginId = JwtUtil.getLoginId(token, secretKey);
        log.info("loginId = {}", loginId);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, null, List.of(new SimpleGrantedAuthority("USER")));
        // Detail 넣기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
