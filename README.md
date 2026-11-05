# Learn Java — JavaFX

A collection of simple JavaFX projects for learning how to build graphical applications in Java through practical, standalone examples.

## Contents

The projects are located in the [`code/`](code/) directory and cover a range of topics:

- **Getting started**: a Hello World application introducing JavaFX.
- **Desktop applications**: calculator, text editor, painting, geometry, temperature conversion, regular expressions, and webcam access.
- **Games**: cards, puzzles, and other simple games.
- **Visualization and computation**: fractals, collection benchmarks, and sums of reciprocals.
- **Databases**: JDBC data access examples, address books, and aircraft management.
- **REST services**: examples featuring dogs and earthquake data.
- **Concurrency**: counters, managers and workers, and producers and consumers.

The [`books/`](books/) directory contains reference material.

## Running a project

The `hello-world` example requires **JDK 21** and **Maven**. From the repository root, run:

```bash
cd code/hello-world
mvn clean javafx:run
```

Each project has its own `pom.xml`: you can open it separately in your IDE and check its dependencies and configuration. Some examples may require additional resources, such as a database, an Internet connection, or a webcam.

## Purpose

These examples are intended for learning: they provide a starting point for exploring JavaFX, modifying code, and experimenting, rather than complete production-ready applications.
