package org.janusvault.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordEntry {
    private String title;
    private String site;
    private String login;
    private String password;
    private String note;

    private String formatNote(){
        if(note != null)
            return String.format("| Note: %s", note);
        return "";
    }

    @Override
    public String toString() {
        return String.format("[%s] Site: %s | Login: %s | Password: %s %s",
                title, site, login, password, formatNote());
    }
}
