package sample;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

    public UserModel(){

    }
    public static void insert(User user){
        try {
            int id = getLastUserId();
            File dataFile = new File("/home/ahmed/IdeaProjects/Data");
            dataFile.mkdir();
            FileWriter fStream = new FileWriter("/home/ahmed/IdeaProjects/Data/data.txt",true);
            BufferedWriter out = new BufferedWriter(fStream);
            out.write((id+1) +","+user.toString()+"\n");
            out.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();

        }
    }

    public static List<User> selectAll() {
        try {
            FileReader fStream = new FileReader("/home/ahmed/IdeaProjects/Data/data.txt");
            BufferedReader br = new BufferedReader(fStream);
            List<User> users = new ArrayList<>();
            String line;
            while((line = br.readLine())!=null){
                String[] userData =  line.split(",");
                users.add(new User(userData[0],userData[1],userData[2],userData[3],userData[4]));
            }
            return users;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getLastUserId() {
        FileReader fStream;
        try {
            fStream = new FileReader("/home/ahmed/IdeaProjects/Data/data.txt");
            BufferedReader br = new BufferedReader(fStream);

            String index="0", line;
            while((line = br.readLine())!=null){
                index =  line.split(",")[0];
            }
            return Integer.parseInt(index);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }
}
