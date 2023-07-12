package com.transport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Email {
    private String sender;
    private List<String> recipients;
    private String msgBody;
    private String subject;
    private String attachment;
}
