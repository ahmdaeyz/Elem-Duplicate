import java.io.*;
import javax.swing.filechooser.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.security.MessageDigest;
import java.io.FileInputStream;
public class eleminatingDuplicate{

    public static void main(String[] args) throws Exception{
        List<File> files = new ArrayList();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the path to the Directory : ");
        String dirPath = input.nextLine();
        flist(dirPath,files);
        System.out.println("Number of Elements in the Directory : "+files.size());
        String[] hashes = new String[files.size()];
        int[] counter = new int[files.size()];
        for(int i =0;i<files.size();i++){
          hashes[i]=checkSumForFile(files.get(i));
        }
        for(int i =0;i<files.size();i++){
            for(int j=i+1;j<files.size();j++){
                if(hashes[i].equals(hashes[j])){
                files.get(i).delete();
                files.remove(i);
                System.out.println("A Duplicate Got Deleted !");
                }
            }
        }
        System.out.println("Number of Elements in the Directory : "+files.size());
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
        return name.toLowerCase().endsWith(".jpg");
    }
    public static String checkSumForFile(File file) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	    FileInputStream fileInput = new FileInputStream(file);
	    byte[] dataBytes = new byte[1024];
	    int bytesRead = 0;
	    while ((bytesRead = fileInput.read(dataBytes)) != -1){
	         messageDigest.update(dataBytes, 0, bytesRead);
	    }
	    byte[] digestBytes = messageDigest.digest();
	    StringBuffer sb = new StringBuffer("");
	    for (int i = 0; i < digestBytes.length; i++) {
	        sb.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
        fileInput.close();
        return sb.toString();
    }
}