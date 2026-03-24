# Algorithms

This repository contains my work for the Algorithms course provided by Princeton. ([Booksite](https://algs4.cs.princeton.edu/home/))

It is assumed anyone exploring this repository is using Linux and has a compatible JDK installed. This repository was developed using OpenJDK 21.0.10 on Ubuntu 24.04.4 LTS running in WSL 2.

# Structure

## Demo script

The top level of this repo contains a demo script `demo.sh`, which allows execution of any individual `.java` file stored in my project directories.

```
./demo.sh example/path/to/AnyJavaFile.java arg1 arg2 arg3
```

The demo script will compile and execute the given Java file, and pass along any number of arguments to the program when executed.

## `lib/`

`lib/` contains Princeton's provided `algs4.jar` archive, as well as a `docgen.sh` script to (1) extract the files in `algs4.jar` to a new directory `lib/algs4_decompressed/`, and (2) generate JavaDocs for those decompressed files, also in a new directory `lib/docs/`. Logs are redirected from stdout and stderr into a new `log.txt`.

# Major Projects

\<none yet>

# Chapter Exercises & Problems

These are smaller projects from each chapter that I decided to do, for one reason or another. The `demo.sh` can run them all the same.

## Chapter 1: Fundamentals

### Basic Programming Model

#### BinarySearch

Simply a copy of some code listed in the book. I did this mainly as a "hello world" for my editing setup, and a test file for working out both the structure of this repo and the mechanics of the shell scripts.

`BinarySearch.java` checks input numbers against a set of whitelisted numbers, using a binary search algorithm to determine whether numbers from stdin are in the whitelist.

```
./demo.sh ch1_fundamentals/basic_programming_model/BinarySearch.java ch1_fundamentals/basic_programming_model/numsW.txt < ch1_fundamentals/basic_programming_model/numsT.txt
```

The above should output the three numbers in `numsT.txt` not present in `numsW.txt`: 15, 33, and 98. (Notice that `demo.sh` allows stdin redirection.)

#### RandomConns

Draws a circle of `N` points, and with probability `p` connects each pair of points.

```
./demo.sh ch1_fundamentals/basic_programming_model/RandomConns.java N p
```

Some values to try:
| N | p |
| - | - |
| 10 | 0.5 |
| 30 | 0.2 |
| 200 | 0.01 |

### Data Abstraction

#### Interval2DClient

Uses the provided `Interval2D` class to create N 2-dimensional intervals dispersed randomly through the unit square, where each `Interval2D` is bounded by provided min and max values. All `Interval2D`s are drawn and compared, with basic stats printed to stdout.

```
./demo.sh ch1_fundamentals/data_abstraction/Interval2DClient.java N min max
```

Some values to try:
| N | min | max |
| - | - | - |
| 5 | 0.2 | 0.8 |
| 20 | 0.05 | 0.3 |
| 100 | 0 | 0.1 |
| 100 | 0 | 0.5 |
| 100 | 0 | 1 |

#### Rational

An ADT (abstract data type) for storing rational numbers. The book outlines an API:

![Rational API. Includes constructor Rational, and methods plus, minus, times, divides, equals, and toString.](docs/rational_api.png)

As a bonus, my `Rational` ADT also controls for integer overflow and underflow.

The `main` method of [`Rational.java`](./ch1_fundamentals/data_abstraction/Rational.java) includes assertions to test functionality; see that method for more details. (Using the demo script on `Rational.java` will produce no output, since all assertions pass.)