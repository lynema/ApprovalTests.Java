package org.approvaltests.reporters.intellij;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import com.spun.util.SystemUtils;

public class IntelliJPathResolver
{
  private final String channelsPath;
  private final String runtimeSuffix;

  public IntelliJPathResolver(Edition edition)
  {
    String appData = "";
    if (SystemUtils.isWindowsEnviroment())
    {
      appData = System.getenv("LOCALAPPDATA");
      runtimeSuffix = "/bin/idea64.exe";
    }
    else if (SystemUtils.isMacEnviroment())
    {
      appData = System.getenv("HOME");
      appData += "/Library/Application Support";
      runtimeSuffix = "/bin/idea";
    }
    else // Linux
    {
      appData = System.getenv("HOME");
      appData += "/.local/share";
      runtimeSuffix = "/bin/idea.sh";
    }
    String toolboxPath = appData + "/JetBrains/Toolbox";
    this.channelsPath = toolboxPath + "/apps/" + edition.getDirectory() + "/ch-0/";
  }
  public String findIt()
  {
    String notPresentPath = "C:\\Intelli-not-present.exe";
    try
    {
      return getIntelliJPath().map(Objects::toString).orElse(notPresentPath);
    }
    catch (IOException e)
    {
      return notPresentPath;
    }
  }
  private Optional<Path> getIntelliJPath() throws IOException
  {
    return Files.walk(Paths.get(channelsPath), 1, FileVisitOption.FOLLOW_LINKS) //
        .map(Path::getFileName) //
        .map(Objects::toString) //
        .filter(Version::isVersionFile) //
        .map(Version::new) //
        .max(Comparator.naturalOrder()) //
        .map(this::getPath);
  }
  private Path getPath(Version version)
  {
    return Paths.get(channelsPath + version.version + runtimeSuffix).toAbsolutePath();
  }
}
