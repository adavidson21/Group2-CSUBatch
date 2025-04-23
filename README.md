# CSUBatch

The goal of this project is to design and implement a batch scheduling system called CSUbatch.

## Description

CSUBatch is a modular batch scheduling system designed to manage and execute multiple jobs efficiently using standard scheduling algorithms like First Come First Serve, Shortest Job First, and Priority-based scheduling. The system functions much like a task manager, receiving and organizing jobs based on selected policies, then dispatching them in a controlled manner to optimize resource usage. It includes real-time performance monitoring features, such as turnaround time, CPU usage, and throughput metrics, allowing users to evaluate the effectiveness of scheduling strategies. Developed as a command-line application, CSUBatch emphasizes cross-platform compatibility, automated testing, and extensibility, making it a robust tool for simulating and analyzing job scheduling behavior in shared-resource environments.

## Getting Started

### Dependencies

* Java Version 17

### Installing

* Clone this repository to any location on your local machine.
* Compile the program using the command `./gradlew clean build` in the root directory.

### Executing program

To compile the project (starting at the root directory):

```
./gradlew clean build
```

To run the project:

```
./gradlew run
```

To run the unit tests:
```
./gradlew clean test
```

## Help

When the program is running, use command `help` for a list of supported commands and their usage.

## Authors

- Alexis Davidson  
- Zachary Cericola
- Trenten Cummings
- Cole Lassiter

## Version History

* 1.0
    * Initial Release

## License

This project is licensed under the [NAME HERE] License - see the LICENSE.md file for details

## Acknowledgments

Project Management:
- [Issue Tracker (Trello)](https://trello.com/b/XDxNiiio/csubatch)
- [Project Specifications Document](https://colstate-my.sharepoint.com/:w:/g/personal/davidson_alexis_students_columbusstate_edu/Ec7Us0sBtzdCvPLC3uAlFi0Be6gxC1Gm52SU9cDAUGJglA?e=MVbbT6)
- [Architectural Documents](https://github.com/user-attachments/files/19558743/arch_diagrams.pdf)