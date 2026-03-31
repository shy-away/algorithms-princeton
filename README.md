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

## Queues

![Deques and randomized queues.](docs/deques_randomized_queues.png)

This project is about working with arrays and linked lists to create and use abstract data structures. My code fulfills 100% of the testing requirements. See the [specification](https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php) for more details.

### [Deque.java](queues/Deque.java)

A deque ("deck") is a generalization of both a stack and a queue. It allows for adding an item (of a generic type) to either the front or back, and likewise allows for removing an item from either the front or back.

For constant time complexity in all operations (including the iterator's constructor and methods), `Deque.java` uses a doubly linked list as its underlying data structure.

### [RandomizedQueue.java](queues/RandomizedQueue.java)

A randomized queue is a collection of items that can be randomly retrieved (or simply sampled, without removing them from the queue). Additionally, all iterators of a randomized queue are _distinct_; if two iterators are created from the same randomized queue, they will iterate over the items in _independently_ random orders.

To accomplish this, `RandomizedQueue.java` uses a dynamically resizing array as its underlying data structure. Resizing operations do consume extra time and memory, but the _amortized_ time complexity is still O(1) for all operations besides the iterator constructor, which is O(n).

### [Permutation.java](queues/Permutation.java)

`Permutation.java` is simply a client of `RandomizedQueue.java`. It takes as input _k_ and a set of strings, and prints a randomized subset of length _k_ of the given strings.

Example usage:

```
$ more queues/distinct.txt
A B C D E F G H I

$ ./demo.sh queues/Permutation.java 3 < queues/distinct.txt
B
E
F

$ more queues/tinyTale.txt
it was the best of times it was the worst of times

$ ./demo.sh queues/Permutation.java 5 < queues/tinyTale.txt
best
of
worst
was
was
```

The booksite provides other test input that I've included. Run `tree queues -P *.txt` to see all options.

**Note:** It is assumed that $ 0 \leq k \leq n $ where _n_ is the number of strings in the input. Violating this will cause a runtime error. For example:

```
$ ./demo.sh queues/Permutation.java 100 < queues/permutation4.txt
D
B
C
A
Exception in thread "main" java.util.NoSuchElementException
        at RandomizedQueue$RandomArrayIterator.next(RandomizedQueue.java:88)
        at Permutation.main(Permutation.java:16)
```

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

### Bags, Queues, and Stacks

#### ResizingArrayStack, Stack, Queue, Bag

These were all mostly retyped from the book. I did this to deeply understand their implementations before working up to other exercises, and ultimately the Queues project.

Each of these programs' main methods contains unit tests, some of which produce output (mostly nonsense testing variables).

[ResizingArrayStack.java](ch1_fundamentals/bags_queues_stacks/ResizingArrayStack.java) is implemented using a dynamically resizing array. [Stack.java](ch1_fundamentals/bags_queues_stacks/Stack.java) uses a (singly) linked list, as do [Queue.java](ch1_fundamentals/bags_queues_stacks/Queue.java) and [Bag.java](ch1_fundamentals/bags_queues_stacks/Bag.java).

#### LinkedListGeneric

This class meets many (though not all) of the linked list exercises found in the book. I built this class to better understand how to work with linked lists, again before starting work on the Queues project.

#### Parentheses (and ParenthesesTest)

`Parentheses.java` takes a string of parentheses, braces, and brackets, and using a Stack (from [Stack.java](ch1_fundamentals/bags_queues_stacks/Stack.java)), determines whether it is a valid sequence. `ParenthesesTest.java` was simply for TDD.

```
$ ./demo.sh ch1_fundamentals/bags_queues_stacks/Parentheses.java "(()"
false

$ ./demo.sh ch1_fundamentals/bags_queues_stacks/Parentheses.java "[{}({}[])]"
true
```

### Analysis of Algorithms

#### DoublingTest, ThreeSum

These were more or less retyped from the book. `ThreeSum.java` expects a series of integers from stdin, after which it will run a static method `count`, a cubic-time algorithm to find all triples that sum to 0. It also includes a static method `countFast` that works in quadratic + logarithmic time, $O(n^2\lg(n))$. (This is because a binary search is executed $n^2$ times.)

`DoublingTest.java` tests progressively larger (randomly generated) arrays against `countFast`, printing the input size and time elapsed to stdout.

```
$ ./demo.sh ch1_fundamentals/analysis_of_algorithms/DoublingTest.java
    250   0.0
    500   0.0
   1000   0.0
   2000   0.1
   4000   0.3
   8000   1.1
  16000   4.4
  32000  18.4
```

#### ClosestPair, DoublingRatio (plus Pair, GenerateDoubles)

`ClosestPair.java` takes a series of numbers (Java `double`s) from stdin and using its own static method `findClosestPair`, finds the closest pair of them. Instead of searching every pair, the input is first sorted and then traversed in linear time. Given that Java's `Arrays.sort` is linearithmic, the overall performance of `findClosestPair` is also linearithmic, $O(n\lg(n))$.

The return value of `findClosestPair` is a custom ADT `Pair`, which is a simple pair of two generic variables. I created it just so that I can return two values at once, which Java doesn't natively allow.

`DoublingRatio.java` is a copy of `DoublingTest.java` with modifications: (1) it tests `findClosestPair`, (2) it runs batches of timed trials and calculates an average time, and (3) it calculates the ratio of different problem sizes, printed to a third column.

In this case, since the algorithm in question is linearithmic, doubling the problem size takes slightly more than double the time, on average.

```
$ ./demo.sh ch1_fundamentals/analysis_of_algorithms/DoublingRatio.java
    250   0.0   0.0
    500   0.0   NaN
   1000   0.0   NaN
   2000   0.0 Infinity
   4000   0.0   2.0
   8000   0.0   1.0
  16000   0.0   2.5
  32000   0.0   1.4
  64000   0.0   1.4
 128000   0.0   2.2
 256000   0.0   1.2
 512000   0.0   1.4
1024000   0.1   2.1
2048000   0.2   2.0
4096000   0.3   2.1
8192000   0.7   2.1
16384000   1.3   2.0
32768000   3.0   2.2
65536000   5.9   2.0
131072000  12.8   2.2
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
```

Also, `GenerateDoubles.java` is just for generating a list of random doubles, in case you want to run `ClosestPair.java` without using `DoublingRatio.java`. It generates N doubles and prints them to stdout.

```
./demo.sh ch1_fundamentals/analysis_of_algorithms/GenerateDoubles.java 3
-130239.18919157668
-896684.4776422336
875496.4231525634
```

`1Mdoubles.txt` was generated using `GenerateDoubles.java`. It's nearly 18Mb, so open it with caution.

```
$ ./demo.sh ch1_fundamentals/analysis_of_algorithms/ClosestPair.java < ch1_fundamentals/analysis_of_algorithms/1Mdoubles.txt
(999996.1746621621, 999999.1425217595)
```

#### ThrowEggs (and Building)

![A graph of data from my ThrowEggs program.](docs/egg_drops_500.png)

```
./demo.sh ch1_fundamentals/analysis_of_algorithms/ThrowEggs.java 500
```

`ThrowEggs` contains my solutions to four versions of one of the creative problems listed in the book.

The problems are about dropping eggs from different floors of a building to determine the lowest floor at which eggs will crack when dropped. The height of the building is N, and the target floor is F. `Building.java` is just a handy way to encapsulate those two variables, such that after creating a `Building`, I can get how many floors it has but _cannot_ simply access its target floor. I also made a test method `throwEgg(int floor)` that returns a boolean indicating whether the egg dropped from the given floor cracked or not.

- The red line is $O(N)$ -- drop an egg at consecutive floors, starting from the bottom, until the correct floor is found. This is a basic linear solution, but I can do better.

- The yellow line is $O(\lg(N))$. Start at the middle floor, and based on whether the egg cracks, search the corresponding half in the same manner -- if cracked, search below; if not cracked, search above. Repeat until the window of possible floors is narrowed to one floor.

- The green lines are $O(\lg(F))$, using an algorithm based not on the total size of the building N, but on the target floor F. Starting at the ground floor, move to the next power-of-2 floor (0 &#8594; 1 &#8594; 2 &#8594; 4 etc.) until finding a floor where the egg breaks. Then, binary-search the last "chunk" between the known egg-cracking floor and the previous non-egg-cracking floor. The lighter green line represents the average number of throws per building size; the darker green line represents the average number of throws _depending on F_, i.e. the x-axis is F instead of N.

- The cyan line is $O(\sqrt{N})$ when given **_only two eggs!_** This is accomplished by dividing the building into "chunks" of size $\sqrt{N}$. Drop the first egg at the beginning of every chunk, starting at the first floor, until finding the first chunk where the egg cracks. Then, search the previous chunk one floor at a time (ascending).

- The purple lines are $O(\sqrt{F})$, also when given **_only two eggs._** Instead of using chunks of size $\sqrt{N}$, use chunks between _squares_! Starting at the bottom floor, move to the next square floor (0 &#8594; 1 &#8594; 4 &#8594; 9 etc.) until the first egg cracks. Then, move back to the previous square floor and search one floor at a time (ascending). As with the green lines, the ligher purple line represents the average number of throws per building size, and the darker purple points represent the average number of throws _depending on F_.
