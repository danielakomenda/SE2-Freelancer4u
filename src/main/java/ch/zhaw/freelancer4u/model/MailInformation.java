package ch.zhaw.freelancer4u.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MailInformation {
    private boolean format;
    private boolean alias;
    private String domain;
    private boolean disposable;
    private boolean dns;
}
