package com.sparta.cmung_project.controller;

import com.sparta.cmung_project.dto.PetRequestDto;
import com.sparta.cmung_project.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor // 기본 생성자를 만들어줍니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
@RequestMapping("/api")
public class MypageController {
    private final MypageService mypageService;

    @GetMapping("/mypage")
    public ResponseEntity<?> getUserInfo() throws RuntimeException {
        return ResponseEntity.ok(mypageService.getUserInfo());
    }

    @GetMapping("/mypage/posts")
    public ResponseEntity<?> getUserPosts(@RequestParam(name = "type", defaultValue = "1") int typeId) throws RuntimeException {
        return ResponseEntity.ok(mypageService.getUserPosts(typeId));
    }

    @PostMapping(value = "/mypagge/pet")
    public ResponseEntity<?> createPet(@RequestBody PetRequestDto requestDto) throws RuntimeException {
        return ResponseEntity.ok(mypageService.createPet(requestDto));
    }

    @PutMapping(value = "/mypagge/pet/{petId}")
    public ResponseEntity<?> updatePet(@PathVariable Long petId, @RequestBody PetRequestDto requestDto) throws RuntimeException {
        return ResponseEntity.ok(mypageService.updatePet(petId, requestDto));
    }

    @DeleteMapping("/mypagge/pet/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {
        return ResponseEntity.ok(mypageService.deletePet(petId));
    }

    @GetMapping("/mypage/pet")
    public ResponseEntity<?> getPet() throws RuntimeException {
        return ResponseEntity.ok(mypageService.getPet());
    }
}
