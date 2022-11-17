import junit.framework.TestCase;
import com.Main;
import org.junit.Test;

public class MainTest extends TestCase {

    @Test
    public void test_searchPrimer() throws Exception {
        Main main = new Main();
        String arg0 = "searchPrimer";
        String arg1 = "-mhapPathT";
        String arg2 = "esophagus_T.mhap.gz";
        String arg3 = "-mhapPathN";
        String arg4 = "esophagus_N.mhap.gz";
        String arg5 = "-cpgPath";
        String arg6 = "hg19_CpG.gz";
        String arg7 = "-region";
        String arg8 = "chr1:942616-942734";
//        String arg7 = "-bedPath";
//        String arg8 = "hg19_1000CpG.bed";
        String arg27 = "-outputDir";
        String arg28 = "outputDir";
        String arg9 = "-tag";
        String arg10 = "linkTest";
        String arg11 = "-fLength";
        String arg12 = "25";
        String arg13 = "-rLength";
        String arg14 = "25";
        String arg15 = "-minT";
        String arg16 = "0.00";
        String arg17 = "-maxN";
        String arg18 = "10";
        String arg19 = "-minFC";
        String arg20 = "0";
        String arg21 = "-minInsertSize";
        String arg22 = "40";
        String arg23 = "-maxInsertSize";
        String arg24 = "160";
        String arg25 = "-minCov";
        String arg26 = "0";

        String[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14,
                arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28};

        String argsStr = "";
        for (int i = 0; i < args.length; i++) {
            argsStr += args[i] + " ";
        }
        System.out.println(argsStr);

        main.main(args);
    }

    @Test
    public void test_listPattern() throws Exception {
        Main main = new Main();
        String arg0 = "listPattern";
        String arg1 = "-mhapPath";
        String arg2 = "NG_lung_merged.mhap.gz NG_normal_merged.mhap.gz";
        String arg3 = "-cpgPath";
        String arg4 = "hg19_CpG.gz";
//        String arg5 = "-FPrimer";
//        String arg6 = "chr20:43727100-43727119";
//        String arg7 = "-RPrimer";
//        String arg8 = "chr20:43727181-43727200";
        String arg5 = "-bedPath";
        String arg6 = "testBed.bed";
        String arg9 = "-outputDir";
        String arg10 = "outputDir";
        String arg11 = "-tag";
        String arg12 = "listPatternTest";

        String[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg9, arg10, arg11, arg12};
        //String[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12};

        String argsStr = "";
        for (int i = 0; i < args.length; i++) {
            argsStr += args[i] + " ";
        }
        System.out.println(argsStr);

        main.main(args);
    }

    @Test
    public void test_countPattern() throws Exception {
        Main main = new Main();
        String arg0 = "countPattern";
        String arg1 = "-mhapPath";
        String arg2 = "esophagus_T.mhap.gz esophagus_N.mhap.gz";
        String arg3 = "-cpgPath";
        String arg4 = "hg19_CpG.gz";
//        String arg5 = "-FPrimer";
//        String arg6 = "chr1:942438-942451";
//        String arg7 = "-RPrimer";
//        String arg8 = "chr1:942471-942498";
//        String arg9 = "-FPattern";
//        String arg10 = "111";
//        String arg11 = "-RPattern";
//        String arg12 = "11111";
        String arg5 = "-bedPath";
        String arg6 = "testBed.bed";
        String arg13 = "-outputDir";
        String arg14 = "outputDir";
        String arg15 = "-tag";
        String arg16 = "countPrimerTest";

        String[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg13, arg14, arg15, arg16};
        //String[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16};

        String argsStr = "";
        for (int i = 0; i < args.length; i++) {
            argsStr += args[i] + " ";
        }
        System.out.println(argsStr);

        main.main(args);
    }
}