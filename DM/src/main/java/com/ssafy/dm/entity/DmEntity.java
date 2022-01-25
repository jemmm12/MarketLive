package com.ssafy.dm.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dm")
public class DmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private Long dm_id; // pk

    @ManyToOne
//    @JsonBackReference(value = "sender_id")
    @JsonManagedReference(value = "sender_id")
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private UserEntity senderId; // 송신자

    @ManyToOne
//    @JsonBackReference(value = "receiver_id")
    @JsonManagedReference(value = "receiver_id")
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private UserEntity receiverId; // 수신자

    private Boolean dm_read; // 수신여부
    private String dm_time; // 도착 시간
    private String dm_title; // 제목
    private String dm_msg; // 내용

    @Builder
    public DmEntity(Long dm_id, UserEntity senderId, UserEntity receiverId, Boolean dm_read, String dm_time, String dm_title, String dm_msg) {
        this.dm_id = dm_id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.dm_read = dm_read;
        this.dm_time = dm_time;
        this.dm_title = dm_title;
        this.dm_msg = dm_msg;
    }

}
