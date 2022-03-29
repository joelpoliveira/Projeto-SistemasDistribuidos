package server;

import java.io.File;
import java.util.*;


public class ConfigurationsParser {
    private String filename;

    public ConfigurationsParser(String filename){
        this.filename = filename;
    }

    public HashMap<String, String> parse(){

        HashMap<String, String> configs = new HashMap<>();
        try{
            File f = new File(this.filename);
            Scanner reader = new Scanner(f);
            String line;
            
            while (reader.hasNextLine()){
                try{
                    line = reader.nextLine();
                    String[] line_elements = line.split("=");
                    configs.put(line_elements[0].strip(), line_elements[1].strip());
                }catch(Exception e1){
                    continue;
                }
            }
            reader.close();
        }catch(Exception e){
            System.out.println("IO error");
        }
        
        return configs;
    }
}
