package utils;

import java.io.*;
import java.util.*;

public class FileHandler {
    public static void writeToFile(String filename, String content){
        try(FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw= new BufferedWriter(fw)){
                bw.write(content);
                bw.newLine();
            }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static List<String> readFromFile(String filename){
        List<String> lines =new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line =br.readLine())!=null){
                lines.add(line);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return lines;
    }
    public static void updateFile(String filename,List<String>contents){
        try(FileWriter fw = new FileWriter(filename);
            BufferedWriter bw= new BufferedWriter(fw)){
                for(String line : contents){
                    bw.write(line);
                    bw.newLine();
                }
            }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

