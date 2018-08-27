
package forensic_harddisk;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

        
public class Forensic_HardDisk implements FileFilter{
    public String line;
    @Override
   public boolean accept(File pathname){
       if (!pathname.isFile()){
           return false;
       }
       if (pathname.getAbsolutePath().endsWith(".txt")) return true;
       else return false;
   }
    public void fetchChild(File file, String malware) throws FileNotFoundException, IOException
    {
        int test;
        if(file.isDirectory())
        {
            File[] children = file.listFiles();
            if (children == null) return;
            for (File child : children)
            {
                if(child.isFile())
                {
                    boolean checkFileExtension = accept(child);
                    if (checkFileExtension == true)
                    {
                        FileReader fr = new FileReader(child.getAbsolutePath());
                        BufferedReader br = new BufferedReader(fr);
                        
                        while ((line = br.readLine())!=null)
                        {
                            test = line.indexOf(malware);
                            if (test!=-1)
                            {
                                System.out.println(child.getAbsolutePath());
                                
                                break;
                            } 
                        }
                        
                        fr.close();
                        br.close();
                    }
                }
            }
            for (File child : children)
            {
                this.fetchChild(child,malware);
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Forensic_HardDisk Forensic = new Forensic_HardDisk();
        boolean flag = true;
        int chon;
        String check, kiemtra;
        while (flag)
        {
            Scanner sc = new Scanner(System.in); 
            System.out.println("Menu:\n1. Quét tất cả ổ đĩa.\n2. Nhập đường dẫn thư mục.\n3. Thoát.");
            System.out.print("Nhập lựa chọn: ");
            chon = sc.nextInt();
            sc.nextLine();
            if (chon!=1 && chon !=2) break;
            System.out.print("Nhập chuỗi cần tìm trong file: ");
            kiemtra = sc.nextLine();
            switch(chon)
            {
                case 1:
                    File[] roots = File.listRoots();
                    for (File root : roots)
                    {
                        File child = new File(root.getAbsolutePath());
                        Forensic.fetchChild(child,kiemtra);
                    }
                    break;
                case 2:    
                    
                    boolean a;
                    File f;
                    String yesorno = null;
                    do
                    {
                        System.out.print("Nhập đường dẫn thư mục: ");
                        check = sc.nextLine();
                        f = new File(check);
                        a = f.exists();
                        if(!a) 
                        {
                            System.out.println("Thư mục không tồn tại");
                            System.out.println("Bạn có muốn nhập lại không?(y)");
                            yesorno = sc.nextLine();
                            
                        }
                        if ("y".equalsIgnoreCase(yesorno)) continue; 
                        else break;
                    } while(!a);
                    Forensic.fetchChild(f,kiemtra);
                    break;          
            }
        }
    }
}
