- VS Code Install:  
  - Download **Adoptium's Temurin® Java 17**:
    - [Windows Temurin® JDK 17 Download](https://adoptium.net/en-GB/download?link=https%3A%2F%2Fgithub.com%2Fadoptium%2Ftemurin17-binaries%2Freleases%2Fdownload%2Fjdk-17.0.16%252B8%2FOpenJDK17U-jdk_x64_windows_hotspot_17.0.16_8.msi&vendor=Adoptium)  

    - [Linux Temurin® JDK 17 Download](https://adoptium.net/en-GB/temurin/releases?os=linux&version=17&package=jdk&arch=any&mode=filter)  
  
  - Once installed:
    - Open a java project (e.g. *CheckJDKVersion.java* in this repo)
    - Open command palette: **View → Command Palette**
    - Type: *Java: Configure Java Runtime*
    - Check that **JDK: JavaSE-17** is shown
    - Click the dropdown box and ensure that file path is something like: *C:\Users\name\AppData\Local\Programs\Eclipse Adoptium\jdk-17.0.16.8-hotspot*
    <br><br>
    - Also got to **File → Preferences → Settings**  
    - Search for: **java.configuration.runtimes**  
    - Click **Edit in settings .json**  
    - Add something like or add to the current setup:
    ```json
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-17",
            "path": "C:\\Users\\username\\jdk-17.0.16+8",
            "javadoc": "https://adoptium.net/docs/",
            "default": true,
        }
    ]
    ```

    - **Replacing path with where your eclipse JDK is installed**  
    - You can find this by running the command
    ```cmd
    where java
    ```
    - Copy the path for the one from **Eclipse Adoptium**  
    - Runt **CheckJDKVersion.java** to make sure everything is set up right  
  

-----------------------------------------------------------------------
- IntelliJ Install:  
  - Go to: **File → Project Structure → Project → SDKs**
  - Press SDK Dropdown Box
  - Select Download JDK:
    - Version = **17**
    - Vendor = **Eclipse Temurin (AdoptOpenJDK HotSpot) 17.0.16**
  - Select **Language Level 17** (*not SDK*)
  - Save Settings & Download Begins  
  *(Will Take A While, So Keep IDE Open)*