package ru.practicum.ewm.service.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentWithSubsDto extends CommentDto {

    private List<CommentDto> subs;

    public CommentWithSubsDto(Integer id, String text, String authorName, LocalDateTime createdOn, List<CommentDto> subs) {
        super(id, text, authorName, createdOn);
        this.subs = subs;
    }
}
