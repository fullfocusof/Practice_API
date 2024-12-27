package org.example.Joke;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

import java.io.FileWriter;
import java.io.IOException;

@Setter
@Getter
@AllArgsConstructor
public class Jokes
{
    static HttpClient client;
    static HttpRequest request;
    static Gson gson;
    private static Map<String, List<Joke>> jokesData;

    public static void getJokeTypes(String outputFilename)
    {
        jokesData = new HashMap<>();

        try
        {
            client = HttpClient.newHttpClient();
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://official-joke-api.appspot.com/jokes/random/100"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            gson = new Gson();
            Type jokesListType = new TypeToken<List<Joke>>() {}.getType();
            List<Joke> jokesInput = gson.fromJson(response.body(), jokesListType);

            jokesData = jokesInput.stream()
                    .collect(Collectors.toMap(Joke::getSetup, joke -> joke, (exist, replace) -> exist))
                    .values().stream()
                    .collect(Collectors.toMap(Joke::getType, joke -> new ArrayList<>(Collections.singletonList(joke)),
                            (existingList, newList) ->
                            {
                                existingList.addAll(newList);
                                return existingList;
                            }
                            ));

            saveJokesDataToJson(outputFilename);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static Map<String, List<Joke>> readDataFromJson(String filename)
    {
        try (FileReader reader = new FileReader(filename))
        {
            Type listType = new TypeToken<Map<String, List<Joke>>>() {}.getType();
            return gson.fromJson(reader, listType);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return new HashMap<>();
        }
    }

    public static void saveJokesDataToJson(String filename)
    {
        Map<String, List<Joke>> beforeData = new HashMap<>();
        File file = new File(filename);
        if (file.exists())
        {
            beforeData = readDataFromJson(filename);
        }
        beforeData.putAll(jokesData);

        gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename))
        {
            gson.toJson(beforeData, writer);
            System.out.println("Данные успешно записаны");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static List<String> getAllJokeTypes()
    {
        try
        {
            client = HttpClient.newHttpClient();
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://official-joke-api.appspot.com/types"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            gson = new Gson();
            Type stringListType = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(response.body(), stringListType);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public static void getAllTypeJokes(String outputFilename)
    {
        File file = new File(outputFilename);
        if (file.exists() && file.delete()) System.out.println("Удален раннее существующий файл");

        String mainURI = "https://official-joke-api.appspot.com/jokes/{jokeType}/ten";
        jokesData = new HashMap<>();
        List<String> jokeTypes = getAllJokeTypes();
        jokeTypes.forEach(jokeType ->
        {
            String curURI = mainURI.replace("{jokeType}", jokeType);

            try
            {
                client = HttpClient.newHttpClient();
                request = HttpRequest.newBuilder()
                        .uri(new URI(curURI))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                gson = new Gson();
                Type jokesListType = new TypeToken<List<Joke>>() {}.getType();
                List<Joke> jokesInput = gson.fromJson(response.body(), jokesListType);

                jokesData = jokesInput.stream()
                        .collect(Collectors.toMap(Joke::getType, joke -> new ArrayList<>(Collections.singletonList(joke)),
                                (existingList, newList) ->
                                {
                                    existingList.addAll(newList);
                                    return existingList;
                                }
                        ));

                saveJokesDataToJson(outputFilename);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        });

        saveJokesDataToJson(outputFilename);
    }
}