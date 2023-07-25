package likelion.halo.hamso.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.halo.hamso.dto.Member.MemberJoinDto;
import likelion.halo.hamso.service.AuthService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void join() throws Exception {
        String loginId = "testId"; // 로그인 아이디

        String password = "1111";

        String name = "testName"; // 회원의 이름

        String phoneNo = "010-1111-1111"; // 전화번호

        String email = "test@email.com"; // 이메일


        mockMvc.perform(post("/auth/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new MemberJoinDto(
                        loginId, password, name, phoneNo, email))))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 - loginId 중복")
    void join_fail() throws Exception {
        String loginId = "testId"; // 로그인 아이디

        String password = "1111";

        String name = "testName"; // 회원의 이름

        String phoneNo = "010-1111-1111"; // 전화번호

        String email = "test@email.com"; // 이메일


        mockMvc.perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberJoinDto(
                                loginId, password, name, phoneNo, email))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

}