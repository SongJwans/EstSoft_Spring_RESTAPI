package com.estsoft.springproject.blog.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private String title;
    private String body;

    @Override
    public String toString() {
        return "title:'" + title + ", body:" + body + "\n";

    }
}
