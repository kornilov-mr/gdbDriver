package gdbDriver.Core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CodeCompiler {
    private final String cppVersion;

    public CodeCompiler(String cppVersion) {
        this.cppVersion = cppVersion;
    }

    public File compileCode(File sourceFile) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(sourceFile.getParentFile());

        File executableFile = createExecutableFile(sourceFile);

        ArrayList<String> args = SystemParameters.getArgs();
        args.add(createCompileExecuteString(sourceFile, executableFile));
        builder.command(args);

        try {
            builder.start();
        } catch (IOException e) {
            System.out.println("A problem occurred during code compilation");
            System.out.println(createCompileExecuteString(sourceFile, executableFile));
            throw new RuntimeException(e);
        }
        return executableFile;
    }

    private File createExecutableFile(File sourceFile) {
        String[] temp = sourceFile.getName().split("\\.");
        String extension = "." + temp[temp.length - 1];
        return new File(sourceFile.getAbsolutePath().replace(extension, ".exe"));
    }

    private String createCompileExecuteString(File sourceFile, File executableFile) {
        return "g++ -g " + cppVersion + " " + sourceFile.getName() +
                " -o " + executableFile.getName().replace(".exe", "");

    }
}
