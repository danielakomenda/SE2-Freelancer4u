
TO DO:

- im Ordner "src/main/java/ch/zhaw/freelancer4u/model" die Java-Klasse "Credentials.java" erstellen mit dem folgenden Inhalt:

import lombok.Getter;

@Getter
public class Credentials {

    private String username = "<EMAILADRESSE>";
    private String password = "<PASSWORT>";
    private String openAIKey = "<API-KEY>";
}

Die Ausdrücke in den eckigen Klammern < > müssen entsprechend angepasst werden.


- Im Ordner "freelancer4u-frontend" das File "cypress.env.json" erstellen mit folgendem Inhalt:

{
    "user": {
            "email": "<USER-EMAILADRESSE>",
            "password": "<USER-PASSWORT>"
        },
    "admin": {
            "email": "<ADMIN-EMAILADRESSE>",
            "password": "<ADMIN-PASSWORT>"
        }
}

Die Ausdrücke in den eckigen Klammern < > müssen entsprechend angepasst werden.


