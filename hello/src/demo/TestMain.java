package demo;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.pdf.CreateP;

// import com.pdf.Application;

import demo.mainPro.CreateMain;
import demo.util.Validate;



public class TestMain {

    public static void main(String[] args) throws Exception {

        // new ChangeToHtml().createAll("D:/闹着玩的/技能书/Y/itext/第三版/模拟生产过程/软件结构/GeneTestReportSoft/",
        // "D:/闹着玩的/技能书/Y/itext/第三版/模拟生产过程/软件结构/GeneTestReportSoft/test1", "html.html",
        // "test");
        // // new ChangeToHtml().createAll(filePath,out,"test.html","TargetingChemotherapy");
        // System.exit(0);


        // new CreateMain().createXML("", "", "", "");
        // System.exit(0);
        // Application.main("D:/闹着玩的/技能书/Y/itext/第三版/模拟生产过程/软件结构/GeneTestReportSoft/",
        // "Targeting_v1",
        // "d:/闹着玩的/技能书/Y/itext/第三版/模拟生产过程/软件结构/GeneTestReportSoft/test1", "test1.pdf");
        // System.out.println("hi?");
        // Runtime.getRuntime().exec(
        // "cmd /c start D:/闹着玩的/技能书/Y/itext/第三版/模拟生产过程/软件结构/GeneTestReportSoft/test1/test1.pdf");
        // System.exit(0);

        CreateMain createMain = new CreateMain();
        Options options = new Options();
        // 第一个参数代表操作，第二个参数代表该参数是否需要强制含有内容，第三个参数是该参数的解释
        options.addOption("fileName", true, "报告文件的名字，例report.pdf，必填");
        options.addOption("report_type", true, "检测报告类型及版本，必填");
        options.addOption("input", true, "基因检测结果文件（支持txt或json格式，实例文件见input/example.txt），必填");
        options.addOption("out", true, "检测报告输出目录，必填");
        options.addOption("input_type", true, "重新生成示例输入检测文件，默认为txt，选填");
        options.addOption("revm", false, "重新生成模板文件，选填");
        options.addOption("check_input", false, "验证输入文件格式是否正确，选填");
        options.addOption("help", false, "帮助");


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("help") || args.length == 0) {

            String formatstr = "java -jar GeneTestReport.jar [OPTION]... PATH";
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(formatstr, options);
        }
        else if (!cmd.hasOption("report_type")) {
            System.out.println("error: Lack of report_type");
        }
        else {
            String reportType = cmd.getOptionValue("report_type");
            String inputType = "txt";
            if (cmd.hasOption("input_type")) {
                String temp = cmd.getOptionValue("input_type");
                if (!(temp.equals("txt") || temp.equals("json"))) {
                    System.out.println("the input_type is wrong!");
                }
                else {
                    inputType = temp;
                }
            }
            String filePath = System.getProperty("java.class.path");
            String pathSplit = System.getProperty("path.separator");// 得到当前操作系统的分隔符，windows下是";",linux下是":"
            if (filePath.contains(pathSplit)) {
                filePath = filePath.substring(0, filePath.indexOf(pathSplit));
            }
            else if (filePath.endsWith(".jar")) {// 截取路径中的jar包名,可执行jar包运行的结果里包含".jar"
                if (filePath.contains("/")) {
                    filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
                }
                else {
                    filePath = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
                }
            }
            filePath = filePath.replaceAll("\\\\", "/");


            String path = filePath + "/Resources/templates";
            // String path = "Resource/templates";
            String vmPath = path + "/" + reportType + "/template.vm";

            File file = new File(vmPath);
            if (cmd.hasOption("revm") || !file.exists()) {
                System.out.println("create .vm template file and example content file...");
                createMain.createTemp(filePath, inputType, reportType);
            }
            if (!cmd.hasOption("out") || !cmd.hasOption("input") || !cmd.hasOption("fileName")) {
                System.out.println("error: lack of parameter of the out or the input");
            }
            else {
                String input = cmd.getOptionValue("input");
                String out = cmd.getOptionValue("out");
                String fileName = cmd.getOptionValue("fileName");
                if (cmd.hasOption("check_input")) {
                    Validate.doValidate(filePath, input, reportType);
                }
                createMain.createXML(filePath, out, input, reportType, inputType);
                // Application.main(filePath, reportType, out, fileName);
                CreateP.main(filePath, reportType, out, fileName);
            }

        }
    }

    public static void createPdf(String filePath, String input, String out, String reportType,
            String fileName) throws Exception {
        CreateMain createMain = new CreateMain();

        filePath = filePath.replaceAll("\\\\", "/");

        // createMain.createXML(filePath, out, input, reportType,inputType);
        // Application.main(filePath, reportType, out, fileName);
    }
}
