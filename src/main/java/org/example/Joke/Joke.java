package org.example.Joke;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Joke
{
    int id;
    String type, setup, punchline;
}