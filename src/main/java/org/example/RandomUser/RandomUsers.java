package org.example.RandomUser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.Joke.Joke;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
public class RandomUsers
{
    static HttpClient client;
    static HttpRequest request;
    static Gson gson;
    //private static List<RandomUser> usersData;

    public static void getTenRandUsersToJson(String filename)
    {
        try
        {
            client = HttpClient.newHttpClient();
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://randomuser.me/api/?results=10"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String dataArray = response.body().substring(response.body().indexOf("["), response.body().lastIndexOf("]") + 1);

            gson = new Gson();
            Type usersListType = new TypeToken<List<RandomUser>>(){}.getType();
            List<RandomUser> usersInput = gson.fromJson(dataArray, usersListType);

            gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(filename))
            {
                gson.toJson(usersInput, writer);
                System.out.println("Данные успешно записаны");
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void getTenRandUsersToXML(String filename)
    {
        try
        {
            client = HttpClient.newHttpClient();
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://randomuser.me/api/?results=10"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String dataArray = response.body().substring(response.body().indexOf("["), response.body().lastIndexOf("]") + 1);

            gson = new Gson();
            Type usersListType = new TypeToken<List<RandomUser>>(){}.getType();
            List<RandomUser> usersInput = gson.fromJson(dataArray, usersListType);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("RandomUsersList");
            document.appendChild(root);

            usersInput.forEach(user ->
            {
                Element userEl = document.createElement("randomUser");

                Element genderEl = document.createElement("gender");
                genderEl.appendChild(document.createTextNode(user.getGender()));
                userEl.appendChild(genderEl);

                Element userNameEl = document.createElement("name");
                Element titleNameEl = document.createElement("title");
                titleNameEl.appendChild(document.createTextNode(user.getName().title));
                userNameEl.appendChild(titleNameEl);

                Element firstNameEl = document.createElement("first");
                firstNameEl.appendChild(document.createTextNode(user.getName().first));
                userNameEl.appendChild(firstNameEl);

                Element lastNameEl = document.createElement("last");
                lastNameEl.appendChild(document.createTextNode(user.getName().last));
                userNameEl.appendChild(lastNameEl);
                userEl.appendChild(userNameEl);

                Element phoneEl = document.createElement("phone");
                phoneEl.appendChild(document.createTextNode(user.getPhone()));
                userEl.appendChild(phoneEl);

                Element pictureEl = document.createElement("picture");
                pictureEl.appendChild(document.createTextNode(user.getPicture().medium.toString()));
                userEl.appendChild(pictureEl);

                Element emailEl = document.createElement("email");
                emailEl.appendChild(document.createTextNode(user.getEmail()));
                userEl.appendChild(emailEl);

                Element locationEl = document.createElement("location");

                Element streetEl = document.createElement("street");

                Element streetNameEl = document.createElement("name");
                streetNameEl.appendChild(document.createTextNode(user.getLocation().street.name));
                streetEl.appendChild(streetNameEl);

                Element numberStreetEl = document.createElement("number");
                numberStreetEl.appendChild(document.createTextNode(String.valueOf(user.getLocation().street.number)));
                streetEl.appendChild(numberStreetEl);

                locationEl.appendChild(streetEl);

                userEl.appendChild(locationEl);

                root.appendChild(userEl);
            });

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(filename);
            transformer.transform(source, result);

            System.out.println("Запись в XML файл завершена");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


    }
}