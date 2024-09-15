# Minecraft Forge 1.16.5 MDK Template

<div>
  <p>
    <a href="./LICENSE">
      <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License" />
    </a>
  </p>
</div>

Template project of empty Minecraft Forge modification

## Requirements

For building:

- [JDK 8](https://github.com/adoptium/temurin8-binaries/releases/latest)
- [Git](https://git-scm.com/downloads)

For development:

- [Visual Studio Code](https://code.visualstudio.com/) or [IntelliJ IDEA](https://www.jetbrains.com/idea/download)
- [Git](https://git-scm.com/downloads)

## Building

- Download the repository:

  ```console
  git clone https://github.com/imesense/minecraft-projects.git
  ```

### Console (Windows)

- Build modification:

  ```console
  ./gradlew.bat build
  ```

### Console (Linux/macOS)

- Build modification:

  ```sh
  ./gradlew build
  ```

### Visual Studio Code (Windows)

- Generate launch files:

  ```console
  ./gradlew.bat genVSCodeRuns
  ```

### Visual Studio Code (Linux/macOS)

- Generate launch files:

  ```sh
  ./gradlew genVSCodeRuns
  ```

### IntelliJ IDEA (Windows)

- Generate launch files:

  ```console
  ./gradlew.bat genIntellijRuns
  ```

### IntelliJ IDEA (Linux/macOS)

- Generate launch files:

  ```sh
  ./gradlew genIntellijRuns
  ```

### Docker

- Build modification:

  ```sh
  docker build -f Dockerfile --output . .
  ```

## License

Contents of this project licensed under terms of the __MIT license__ unless otherwise specified. See [this](./LICENSE) file for details
