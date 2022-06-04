package site.metacoding.awsv4.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import site.metacoding.awsv4.domain.Book;
import site.metacoding.awsv4.domain.BookRepository;

// select 테스트는 order를 먼저해야 좋다. 
// @Transactional 어노테이션을 붙이면 스프링 트랜잭션과 JUnit 트랜잭션이 분리된다.
// 그래서 RestTemplate로 직접 호출했을 때 = 스프링 트랜잭션
// BookRepository로 호출했을 때 = JUnit 트랜잭션
// auto_increment 초기화를 하려면 @Transactional 어노테이션이 붙어야 하는데 붙이면 트랜잭션이 따로 돌아서 더 복잡해져서 하지말자!!

@ActiveProfiles("dev")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    // @Autowired
    // private EntityManager em;

    @AfterEach
    public void tearDown() throws Exception {
        bookRepository.deleteAll();

        // 이거 붙이고 테스트 하려면 Repository나 Service 레이어에서 단위테스트할 때 사용하자!!
        // em.createNativeQuery("ALTER TABLE book COLUMN id RESTART WITH
        // 1").executeUpdate();
    }

    @Order(1)
    @Test
    public void 책목록보기_테스트() {

        List<Book> books = Arrays.asList(
                new Book("제목1", "내용1", "메타코딩"),
                new Book("제목2", "내용2", "메타코딩"));
        bookRepository.saveAll(books);

        // 테스트 시작
        ResponseEntity<String> response = restTemplate.exchange("/api/book",
                HttpMethod.GET, null, String.class);
        System.out.println("================================================================================");
        System.out.println(response.getBody());
        System.out.println("================================================================================");
        // 테스트 검증
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title1 = dc.read("$.[0].title");
        String title2 = dc.read("$.[1].title");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("제목1", title1);
        assertEquals("제목2", title2);
    }

    @Order(2)
    @Test
    public void 책등록_테스트() throws Exception {

        // 데이터 준비
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = new ObjectMapper().writeValueAsString(new Book("제목3", "내용3", "메타코딩"));
        HttpEntity<String> request = new HttpEntity<String>(body, headers);

        // 테스트 시작
        ResponseEntity<String> response = restTemplate.exchange("/api/book", HttpMethod.POST, request, String.class);
        System.out.println("================================================================================");
        System.out.println(response.getBody());
        System.out.println("================================================================================");

        // 테스트 검증
        DocumentContext dc = JsonPath.parse(response.getBody());
        Long id = dc.read("$.id", Long.class); // read할때 꼭 캐스팅해줘야 한다. 무조건 Integer로 변환해서 받음.
        String title = dc.read("$.title");
        String content = dc.read("$.content");
        String author = dc.read("$.author");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(3L, id);
        assertEquals("제목3", title);
        assertEquals("내용3", content);
        assertEquals("메타코딩", author);
    }

}
