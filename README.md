Up to Date
=
Will help keep your maven project dependencies up to date

Make sure it works
=
    mvn clean install
    java -jar console/target/up-to-date.jar up-to-date.json
You should see various reports in the generated/up-to-date directory

Configuration
=
- Configuration is done with a .json file
- pass the .json file as the first argument
- "pomFileName"
    - name of the pom files, usually "pom.xml"
- "directoryNamesToSkip"
    - do not navigate into these directories, a typical example would be "target"
- "directoriesToSearch"
    - where to scan for pom files
- "mavenRepositories"
    - which repositories to scan, in order, for example: "http://repo.maven.apache.org/maven2"
- "doNotUpgradeTo"
    - do not upgrade to this dependency, but go ahead and use a newer one if available
    - useful for temporarily blocking upgrades to an unstable version
- "doNotUpgradeFrom"
    - do not upgrade from this dependency
    - useful if you are not ready to deal with the consequences of upgrade yet
- "automaticallyUpgrade"
    - go ahead and update the pom files to the latest versions
- "reportDirectory"
    - directory to place reports
- "cacheDirectory"
    - directory for cache
- "cacheExpire"
    - how long cache is valid for

Reports
=
Reports are currently formatted as plain json

- apply
    - recommended upgrades, or upgrades that have already been applied if automaticallyUpgrade was true
- ignore
    - upgrades that have no recommendation because they were skipped by either the doNotUpgradeTo or doNotUpgradeFrom lists
- inconsistency
    - dependencies that have different versions
- not-found
    - dependencies that could not be found in repositories
- pom
    - raw data from scanning pom files
- repository
    - raw data from scanning repository
- status-quo
    - list of dependencies to add to doNotUpgradeFrom if you want to keep versions where they are
    - useful if you want to make sure all new dependencies are automatically upgraded, but aren't ready to upgrade existing ones

Design by Contract
=

- This project uses Sean's design-by-contract programming style
- To learn more, see the following presentation
- [Design by Contract](design-by-contract-2.md)

Dependency Analysis
=
If you want dependency analysis, set up [dependency analyzer](https://github.com/SeanShubin/dependency-analyzer) and [dot](http://graphviz.org) first.

Then, you can run the dependency report as follows

    mvn verify -P dependency

