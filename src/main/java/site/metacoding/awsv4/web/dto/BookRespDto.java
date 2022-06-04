package site.metacoding.awsv4.web.dto;

import lombok.Builder;
import lombok.Getter;
import site.metacoding.awsv4.domain.Book;

@Getter
public class BookRespDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    @Builder
    public BookRespDto(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public BookRespDto(Book bookEntity) {
        this.id = bookEntity.getId();
        this.title = bookEntity.getTitle();
        this.content = bookEntity.getContent();
        this.author = bookEntity.getAuthor();
    }
}
