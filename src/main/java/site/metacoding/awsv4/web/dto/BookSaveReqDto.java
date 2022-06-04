package site.metacoding.awsv4.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.metacoding.awsv4.domain.Book;

@NoArgsConstructor
@Getter
public class BookSaveReqDto {
    private String title;
    private String content;
    private String author;

    @Builder
    public BookSaveReqDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
