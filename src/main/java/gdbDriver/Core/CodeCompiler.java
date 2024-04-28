package gdbDriver.Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CodeCompiler {
    private final String cppVersion;
    private final Path folderForExecutablePath = Paths.get("src/main/java/executableFiles");
    public CodeCompiler(String cppVersion) {
        this.cppVersion = cppVersion;
        if(!folderForExecutablePath.toFile().exists()){
            folderForExecutablePath.toFile().mkdirs();
        }
    }

    public File compileCode(File sourceFile) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(sourceFile.getParentFile());

        File executableFile = createExecutableFile(sourceFile);

        executableFile.delete();

        ArrayList<String> args = SystemParameters.getArgs();
        args.add(createCompileExecuteString(sourceFile, executableFile));
        builder.command(args);
        try {
            Process process = builder.start();
            process.waitFor();
        } catch (IOException e) {
            System.out.println("A problem occurred during code compilation");
            System.out.println(createCompileExecuteString(sourceFile, executableFile));
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return executableFile;
    }

    private File createExecutableFile(File sourceFile) {
        String[] temp = sourceFile.getName().split("\\.");
        String extension = "." + temp[temp.length - 1];
        File executableFile = folderForExecutablePath.resolve(
                sourceFile.getName().replace(extension, SystemParameters.ExecutableFileExtension)).toFile();
        return executableFile;
    }

    private String createCompileExecuteString(File sourceFile, File executableFile) {

        return "g++ -g " + cppVersion + " " + sourceFile.getName() +
                " -o " + executableFile.getAbsolutePath().replace(SystemParameters.ExecutableFileExtension, "");

    }
}
