package br.leetjourney.neighborshare.domain.model;

import br.leetjourney.neighborshare.domain.enums.CommunityRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
        name = "community_members",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_community", columnNames = {"user_id", "community_id"})
)
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommunityMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunityRole role;

}
