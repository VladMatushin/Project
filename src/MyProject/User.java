package MyProject;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String login;
    private  String password;
    private ArrayList<Serial> mySerials= new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> viewedSerials = new HashMap<>();
    private static final String noSuchSerial = "Сериала с таким именем нет в нашей базе данных.";



    public void ChangePassword (String oldPassword, String newPassword){
        if ((oldPassword.equals(this.password)) && (newPassword.length() >= 4) && (newPassword.length() <=40)){
            this.password = newPassword;
        }else {
                if (!(oldPassword.equals(this.password))) {System.out.println("Вы неправельно ввели изначальный пароль.");}
                else {System.out.println("Длинна нового пароля не соответствует условиям длинны пароля.");}
        }
    }

    public boolean StartNewSerial(String sname,ArrayList<Serial> serials){
        boolean exist = false;
        for (Serial serial:serials) {
            if (serial.getName().equals(sname)){
                mySerials.add(serial);
                serial.newViewer();
                ArrayList<Integer> standard = new ArrayList<>();
                standard.add(1);
                standard.add(1);
                standard.add(0);
                viewedSerials.put(serial.getName(),standard);
                exist =true;
                break;
            }
        }
        if (exist){
            System.out.println("Сериал был добавлен в ваш список сериалов.");
        }else{
            System.out.println(noSuchSerial);
        }
        return exist;
    }

    public void stoppedAt(int atSeason, int atEpisode,String sname){
            if (viewedSerials.containsKey(sname)){
                boolean exist = true;
                for (Serial serial:mySerials) {
                    if (serial.getName().equals(sname)){
                        if ((serial.getNumberOfSeasons() >= atSeason)&&(serial.getNumberOfEpisodes() >= atEpisode)){
                            ArrayList<Integer> standard = viewedSerials.get(sname);
                            standard.set(0,atSeason);
                            standard.set(1,atEpisode);
                            viewedSerials.put(sname,standard);
                            exist =false;
                        }
                        break;
                    }
                    }
                if (exist){
                    System.out.println("В этом сериале нет такого количества сезонов или серий.");
                }else {
                    System.out.println("Номер сезона и эпизода были успешно записаны.");
                }

            }else{System.out.println(noSuchSerial);}

    }



/*boolean exist = false;
        for (Serial serial:mySerials) {
            if (serial.getName().equals(sname)){
                exist =true;
                break;
            }
        }*/

    public void stoppedWhere(String sname){
        if (viewedSerials.containsKey(sname)) {
            ArrayList<Integer> standard = viewedSerials.get(sname);
            System.out.printf("В сериале %s вы остановились на %d сезоне %d серии.", sname, standard.get(0), standard.get(1));
        }else {System.out.println("Вы не начинали просмотр сериала с таким названием.");}
    }

    public void putMark(String sname,int mark){
        if (viewedSerials.containsKey(sname)) {
            if ((mark >= 1) && (mark <= 10)) {
                for (Serial serial : mySerials) {
                    if (serial.getName().equals(sname)) {
                        ArrayList<Integer> standard = viewedSerials.get(sname);
                        if (standard.get(2) == 0) {
                            serial.newMark(mark);
                            standard.set(2, mark);
                            viewedSerials.put(sname, standard);
                        } else {
                            serial.newMark(standard.get(2), mark);
                            standard.set(2, mark);
                            viewedSerials.put(sname, standard);
                        }
                        break; }

                }
            } else {
                System.out.printf("Оценка должна быть от 1 до 10, а вы поставили %d", mark);
            }
        }else {System.out.println("Вы не начинали просмотр сериала с таким названием.");}
        }

        public void allViewedSerials(User user){
            System.out.println("Вы начали просмотр этих сериалов:");
            for (Serial serial:mySerials) {
                System.out.println("'" + serial.getName() + "'");
            }
        }

        public void infoAboutSerial(String sname,ArrayList<Serial> allSerials) {
        User user = new User(this.login,this.password,this.mySerials,this.viewedSerials);
            for (Serial serial : allSerials) {
                if (serial.getName().equals(sname)) {
                    if (viewedSerials.containsKey(sname)) {
                        System.out.println(serial.toString(user));
                    } else {
                        System.out.println(serial.toString());
                    }
                    break;
                }
            }
        }

    public User(String login, String password, ArrayList<Serial> mySerials, HashMap<String, ArrayList<Integer>> viewedSerials) {
        this.login = login;
        this.password = password;
        this.mySerials = mySerials;
        this.viewedSerials = viewedSerials;
    }

    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public ArrayList<Serial> getMySerials() {
        return mySerials;
    }
    public HashMap<String, ArrayList<Integer>> getViewedSerials() { return viewedSerials; }

    public void setPassword(String password) {
        this.password = password;
    }
}
