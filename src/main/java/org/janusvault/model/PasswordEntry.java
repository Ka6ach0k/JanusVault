package org.janusvault.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordEntry {
    private char[] title;
    private char[] site;
    private char[] login;
    private char[] password;
    private char[] note;

    public void clean() {
        destroyField(this.title);
        destroyField(this.site);
        destroyField(this.login);
        destroyField(this.password);
        destroyField(this.note);
    }
    private void destroyField(char[] array){
        if(array != null)
            Arrays.fill(array, '\0');
    }
}
