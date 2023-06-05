package ru.netology;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws Exception {

        Server server = new Server();

        server.addHandler("GET", "/classic.html",
                (request, responseStream) -> {
                    Path path = Path.of(".", "/public", request.getPath());
                    try {
                        final var template = Files.readString(path);
                        responseStream.write(template.replace(
                                "{time}",
                                LocalDateTime.now().toString()).getBytes());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                });

        server.addHandler("GET", "/forms.html", (request, responseStream) -> {
            Path path = Path.of(".", "/public", request.getPath());
            if (request.getQueryParams() != null) {
                //System.out.println(request.getQueryParams());
                responseStream.write("<p>Query params are:<br>".getBytes());
                request.getQueryParams().forEach(s -> {
                    try {
                        responseStream.write((s.toString() + "<br>").getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                responseStream.write("</p>".getBytes());
            } else {
                responseStream.write(Files.readString(path).getBytes());
            }
        });
        server.listen(9999);
    }
}


