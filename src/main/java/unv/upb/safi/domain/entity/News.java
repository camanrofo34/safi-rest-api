package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @Column(nullable = false)
    private String newsTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String newsContent;

    @Column(nullable = false)
    private String urlNewsImage;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date newsDate;

    @ManyToMany
    @JoinTable(
            name = "news_tag",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Map<Long, Tag> tags;
}
