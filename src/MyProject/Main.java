package MyProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static final String someError = "BaseDate error(%s)";
    private static final String noUser = "Войдите в аккаунт чтобы воспользоваться данной функцией";
    private static final String entryMessage = "Чтобы выйти из программы, введите команду exit\n";
    private static final String endInfo = "Чтобы воспользоваться другими функциями, войдите в аккаунт.";
    private static final String startInfo = "Чтобы войти в аккаунт введите команду Login\n";
    private static final String standardInfo =
            "Чтобы создать аккаунт, введите команду Create user \n" +
            "Чтобы получить информацию о сериале, введите команду Serial info\n" +
            "Чтобы увидеть список названий всех сериалов в нашей базе, введите команду All serials\n" +
            "Чтобы добавить сериал в базу, введите команду Add new serial\n";

    private static final String dopInfo =
            "Чтобы выйти из аккаунта, введите команду Logout\n" +
            "Чтобы увидеть все просмотренные сериалы, введите команду My serials\n"+
            "Чтобы начать просмотр нового сериала, введите команду Start new serial\n"+
            "Чтобы установить место, где вы остановили просмотр введите команду Stop at\n"+
            "Чтобы узнать, где вы остановили просмотр, введите команду Where stopped\n"+
            "Чтобы поставить оценку сериалу, введите команду Put mark\n"+
            "Чтобы изменить пароль аккаунта, введите команду Change password\n";
    public static void main(String[] args) {
        DBManager dbmanager = new DBManager();
        ArrayList<Serial> serials= new ArrayList<>();
        HashMap<String,String> users = new HashMap<>();
        try {
            serials = dbmanager.GetAllSerials();
            users = dbmanager.GetAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String inputString = "";
        boolean logined = false;
        System.out.println(startInfo + standardInfo + entryMessage +endInfo);
        String uLogin;
        String uPassword;
        User user = new User("","",null,null);
        boolean check = true;
        int numberOfSeasons = 0;
        int numberOfEpisodes = 0;
        boolean exist= false;
        String sname = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            while (!inputString.equals("exit")) {
                inputString = reader.readLine();
                switch (inputString) {
                    case ("info"):
                        if (logined){
                            System.out.println(dopInfo + standardInfo + entryMessage);
                        }else {
                            System.out.println(startInfo + standardInfo + entryMessage +endInfo);
                        }
                        break;
                    case ("Login"):
                        if (!logined) {
                            System.out.println("Введите имя пользавателя");
                            inputString = reader.readLine();
                            if (users.containsKey(inputString)) {
                                uLogin = inputString;
                                System.out.println("Введите пароль");
                                for (int i = 0; i < 4; i++) {
                                    inputString = reader.readLine();
                                    if (inputString.equals(users.get(uLogin))) {
                                        System.out.println("Вы успешно вошли в аккаунт");
                                        logined = true;
                                        uPassword =inputString;
                                        try {user =dbmanager.GetMyUser(uLogin,uPassword,serials); }
                                        catch (SQLException e) {e.printStackTrace();}
                                        break;
                                    }else {
                                        System.out.printf("Пароль не верен, у вас осталось %d попыток",3-i);
                                    }
                                }
                            } else {
                                System.out.println("Пользавателя с таким именем не существует.");
                            }

                        }else{System.out.println("Вы уже вошли в аккаунт.");}
                            break;
                    case ("Create user"):
                        System.out.println("Введите имя пользователя");
                        inputString = reader.readLine();
                        if (!users.containsKey(inputString)){
                            uLogin = inputString;
                            System.out.println("Введите пароль");
                            inputString = reader.readLine();
                            boolean badPassword = true;
                            while (badPassword){
                                if ((inputString.length() >= 4) && (inputString.length() <=40)){badPassword =false;}
                                else {
                                    System.out.println("Пароль должен содержать от 4 до 40 символов. Попробуйте ещё раз.");
                                    inputString = reader.readLine();
                                }
                            }
                            User newUser = new User(uLogin,inputString,null,null);
                            try {
                                dbmanager.NewUser(newUser);
                            }catch (SQLException e){ e.printStackTrace();
                                System.out.printf(someError,"NewUser");}

                            users.put(uLogin,inputString);
                            System.out.println("Пользователь создан.");
                        }else{
                            System.out.println("Пользователь с таким именем уже существует.");
                        }
                        break;
                    case ("Serial info"):
                        System.out.println("Введите название сериала");
                        inputString = reader.readLine();
                        for (Serial serial : serials) {
                            if (serial.getName().equals(inputString)) {
                                if (logined){user.infoAboutSerial(inputString,serials);}
                                else {System.out.println(serial.toString());}
                            }
                        }
                        break;
                    case ("All serials"):
                        System.out.println("Список названий всех сериалов в нашей базе:");
                        for (Serial serial : serials) {
                            System.out.println(serial.getName());
                        }
                        break;
                    case ("Add new serial"):
                        System.out.println("Введите название сериала");
                        sname = reader.readLine();
                        check = true;
                        System.out.println("Введите количество сезонов");
                        numberOfSeasons = 0;
                        while (check){
                            inputString = reader.readLine();
                            try {numberOfSeasons = Integer.parseInt(inputString.trim());
                            }catch (NumberFormatException e){System.out.println("Введите целое число");}
                            if (numberOfSeasons>=1){check = false;}
                            else {System.out.println("Число должно быть больше 0.");}
                        }
                        check = true;
                        System.out.println("Введите количество серий в сезоне");
                        numberOfEpisodes = 0;
                        while (check){
                            inputString = reader.readLine();
                            try {numberOfEpisodes = Integer.parseInt(inputString.trim());
                            }catch (NumberFormatException e){System.out.println("Введите целое число");}
                            if (numberOfEpisodes>=1){check = false;}
                            else {System.out.println("Число должно быть больше 0.");}
                        }
                        Serial serial = new Serial(sname,numberOfSeasons,numberOfEpisodes,0,0,0);
                        serials.add(serial);
                        System.out.println("Сериал успешно добавлен");
                        try {
                            dbmanager.NewSerial(serial);
                        }catch (SQLException e){e.printStackTrace();
                            System.out.printf(someError,"Serial");}

                        break;
                    case ("Logout"):
                        if(logined){
                            try{
                                dbmanager.ChangeView(user);
                            }catch (SQLException e){System.out.printf(someError,"ChangeView");}
                            logined = false;
                            user = null;
                            System.out.println("Вы успешно вышли из аккаунта");
                        }
                        else {System.out.println(noUser);}
                        break;
                    case ("My serials"):
                        if(logined){
                            ArrayList<Serial> mySerials = user.getMySerials();
                            for (Serial mySerial:mySerials) {
                                System.out.println(mySerial.getName());
                            }
                        }
                        else {System.out.println(noUser);}
                        break;
                    case ("Start new serial"):
                        if(logined) {
                            System.out.println("Введите название сериала.");
                            inputString = reader.readLine();
                            if(!user.getViewedSerials().containsKey(inputString)){
                            exist = user.StartNewSerial(inputString, serials);
                            if (exist) {
                                try {
                                    dbmanager.NewView(user.getLogin(), inputString);
                                } catch (SQLException e) {
                                    System.out.printf(someError, "NewView");
                                }
                            }
                        }else{System.out.println("Вы уже начали просмотр этого сериала");}
                        }
                        else {System.out.println(noUser);}
                        break;
                    case ("Stop at"):
                        if(logined){
                            System.out.println("Введите название сериала");
                            sname = reader.readLine();
                            check = true;
                            System.out.println("Введите на каком сезоне в остановились");
                            numberOfSeasons = 0;
                            while (check){
                                inputString = reader.readLine();
                                try {numberOfSeasons = Integer.parseInt(inputString.trim());
                                }catch (NumberFormatException e){System.out.println("Введите целое число");}
                                if (numberOfSeasons>=1){check = false;}
                                else {System.out.println("Число должно быть больше 0.");}
                            }
                            check = true;
                            System.out.println("Введите на какой серии в остановились");
                            numberOfEpisodes = 0;
                            while (check){
                                inputString = reader.readLine();
                                try {numberOfEpisodes = Integer.parseInt(inputString.trim());
                                }catch (NumberFormatException e){System.out.println("Введите целое число");}
                                if (numberOfEpisodes>=1){check = false;}
                                else {System.out.println("Число должно быть больше 0.");}
                            }
                            user.stoppedAt(numberOfSeasons,numberOfEpisodes,sname);}
                        else {System.out.println(noUser);}
                        break;
                    case ("Where stopped"):
                        if(logined){
                            System.out.println("Введите название сериала");
                            sname = reader.readLine();
                            user.stoppedWhere(sname);
                        }
                        else {System.out.println(noUser);}
                        break;
                    case ("Put mark"):
                        if(logined){
                            System.out.println("Введите название сериала");
                            sname = reader.readLine();
                            System.out.println("Введите оценку, которую хотите поставить(от 1 до 10)");
                            inputString = reader.readLine();
                            int mark = Integer.parseInt(inputString.trim());
                            user.putMark(sname,mark);
                            System.out.println("Оценка поставлена");
                        }
                        else {System.out.println(noUser);}
                        break;
                    case ("Change password"):
                        if(logined){
                            System.out.println("Введите текущий пароль");
                            String oldPassword = reader.readLine();
                            System.out.println("Введите новый пароль");
                            inputString = reader.readLine();
                            user.ChangePassword(oldPassword,inputString);
                            try{
                            dbmanager.ChangeUser(user);
                            }catch (SQLException e){System.out.printf(someError,"password");}
                            System.out.println("Пароль был успешно изменён");
                        }
                        else {System.out.println(noUser);}
                        break;
                    case ("exit"):break;
                    default:
                        System.out.println("Такой команды нет. Вывод всех команд - info");
                }
            }
        } catch (IOException e) {e.printStackTrace();}
        finally {if(logined){try{dbmanager.ChangeView(user);
        }catch (SQLException e){System.out.printf(someError,"ChangeView");}}
        }
        try{
            dbmanager.ChangeSerials(serials);
        }catch (SQLException e){e.printStackTrace();
            System.out.printf(someError,"AllSerials");}

    }
}
