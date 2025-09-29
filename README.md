# video-game
Repository for developing video game. Cohort 3, Team 8.  

-----------------------------------------------------------------------

Note we must all use **Adoptium's Temurin® Java 17**:
- This is the version the modules technical requirements state we must use  
- To test you are using the right version, run the file: *check_JDK_version.java*
  - Expect results like this:  
    - Java Version: 17.0.16  
    - Java Vendor: Eclipse Adoptium

-----------------------------------------------------------------------
- VS Code Install:  
  - Download **Adoptium's Temurin® Java 17**:
    - [Windows Temurin® JDK 17 Download](https://adoptium.net/en-GB/download?link=https%3A%2F%2Fgithub.com%2Fadoptium%2Ftemurin17-binaries%2Freleases%2Fdownload%2Fjdk-17.0.16%252B8%2FOpenJDK17U-jdk_x64_windows_hotspot_17.0.16_8.msi&vendor=Adoptium)  

    - [Linux Temurin® JDK 17 Download](https://adoptium.net/en-GB/temurin/releases?os=linux&version=17&package=jdk&arch=any&mode=filter)  
  
  - Once installed:
    - Open a java project (e.g. *check_JDK_version.java* in this repo)
    - Open command palette: **View → Command Palette**
    - Type: *Java: Configure Java Runtime*
    - Check that **JDK: JavaSE-17** is shown
    - Click the dropdown box and ensure that file path is something like: *C:\Users\name\AppData\Local\Programs\Eclipse Adoptium\jdk-17.0.16.8-hotspot*  

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

-----------------------------------------------------------------------
Commit Standards:
- **Type: Message**
  - Type: **fix**, **feature**, **doc**, **config**
  - Message: short explain change
- Example:  
  - *fix: data validation bug corrected*

-----------------------------------------------------------------------

Names:  
Henry G  
Lenny S  
Isaac M  
Andri K  
Rishi T  
Isaac K  