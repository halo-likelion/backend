package likelion.halo.hamso.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.halo.hamso.dto.Member.MemberJoinDto;
import likelion.halo.hamso.dto.Member.MemberLoginDto;
import likelion.halo.hamso.exception.InvalidPasswordException;
import likelion.halo.hamso.exception.MemberDuplicateException;
import likelion.halo.hamso.exception.MemberNotFoundException;
import likelion.halo.hamso.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    @WithAnonymousUser
    void join() throws Exception {
        String loginId = "testId"; // 로그인 아이디

        String password = "1111";

        String name = "testName"; // 회원의 이름

        String phoneNo = "010-1111-1111"; // 전화번호

        String email = "test@email.com"; // 이메일


        mockMvc.perform(post("/auth/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberJoinDto(
                                loginId, password, name, phoneNo, email))))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 - loginId 중복")
    @WithAnonymousUser
    void join_fail() throws Exception {
        String loginId = "testId"; // 로그인 아이디

        String password = "1111";

        String name = "testName"; // 회원의 이름

        String phoneNo = "010-1111-1111"; // 전화번호

        String email = "test@email.com"; // 이메일

        when(authService.join(any()))
                .thenThrow(new MemberDuplicateException("해당 userId가 중복됩니다."));


        mockMvc.perform(post("/auth/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberJoinDto(
                                loginId, password, name, phoneNo, email))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

//    @Test
//    @DisplayName("로그인 성공")
//    @WithAnonymousUser
//    void login_success() throws Exception{
//        String loginId = "testId"; // 로그인 아이디
//
//        String password = "1111";
//
//        when(authService.login(any()))
//                .thenReturn("token");
//
//        mockMvc.perform(post("/auth/login")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberLoginDto(
//                                loginId, password))))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("로그인 실패 - loginId 없음")
    void login_fail1() throws Exception{
        String loginId = "testId"; // 로그인 아이디

        String password = "1111";

        when(authService.login(any()))
                .thenThrow(new MemberNotFoundException("loginId 없음 - 로그인 불가"));

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberLoginDto(
                                loginId, password))))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("로그인 실패 - password 틀린 경우")
    void login_fail2() throws Exception{
        String loginId = "testId"; // 로그인 아이디

        String password = "1111";

        when(authService.login(any()))
                .thenThrow(new InvalidPasswordException("password 틀림 - 로그인 불가"));

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberLoginDto(
                                loginId, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}