package utilities;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class WikiUtils {
	 public int validateFolder(String path){
	 		File dir = new File(path) ;
			if(!dir.exists() || !dir.canRead() )
				return -1;
			
			return 0;
	 }
}
