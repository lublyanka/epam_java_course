package com.epam.rd.java.basic.topic07.task02;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import org.junit.jupiter.api.*;

import com.epam.rd.java.basic.topic07.task02.Demo;
import com.epam.rd.java.basic.topic07.task02.db.*;
import com.epam.rd.java.basic.topic07.task02.db.entity.*;

/**
 * @author D. Kolesnikov
 */
public class TestUser {

    private static final String CONNECTION_URL = "jdbc:postgresql://0.0.0.0:5433/postgres?user=postgres&password=docker";

    private static final String APP_PROPS_FILE = "app.properties";

    private static final String APP_CONTENT = "connection.url=" + CONNECTION_URL;

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS users ("
                    + "	id SERIAL PRIMARY KEY,"
                    + "	login VARCHAR(10) UNIQUE"
                    + ")";

    private static final String CREATE_TEAMS_TABLE =
            "CREATE TABLE IF NOT EXISTS teams ("
                    + "	id SERIAL PRIMARY KEY,"
                    + "	name VARCHAR(10)"
                    + ")";

    private static final String CREATE_USERS_TEAMS_TABLE =
            "CREATE TABLE IF NOT EXISTS users_teams ("
                    + "	user_id INT REFERENCES users(id) on delete cascade,"
                    + "	team_id INT REFERENCES teams(id) on delete cascade,"
                    + "	UNIQUE (user_id, team_id)"
                    + ")";

    private static final String DROP_USERS_TEAMS_TABLE = "DROP TABLE users_teams";

    private static final String DROP_USERS_TABLE = "DROP TABLE users";

    private static final String DROP_TEAMS_TABLE = "DROP TABLE teams";

    private static Connection con;

    private static String userDefinedAppContent;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        con = DriverManager.getConnection(CONNECTION_URL);
    }

    @AfterAll
    static void globalTearDown() throws SQLException {
        con.close();
    }

    private DBManager dbm;

    @BeforeEach
    void setUp() throws SQLException {
        dbm = DBManager.getInstance();

        con.createStatement().executeUpdate(CREATE_USERS_TABLE);
        con.createStatement().executeUpdate(CREATE_TEAMS_TABLE);
        con.createStatement().executeUpdate(CREATE_USERS_TEAMS_TABLE);
    }

    @AfterEach
    void tearDown() throws SQLException {
        con.createStatement().executeUpdate(DROP_USERS_TEAMS_TABLE);
        con.createStatement().executeUpdate(DROP_USERS_TABLE);
        con.createStatement().executeUpdate(DROP_TEAMS_TABLE);
    }

    @Test
    void testCompliance() throws IOException {
        assertTrue(Files.exists(Path.of("sql/db-create.sql")), "No db-create.sql file was found in a sql directory");

        List<String> lines = Files.readAllLines(Path.of("sql/db-create.sql"));

        assertFalse(lines.size() < 8, "Too small count of lines in a db-create.sql file");
        assertTrue(lines.stream().anyMatch(line -> line.toLowerCase().contains("cascade")),
                "sql/db-create.sql must contain CASCADE word");
    }

    @Test
    void testDemo() throws DBException, SQLException {
        con.createStatement().executeUpdate("insert into users values (DEFAULT, 'ivanov')");
        con.createStatement().executeUpdate("insert into users values (DEFAULT, 'petrov')");
        con.createStatement().executeUpdate("insert into users values (DEFAULT, 'obama')");

        con.createStatement().executeUpdate("insert into teams values (DEFAULT, 'teamA')");
        con.createStatement().executeUpdate("insert into teams values (DEFAULT, 'teamB')");
        con.createStatement().executeUpdate("insert into teams values (DEFAULT, 'teamC')");

        Demo.main(null);
    }

    @Test
    void test1() {
        User user = User.createUser("testUser");
        User user2 = User.createUser("testUser");
        assertEquals("testUser",  user.getLogin());
        assertTrue(user.equals(user2), "Two users must be equaled if their logins are equaled");
    }

    @Test
    void test2() {
        Team team = Team.createTeam("testTeam");
        Team team2 = Team.createTeam("testTeam");
        assertEquals("testTeam",  team.getName());
        assertTrue(team.equals(team2), "Two teams must be equaled if their logins are equaled");
    }

    @Test
    void test3() throws DBException {
        List<User> users = createAndInsertUsers(1, 5);
        List<User> usersFromDB = sort(dbm.findAllUsers(), User::getLogin);
        assertEquals(users, usersFromDB);
    }

    @Test
    void test4() throws DBException {
        List<Team> teams = createAndInsertTeams(1, 5);
        List<Team> teamsFromDB = sort(dbm.findAllTeams(), Team::getName);

        assertEquals(teams, teamsFromDB);
    }

    @Test
    void test5() throws DBException {
        List<User> users = createAndInsertUsers(0, 5);
        List<Team> teams = createAndInsertTeams(0, 5);
        for (int j = 0; j < 5; j++) {
            dbm.setTeamsForUser(users.get(j), teams.subList(0, j + 1).toArray(Team[]::new));
        }

        for (int j = 0; j < 5; j++) {
            List<Team> userTeams = sort(dbm.getUserTeams(users.get(j)), Team::getName);
            assertEquals(teams.subList(0, j + 1), userTeams);
        }
    }

    // util methods

    private static <T, U extends Comparable<? super U>> List<T>
    sort(List<T> items, Function<T, U> extractor) {
        items.sort(Comparator.comparing(extractor));
        return items;
    }

    private List<Team> createAndInsertTeams(int from, int to) throws DBException {
        DBManager dbm = DBManager.getInstance();
        List<Team> teams = IntStream.range(from, to)
                .mapToObj(x -> "team" + x)
                .map(Team::createTeam)
                .collect(Collectors.toList());

        for (Team team : teams) {
            dbm.insertTeam(team);
        }
        return teams;
    }

    private List<User> createAndInsertUsers(int from, int to) throws DBException {
        DBManager dbm = DBManager.getInstance();
        List<User> users = IntStream.range(from, to)
                .mapToObj(x -> "user" + x)
                .map(User::createUser)
                .collect(Collectors.toList());

        for (User user : users) {
            dbm.insertUser(user);
        }
        return users;
    }

}
