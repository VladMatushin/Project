package MyProject;

import java.util.ArrayList;
import java.util.HashMap;


public class Serial {
    private String name;

    private int numberOfSeasons;
    private int numberOfEpisodes;
    private  int sumOfRating;
    private int numberOfMarks;
    private int numberOfViews;


    public void addNumberOfSeasons() {
        this.numberOfSeasons += 1;
    }

    public Serial(String name, int numberOfSeasons, int numberOfEpisodes, int sumOfRating, int numberOfMarks, int numberOfViews) {
        this.name = name;
        this.numberOfSeasons = numberOfSeasons;
        this.numberOfEpisodes = numberOfEpisodes;
        this.sumOfRating = sumOfRating;
        this.numberOfMarks = numberOfMarks;
        this.numberOfViews = numberOfViews;
    }



    public void newMark(int mark){
        numberOfMarks+=1;
        sumOfRating+=mark;
    }
    public void newMark(int oldMark, int newMark){
        sumOfRating+= newMark - oldMark;
    }
    public void  newSeason(){ numberOfSeasons+=1;}
    public void newViewer(){numberOfViews+=1;}

    public String getName() { return name;}
    public int getNumberOfSeasons() { return numberOfSeasons; }
    public int getNumberOfEpisodes() {return numberOfEpisodes;}
    public int getSumOfRating() {return sumOfRating;}
    public int getNumberOfMarks() { return numberOfMarks; }
    public int getNumberOfViews() { return numberOfViews; }


    @Override
    public String toString() {
        String ret = "";
        if (numberOfMarks != 0) {
            ret = ("Название сериала: '" + name + "'\n" +
                    "Количество сезонов: " + numberOfSeasons + "\n" +
                    "Количество серий в каждом сезоне: " + numberOfEpisodes + "\n" +
                    "Рейтинг сериала: " + sumOfRating/numberOfMarks + "\n" +
                    "Количество просмотров сериала: " + numberOfViews + '.');
        }else {
            ret = "Название сериала: '" + name + "'" +
                    "Количество сезонов: " + numberOfSeasons + "\n" +
                    "Количество серий в каждом сезоне: " + numberOfEpisodes + "\n" +
                    "У сериала пока нет оценок" +  "\n" +
                    "Количество просмотров сериала: " + numberOfViews + '.';
        }
        return  ret;
    }


    public String toString(User user){
        String ret = "";
        HashMap<String, ArrayList<Integer>> viewedSerials = user.getViewedSerials();
        ArrayList<Integer> standard = viewedSerials.get(name);

        int myRating = standard.get(2);

        if (numberOfMarks != 0) {
            String str = "";
            if (myRating != 0){
                str = "Рейтинг сериала: " + sumOfRating/numberOfMarks + "\n" +
                        "Вы поставили сериалу оценку " + standard.get(2) + "\n";
            }else {
                str = "Рейтинг сериала: " + sumOfRating/numberOfMarks + "\n" +
                        "Вы не поставили оценку сериалу\n";
            }
            ret = ("Название сериала: '" + name + "'" +
                    "Количество сезонов: " + numberOfSeasons + "\n" +
                    "Количество серий в каждом сезоне: " + numberOfEpisodes + "\n" +
                    "Вы остановились на " + standard.get(0) + "сезоне " + standard.get(1) + "серии\n" +
                    str +
                    "Количество просмотров сериала: " + numberOfViews + '.');
        }else {
            ret = "Название сериала: '" + name + "'" +
                    "Количество сезонов: " + numberOfSeasons + "\n" +
                    "Количество серий в каждом сезоне: " + numberOfEpisodes + "\n" +
                    "У сериала пока нет оценок" +  "\n" +
                    "Количество просмотров сериала: " + numberOfViews + '.';
        }
        return ret;
    }

}
