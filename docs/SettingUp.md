---
  layout: default.md
  title: "Setting up and getting started"
  pageNav: 3
---

# Setting up and getting started

<!-- * Table of Contents
<page-nav-print />

--- -->

### Steps to take
1. **Check Java Version**

   Ensure you have Java `17` or above installed on your computer.

   ```
   java -version
   ```

   **Mac users:** Follow the setup guide [here](https://se-education.org/guides/tutorials/javaInstallationMac.html) to install the correct JDK version.
   **Windows users:** Follow the setup guide [here](https://se-education.org/guides/tutorials/javaInstallationWindows.html) to install the correct JDK version.
   **Linux users:** Follow the setup guide [here](https://se-education.org/guides/tutorials/javaInstallationLinux.html) to install the correct JDK version.

2. **Download playbook.io**

   Download the latest `.jar` file from the [playbook.io Releases](https://github.com/AY2526S1-CS2103T-F13-3/tp/releases) page.

3. **Set Up Home Folder**

   Copy the `.jar` file to the folder you want to use as the **home folder** for playbook.io. This is where your data files will be stored.

4. **Launch the Application**

   Open a terminal, navigate (`cd`) to that folder, and run:

   ```
   java -jar "[CS2103T-F13-3][playbook.io].jar"
   ```

   For example, if your `.jar` file is in the Downloads folder:

   ```
   cd Downloads
   java -jar "[CS2103T-F13-3][playbook.io].jar"
   ```
   The application window should appear, ready for use.

5. **Saving your data**

   The app automatically saves your data to a JSON file on your computer whenever changes are made.
   You do not need to manually save your progress.

6. **Getting help**

   Within the app, type `help` to get a link for our UserGuide.
   You can also refer to the [_User Guide_](https://ay2526s1-cs2103t-f13-3.github.io/tp/UserGuide.html) for detailed instructions and examples.

---

### Having issues?

If you encounter errors while running the `.jar` file, check:

- That you’re using **Java 17 or newer**
- The `.jar` file is located in a folder you have permission to access
- You’re using the correct command syntax

---
