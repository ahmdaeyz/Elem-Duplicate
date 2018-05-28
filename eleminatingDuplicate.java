import java.io.*;
import javax.swing.filechooser.FileFilter;
import java.util.ArrayList;
import java.util.List;
public class eleminatingDuplicate{

    public static void main(String[] args){
        List<File> files = new ArrayList();
        String dirPath = "/home/ahmedaeyz/Pictures/";
        flist(dirPath,files);
        for(int i =0;i<files.size();i++){
            for(int j =0;j<files.size();j++){
                if(files.get(i).getName().equals(files.get(j).getName())){
                    files.get(i).delete();
                    System.out.println("Duplicate Deleted");
                }
            }
        }
    }
    public static void flist(String directoryName ,List<File> files){
        File dir = new File(directoryName);
        File[] fls = dir.listFiles();
        for(File file:fls){
            if(file.isFile()){
                if(get(file,file.getName())){
                files.add(file);
                }
            }else if(file.isDirectory()){
                flist(file.getAbsolutePath(),files);
            }
        }
    }
    public static boolean get(File file,String name){
        return name.toLowerCase().endsWith(".png");
    }
}