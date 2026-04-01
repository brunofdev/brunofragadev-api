package com.brunofragadev.module.article.domain.entity;

import com.brunofragadev.shared.domain.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articles", indexes = {
        @Index(name = "idx_article_slug", columnList = "slug")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "id")
public class Article extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 255)
    private String subtitle;

    @Column(nullable = false, unique = true, length = 150)
    private String slug;

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ArticleStatus status;

    @Column(name = "font_family", length = 50)
    private String fontFamily;

    @Column(name = "content_html", columnDefinition = "LONGTEXT", nullable = false)
    private String contentHtml;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content_json", columnDefinition = "JSON", nullable = false)
    private String contentJson;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "article_tags", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "tag", length = 50)
    private Set<String> tags = new HashSet<>();

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    public static Article criar(String title, String subtitle, String slug, String coverImage,
                                String fontFamily, String contentHtml, String contentJson, Set<String> tags) {

        validarDadosCriacao(title, slug, contentHtml, contentJson);

        Article article = new Article();
        article.title = title;
        article.subtitle = subtitle;
        article.slug = slug.toLowerCase();
        article.coverImage = coverImage;
        article.fontFamily = fontFamily;
        article.contentHtml = contentHtml;
        article.contentJson = contentJson;
        article.tags = (tags != null) ? tags : new HashSet<>();
        article.status = ArticleStatus.RASCUNHO;
        return article;
    }

    private static void validarDadosCriacao(String title, String slug, String html, String json) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("O título é obrigatório");
        if (slug == null || slug.isBlank()) throw new IllegalArgumentException("O slug é obrigatório");
        if (html == null || html.isBlank()) throw new IllegalArgumentException("O conteúdo HTML não pode ser vazio");
        if (json == null || json.isBlank()) throw new IllegalArgumentException("A estrutura JSON é obrigatória");

        if (title.length() > 150) throw new IllegalArgumentException("Título excede 150 caracteres");
        if (!slug.matches("^[a-z0-9-]+$")) throw new IllegalArgumentException("Slug com formato inválido");
    }

    public void publish() {
        if (this.status == ArticleStatus.PUBLICADO) return;
        this.status = ArticleStatus.PUBLICADO;
        this.publishedAt = LocalDateTime.now();
    }
    public void atualizar(String title, String subtitle, String slug, String coverImage,
                          String fontFamily, String contentHtml, String contentJson,
                          Set<String> tags, ArticleStatus status) {

        validarDadosCriacao(title, slug, contentHtml, contentJson);

        this.title = title;
        this.subtitle = subtitle;
        this.slug = slug;
        this.coverImage = coverImage;
        this.fontFamily = fontFamily;
        this.contentHtml = contentHtml;
        this.contentJson = contentJson;
        this.tags = tags;
        this.status = status;

        if (status == ArticleStatus.PUBLICADO && this.publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
    }

}