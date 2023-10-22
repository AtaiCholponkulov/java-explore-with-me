package ru.practicum.stats.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class ViewStatsDto {

    private final String app;
    private final String uri;
    private Integer hits;

    public ViewStatsDto(String app, String uri) {
        this.app = app;
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ViewStatsDto that = (ViewStatsDto) o;
        return Objects.equals(app, that.app) && Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri);
    }
}
