import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {


    public static class DuplicateCandidate {

        public DuplicateCandidate(File file) {
            this.file = file;
            this.hash = checkSumForFile(file);
            System.out.println(Thread.currentThread().getName() + " - " + file.getName() + " : " + hash);
        }

        public final File file;
        public final String hash;
        public boolean deleted = false;
    }

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the path to the Directory : ");
        String dirPath = input.nextLine();

        long startTime = System.currentTimeMillis();

        List<File> files = new ArrayList();
        flist(dirPath, files);

        System.out.println("Number of Elements in the Directory : " + files.size());


        List<DuplicateCandidate> duplicateCandidates = Collections.synchronizedList(new ArrayList<DuplicateCandidate>());


        int MAX_THREAD_NUMBER = 32;

        Thread[] threads = new Thread[MAX_THREAD_NUMBER];

        AtomicInteger counter = new AtomicInteger(-1);
        for (int i = 0; i < MAX_THREAD_NUMBER; i++) {
            threads[i] = new Thread(() -> {
                int index;
                while ((index = counter.incrementAndGet()) < files.size()) {
                    duplicateCandidates.add(new DuplicateCandidate(files.get(index)));
                }
            });
            threads[i].setName("THREAD - " + i);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }


        for (int i = 0; i < duplicateCandidates.size(); i++) {
            System.out.println("Searching for duplicates for :" + duplicateCandidates.get(i).file);
            for (int j = i + 1; j < duplicateCandidates.size(); j++) {
                if (!duplicateCandidates.get(j).deleted &&
                        duplicateCandidates.get(i).hash.equalsIgnoreCase(duplicateCandidates.get(j).hash)) {
                    System.out.println("\tDuplicate found :" + duplicateCandidates.get(j).file );
                    duplicateCandidates.get(j).deleted = true;
                    //duplicateCandidates.get(j).file.delete();
                }
            }
        }

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
    }

    public static void flist(String directoryName, List<File> files) {
        File dir = new File(directoryName);
        File[] fls = dir.listFiles();
        for (File file : fls) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                flist(file.getAbsolutePath(), files);
            }
        }
    }


    public static String checkSumForFile(File file) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            FileInputStream fileInput = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = fileInput.read(dataBytes)) != -1) {
                messageDigest.update(dataBytes, 0, bytesRead);
            }
            byte[] digestBytes = messageDigest.digest();
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < digestBytes.length; i++) {
                sb.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            fileInput.close();


            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}