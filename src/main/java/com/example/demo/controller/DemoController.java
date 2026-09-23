package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.domain.TestDB;

import com.example.demo.model.service.TestService; // 최상단 서비스 클래스 연동 추가

import java.util.List;

@Controller // 컨트롤러 어노테이션 명시
public class DemoController {

    @GetMapping("/index") // 전송 방식 GET
    public String index() {
        return "index"; // index.html 연결
    }

    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", " 반갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }

    @GetMapping("/hello2") // 전송 방식 GET
    public String hello2(Model model) {
        model.addAttribute("data1", "홍길동님.");
        model.addAttribute("data2", "반갑습니다.");
        model.addAttribute("data3", "오늘.");
        model.addAttribute("data4", "날씨는.");
        model.addAttribute("data5", "매우 좋습니다.");
        return "hello2"; // hello2.html 연결
    }

    // 클래스 하단 작성
    @Autowired
    TestService testService; // DemoController 클래스 아래 객체 주입

    // 하단에 맵핑 이어서 추가
    @GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        // TestDB test = testService.findByName("홍길동");
        // model.addAttribute("data4", test);
        // System.out.println("데이터 출력 디버그 : " + test);
        List<TestDB> users = testService.findAll();
        model.addAttribute("users", users);

        return "testdb";
    }
}
