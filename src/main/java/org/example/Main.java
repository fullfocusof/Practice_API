package org.example;
import org.example.Joke.Jokes;
import org.example.RandomUser.RandomUsers;

public class Main
{
    public static void main(String[] args)
    {
        //Jokes.getJokeTypes("jokeTypes.json");
        //Jokes.getAllTypeJokes("allTypeJokes.json");
        RandomUsers.getTenRandUsersToJson("10users.json");
        //RandomUsers.getTenRandUsersToXML("10users.xml");
    }
}