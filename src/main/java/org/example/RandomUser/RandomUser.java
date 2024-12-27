package org.example.RandomUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RandomUser
{
    String gender;
    Name name;
    String email;
    Location location;
    Picture picture;

    class Name
    {
        String title;
        String first;
        String last;
    }

    class Location
    {
        class Street
        {
            String name;
            int number;
        }

        Street street;
        String city, state, country, postcode;
    }

    class Picture
    {
        URI medium;
    }

    String phone;
}