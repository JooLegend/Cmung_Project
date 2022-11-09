package com.sparta.cmung_project.dto;

import com.sparta.cmung_project.model.Category;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.util.Chrono;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AllPostResponseDto {
    private Long id;
    private String title;
    private String content;

    private String category;

    private String state;
    private int price;

    private List<String> imgs;
    private String createdAt;

    public AllPostResponseDto(Post post,List<String> imgs) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category =post.getCategory().getName();
        this.state = post.getState();
        this.price = post.getPrice();
        this.imgs = imgs;
        this.createdAt = Chrono.timesAgo(post.getCreatedAt());
    }
}
