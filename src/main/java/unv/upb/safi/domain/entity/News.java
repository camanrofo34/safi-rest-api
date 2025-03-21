package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @Column(nullable = false)
    @Setter
    private String newsTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String newsContent;

    @Column(nullable = false)
    @Setter
    private String urlNewsImage;

    @Column(nullable = false)
    @Setter
    @Temporal(TemporalType.DATE)
    private Date newsDate;

    @ManyToMany
    @JoinTable(
            name = "news_tag",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Setter
    private Set<Tag> tags;
}
