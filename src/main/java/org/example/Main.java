package org.example;
import org.example.Joke.Jokes;

public class Main
{
    public static void main(String[] args)
    {
        Jokes.getJokeTypes("jokeTypes.json");
        //Jokes.getAllTypeJokes("allTypeJokes.json");
    }
}