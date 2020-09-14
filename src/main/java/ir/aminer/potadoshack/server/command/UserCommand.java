package ir.aminer.potadoshack.server.command;

import ir.aminer.potadoshack.server.User;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

public class UserCommand extends Command {

    @Override
    public void execute(List<Argument> arguments) {
        File clientsDir = new File("./clients/");
        if (!clientsDir.exists() || !clientsDir.isDirectory()) {
            System.err.println("There is not clients folder");
            return;
        }

        if (arguments.size() == 0) {
            help();
            return;
        }

        if (!arguments.get(0).isCommand())
            return;

        if (arguments.get(0).getValue().equalsIgnoreCase("show")) {
            if (arguments.size() >= 2 && arguments.get(1).isCommand())
                show(arguments.get(1).getValue());
            else if (arguments.size() == 1)
                show();

        } else if (arguments.get(0).getValue().equalsIgnoreCase("remove")
                && arguments.size() >= 2
                && arguments.get(1).isCommand())
            remove(arguments.get(1).getValue());
        else
            help();

    }

    private void remove(String username) {
        if (User.hasUserFilePref(username)) {
            if (User.getUserFilePref(username).delete())
                System.out.println("removed!");
        } else
            System.err.println("No user with the given username was found");
    }

    private void show() {

        File clientsDir = new File("./clients/");
        File[] userFiles = Objects.requireNonNull(clientsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pref")));


        System.out.println("USERNAME \t\t| LAST LOGIN");
        for (File userFile : userFiles) {
            System.out.println(userFile.getName() + "\t\t| "
                    + LocalDateTime.ofInstant(Instant.ofEpochMilli(userFile.lastModified()), ZoneId.systemDefault()));
        }
    }

    private void show(String username) {
        User user;
        try {
            user = User.fromUsername(username);
        } catch (IllegalStateException exception) {
            System.err.println("No user with the given username was found");
            return;
        }

        System.out.println("==============================");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("First name: " + user.getFirstName());
        System.out.println("Last name: " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("PhoneNumber: " + user.getPhoneNumber().toString());
        System.out.println("==============================");
    }

    @Override
    public String help() {
        return "user <command>:\n" +
                "  commands:\n" +
                "    show:\t Shows the list of users and their register date.\n" +
                "    show <username>:\t Shows the user info.\n" +
                "    remove <username>:\t removes the specified username.";
    }

    @Override
    protected String getCode() {
        return "users";
    }
}
