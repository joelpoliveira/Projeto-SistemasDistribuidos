
package projeto.sd.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import projeto.sd.model.Game;
import projeto.sd.model.Player;
import projeto.sd.model.Team;
import projeto.sd.model.User;
import projeto.sd.service.GameService;
import projeto.sd.service.PlayerService;
import projeto.sd.service.TeamService;
import projeto.sd.service.UserService;

@Controller
public class HomeController {
    @Autowired
    GameService gameService;

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @Autowired
    UserService userService;

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

    @GetMapping("/")
    public String homepage(Model model, HttpSession session) {
        model.addAttribute("games", gameService.getAllGames());

        if (session.getAttribute("username") != null)
            model.addAttribute("username", session.getAttribute("username"));
        else
            model.addAttribute("username", "");
        return "index";
    }

    @GetMapping("/403")
    public String AcessDenied() {
        return "403";
    }

    @GetMapping("/fill")
    public String fillDatabase() {
        // Add Admin
        User admin = new User("admin", "admin", "admin@scoredei.pt", null);
        admin.setRoles("ADMIN");
        userService.add(admin);

        ArrayList<Team> teams = new ArrayList<>();
        ArrayList<String> equipas = new ArrayList<>();
        equipas.add("Sporting");
        equipas.add("Benfica");
        equipas.add("Porto");
        equipas.add("Boavista");
        equipas.add("Maritimo");
        equipas.add("Rio Ave");
        equipas.add("Portimonense");

        for (String e : equipas) {
            Team t = new Team(e, "", "");
            teamService.add(t);
            teams.add(t);
        }

        teams.get(0).setImageUrl(
                "https://upload.wikimedia.org/wikipedia/en/thumb/e/e1/Sporting_Clube_de_Portugal_%28Logo%29.svg/1200px-Sporting_Clube_de_Portugal_%28Logo%29.svg.png");
        teams.get(1).setImageUrl(
                "https://upload.wikimedia.org/wikipedia/en/thumb/a/a2/SL_Benfica_logo.svg/1200px-SL_Benfica_logo.svg.png");
        teams.get(2).setImageUrl("https://upload.wikimedia.org/wikipedia/pt/c/c5/F.C._Porto_logo.png");
        teams.get(3).setImageUrl("https://upload.wikimedia.org/wikipedia/pt/5/5c/Logo_Boavista_FC.png");
        teams.get(4).setImageUrl("https://upload.wikimedia.org/wikipedia/pt/a/a2/Logo_CS_Maritimo.png");
        teams.get(5).setImageUrl("https://upload.wikimedia.org/wikipedia/pt/f/f5/Logo_Rio_Ave.png");
        teams.get(6).setImageUrl(
                "https://upload.wikimedia.org/wikipedia/en/thumb/c/c0/Portimonense_Sporting_Clube_logo.svg/1200px-Portimonense_Sporting_Clube_logo.svg.png");

        for (Team t : teams) {
            playerService.add(new Player("A", "Striker", null, t));
            playerService.add(new Player("B", "Striker", null, t));
            playerService.add(new Player("C", "Striker", null, t));
            playerService.add(new Player("D", "Striker", null, t));
        }

        gameService.add(new Game("Lisboa", null, teams.get(0), teams.get(1)));
        gameService.add(new Game("Lisboa", null, teams.get(0), teams.get(2)));
        gameService.add(new Game("Lisboa", null, teams.get(3), teams.get(1)));

        return "redirect:/";
    }

    @GetMapping("/fill-API")
    public String fillDatabaseFromAPI() {
        String APIKey = "0d277b56911c64405b959d8fb5688549";
        Map<String, Team> allTeams = new HashMap<String, Team>();

        // Teams
        HttpResponse<JsonNode> response = Unirest.get("https://v3.football.api-sports.io/teams?league=94&season=2021")
                .header("x-apisports-key", APIKey)
                .asJson();

        JsonObject jsonObject = new JsonParser().parse(response.getBody().toString()).getAsJsonObject();

        for (JsonElement t : jsonObject.get("response").getAsJsonArray()) {

            JsonElement team = t.getAsJsonObject().get("team").getAsJsonObject();
            JsonElement venue = t.getAsJsonObject().get("venue").getAsJsonObject();

            Team newTeam = new Team(team.getAsJsonObject().get("name").getAsString(),
                    team.getAsJsonObject().get("logo").getAsString(),
                    venue.getAsJsonObject().get("city").getAsString());
            teamService.add(newTeam);

            allTeams.put(team.getAsJsonObject().get("name").getAsString(), newTeam);
        }

        // Players
        int page = 1;
        int total_pages = Integer.MAX_VALUE;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        response = Unirest.get("https://v3.football.api-sports.io/players?league=94&season=2021&page=" + page)
                .header("x-apisports-key", APIKey)
                .asJson();

        jsonObject = new JsonParser().parse(response.getBody().toString()).getAsJsonObject();
        total_pages = jsonObject.get("paging").getAsJsonObject().get("total").getAsInt();

        while (page <= total_pages) {

            for (JsonElement json : jsonObject.get("response").getAsJsonArray()) {
                JsonElement player = json.getAsJsonObject().get("player").getAsJsonObject();
                JsonElement statistics = json.getAsJsonObject().get("statistics").getAsJsonArray().get(0)
                        .getAsJsonObject();

                Player newPlayer = new Player();
                newPlayer.setName(player.getAsJsonObject().get("name").getAsString());

                try {
                    // check if birth is null
                    if (!player.getAsJsonObject().get("birth").getAsJsonObject().get("date").isJsonNull()) {
                        newPlayer.setBirthDate(formatter
                                .parse(player.getAsJsonObject().get("birth").getAsJsonObject().get("date")
                                        .getAsString()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                newPlayer.setPosition(
                        statistics.getAsJsonObject().get("games").getAsJsonObject().get("position").getAsString());
                // try {
                // newPlayer.setTeam(teamService
                // .getTeam(statistics.getAsJsonObject().get("team").getAsJsonObject().get("name")
                // .getAsString())
                // .get());
                // } catch (NotFoundException | NoSuchElementException e) {
                // System.out.println("No team Found");
                // }

                newPlayer.setTeam(allTeams
                        .get(statistics.getAsJsonObject().get("team").getAsJsonObject().get("name").getAsString()));

                playerService.add(newPlayer);
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Failed to sleep");
            }

            System.out.println("Done page" + page + "of " + total_pages);

            page++;
            response = Unirest.get("https://v3.football.api-sports.io/players?league=94&season=2021&page="
                    + page)
                    .header("x-apisports-key", APIKey)
                    .asJson();

            jsonObject = new JsonParser().parse(response.getBody().toString()).getAsJsonObject();
        }

        return "redirect:/";
    }

}
