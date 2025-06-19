package com.example.domain.entity;


import com.example.domain.common.BaseEntity;
import com.example.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Getter
@DynamicUpdate // update시 null인경우 쿼리를 보내지 않음
@DynamicInsert // insert시 null인경우 쿼리를 보내지 않음
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
@AllArgsConstructor // 모든 매개변수 생성자
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public void encodePassword(String password) {
        this.password = password;
    }
}